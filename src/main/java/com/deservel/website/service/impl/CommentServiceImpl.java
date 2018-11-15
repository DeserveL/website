/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.deservel.website.service.impl;

import com.deservel.website.common.bean.ExceptionType;
import com.deservel.website.common.exception.TipRestException;
import com.deservel.website.common.utils.DateUtils;
import com.deservel.website.common.utils.HtmlUtils;
import com.deservel.website.config.WebSiteConst;
import com.deservel.website.dao.CommentsMapper;
import com.deservel.website.dao.ContentsMapper;
import com.deservel.website.model.dto.CommentDto;
import com.deservel.website.model.po.Comments;
import com.deservel.website.model.po.Contents;
import com.deservel.website.model.po.Users;
import com.deservel.website.service.CommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DeserveL
 * @date 2017/12/7 17:04
 * @since 1.0.0
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentsMapper commentsMapper;

    @Autowired
    ContentsMapper contentsMapper;

    /**
     * 获取评论列表
     *
     * @param uid
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageInfo<Comments> getCommentsWithPage(Integer uid, int page, int limit) {
        Condition condition = new Condition(Comments.class);
        condition.setOrderByClause("coid desc");
        condition.createCriteria().andNotEqualTo("authorId", 1);
        PageHelper.startPage(page, limit);
        List<Comments> comments = commentsMapper.selectByCondition(condition);
        return new PageInfo<>(comments);
    }

    /**
     * 获取文章评论列表
     *
     * @param uid
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageInfo<CommentDto> getArticleComments(Integer uid, int page, int limit) {
        PageHelper.startPage(page, limit);
        List<CommentDto> comments = commentsMapper.selectArticleCommentsById(uid);
        PageInfo<CommentDto> pageInfo=new PageInfo<>(comments);
        for (CommentDto comment:pageInfo.getList()){
            //获取子评论
            setChildrenComment(comment);
        }
        return pageInfo;
    }

    /**
     * 删除评论
     *
     * @param coid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Integer coid) {
        Comments comments = commentsMapper.selectByPrimaryKey(coid);
        if (null == comments) {
            throw new TipRestException(ExceptionType.COMMENT_COID_BLANK);
        }

        int i = commentsMapper.deleteByPrimaryKey(coid);
        if (i > 0) {
            //更新文章评论数
            Contents ci = contentsMapper.selectByPrimaryKey(comments.getCid());
            Contents contents = new Contents();
            contents.setCid(ci.getCid());
            contents.setCommentsNum(ci.getCommentsNum() - 1);
            contentsMapper.updateByPrimaryKeySelective(contents);
            return true;
        }
        return false;
    }

    /**
     * 更新评论状态
     *
     * @param coid
     * @param status
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatusByCoid(Integer coid, String status) {
        Comments comments = new Comments();
        comments.setCoid(coid);
        comments.setStatus(status);
        commentsMapper.updateByPrimaryKeySelective(comments);
        return true;
    }

    /**
     * 回复评论
     *
     * @param coid
     * @param content
     * @param user
     * @param remoteIp
     * @return
     */
    @Override
    public boolean replay(Integer coid, String content, Users user, String remoteIp) {
        if (null == coid || StringUtils.isBlank(content)) {
            throw new TipRestException(ExceptionType.COMMENT_COID_BLANK);
        }
        //长度
        if (content.length() < WebSiteConst.MIN_COMMENT_COUNT || content.length() > WebSiteConst.MAX_COMMENT_COUNT) {
            throw new TipRestException(ExceptionType.COMMENT_COUNT);
        }
        //父评论
        Comments commentParent = commentsMapper.selectByPrimaryKey(coid);
        if (null == commentParent) {
            throw new TipRestException(ExceptionType.COMMENT_COID_BLANK);
        }
        //用户信息
        if (null == user) {
            throw new TipRestException(ExceptionType.USER_NOT_FOUND);
        }
        //回复内容处理
        content = HtmlUtils.cleanXSS(content);
        content = EmojiParser.parseToAliases(content);

        Comments comments = new Comments();
        comments.setAuthor(user.getUsername());
        comments.setAuthorId(user.getUid());
        comments.setCid(commentParent.getCid());
        comments.setIp(remoteIp);
        comments.setUrl(user.getHomeUrl());
        comments.setContent(content);
        if (StringUtils.isNotBlank(user.getEmail())) {
            comments.setMail(user.getEmail());
        } else {
            comments.setMail("support@deservel.top");
        }
        comments.setParent(coid);
        //保存
        if (this.saveComment(comments)) {
            return true;
        }
        return false;
    }

    /**
     * 保存评论
     *
     * @param comments
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveComment(Comments comments) {
        if (comments.getContent().length() < WebSiteConst.MIN_COMMENT_COUNT || comments.getContent().length() > WebSiteConst.MAX_COMMENT_COUNT) {
            throw new TipRestException(ExceptionType.COMMENT_COUNT);
        }
        if (null == comments.getCid()) {
            throw new TipRestException(ExceptionType.COMMENT_CID_BLANK);
        }
        Contents contents = contentsMapper.selectByPrimaryKey(comments.getCid());
        if (null == contents) {
            throw new TipRestException(ExceptionType.COMMENT_CONTENT_BLANK);
        }

        comments.setOwnerId(contents.getAuthorId());
        comments.setCreated(DateUtils.getCurrentUnixTime());
        comments.setCoid(null);
        int i = commentsMapper.insertSelective(comments);
        if (i > 0) {
            Contents temp = new Contents();
            temp.setCommentsNum(contents.getCommentsNum() + 1);
            temp.setCid(contents.getCid());
            contentsMapper.updateByPrimaryKeySelective(temp);
            return true;
        }
        return false;
    }

    /**
     * 获取该评论下的追加评论
     *
     * @param coid 评论Id
     * @return
     */
    private void getChildren(List<Comments> list, Integer coid) {
        Condition condition = new Condition(Comments.class);
        condition.setOrderByClause("coid asc");
        condition.createCriteria()
                 .andEqualTo("parent",coid)
                 .andEqualTo("status","approved");
        List<Comments> cms =commentsMapper.selectByCondition(condition);
        if (null != cms) {
            list.addAll(cms);
            cms.forEach(c -> getChildren(list, c.getCoid()));
        }
    }

    /**
     * 將評論以及子评论封装
     *
     * @param parent 一级评论
     * @return
     */
    private void setChildrenComment(CommentDto parent) {
        List<Comments> children = new ArrayList<>();
        getChildren(children, parent.getCoid());
        parent.setChildren(children);
        if (children.size()>0) {
            parent.setLevels(1);
        }
    }
}
