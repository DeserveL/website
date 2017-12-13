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
import com.deservel.website.common.exception.TipPageException;
import com.deservel.website.common.exception.TipRestException;
import com.deservel.website.common.utils.DateUtils;
import com.deservel.website.config.WebSiteConst;
import com.deservel.website.config.WebSiteTools;
import com.deservel.website.dao.ContentsMapper;
import com.deservel.website.dao.LogsMapper;
import com.deservel.website.dao.RelationshipsMapper;
import com.deservel.website.model.dto.LogActions;
import com.deservel.website.model.dto.Types;
import com.deservel.website.model.po.Contents;
import com.deservel.website.model.po.Logs;
import com.deservel.website.model.po.Relationships;
import com.deservel.website.service.ContentService;
import com.deservel.website.service.MetaService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author DeserveL
 * @date 2017/12/6 0006 下午 22:55
 * @since 1.0.0
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    ContentsMapper contentsMapper;

    @Autowired
    RelationshipsMapper relationshipsMapper;

    @Autowired
    LogsMapper logsMapper;

    @Autowired
    MetaService metaService;

    /**
     * 新增标识
     */
    private static final String TYPE_ADD = "add";
    /**
     * 修改标识
     */
    private static final String TYPE_UPDATE = "update";

    /**
     * 获取文章列表
     *
     *
     * @param type
     * @param status
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageInfo<Contents> getArticlesWithPage(String type, String status, Integer page, Integer limit) {
        Condition condition = new Condition(Contents.class);
        condition.setOrderByClause("created desc");
        Example.Criteria criteria = condition.createCriteria();
        if(StringUtils.isNotBlank(type)){
            criteria.andEqualTo("type", type);
        }
        if(StringUtils.isNotBlank(status)){
            criteria.andEqualTo("status", status);
        }
        PageHelper.startPage(page, limit);
        List<Contents> contents = contentsMapper.selectByCondition(condition);
        return new PageInfo<>(contents);
    }

    /**
     * 获取页面列表
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageInfo<Contents> getPagesWithPage(Integer page, Integer limit) {
        Condition condition = new Condition(Contents.class);
        condition.setOrderByClause("created desc");
        condition.createCriteria().andEqualTo("type", Types.PAGE);
        PageHelper.startPage(page, limit);
        List<Contents> contents = contentsMapper.selectByCondition(condition);
        return new PageInfo<>(contents);
    }

    /**
     * 获取文章详情
     *
     * @param cid
     * @return
     */
    @Override
    public Contents getContents(String cid) {
        if (StringUtils.isNotBlank(cid)) {
            if (StringUtils.isNumeric(cid)) {
                Contents contents = contentsMapper.selectByPrimaryKey(Integer.valueOf(cid));
                return contents;
            } else {
                Condition condition = new Condition(Contents.class);
                condition.createCriteria().andEqualTo("slug", cid);
                List<Contents> contents = contentsMapper.selectByCondition(condition);
                if (contents.size() != 1) {
                    throw new TipPageException(ExceptionType.ID_NOT_ONE);
                }
                return contents.get(0);
            }
        }
        return null;
    }

    /**
     * 添加文章
     *
     * @param contents
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publish(Contents contents) {
        //内容检测
        checkContent(contents, TYPE_ADD);

        //过滤emoji字符
        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));
        int time = DateUtils.getCurrentUnixTime();
        contents.setCreated(time);
        contents.setModified(time);
        contents.setHits(0);
        contents.setCommentsNum(0);
        contentsMapper.insert(contents);

        //标签
        metaService.saveMetas(contents.getCid(), contents.getTags(), Types.TAG);
        //分类
        metaService.saveMetas(contents.getCid(), contents.getCategories(), Types.CATEGORY);
        return true;
    }

    /**
     * 修改文章
     *
     * @param contents
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateArticle(Contents contents) {
        //内容检测
        checkContent(contents, TYPE_UPDATE);

        //过滤emoji字符
        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));
        contents.setModified(DateUtils.getCurrentUnixTime());
        contents.setTags(contents.getTags() != null ? contents.getTags() : "");
        contents.setCategories(contents.getCategories() != null ? contents.getCategories() : "");
        contentsMapper.updateByPrimaryKeySelective(contents);

        //依赖关系
        Relationships relationships = new Relationships();
        relationships.setCid(contents.getCid());
        relationshipsMapper.delete(relationships);
        //标签
        metaService.saveMetas(contents.getCid(), contents.getTags(), Types.TAG);
        //分类
        metaService.saveMetas(contents.getCid(), contents.getCategories(), Types.CATEGORY);
        return true;
    }

    /**
     * 删除文章操作
     *
     * @param cid
     * @param uid
     * @param remoteIp
     * @param type
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByCid(Integer cid, Integer uid, String remoteIp, String type) {
        if (null != cid) {
            int i = contentsMapper.deleteByPrimaryKey(cid);
            if (i > 0) {
                //依赖关系
                Relationships relationships = new Relationships();
                relationships.setCid(cid);
                relationshipsMapper.delete(relationships);
                //记录日志
                String action = "";
                if(Types.ARTICLE.equals(type)){
                    action = LogActions.DEL_ARTICLE;
                }else if(Types.PAGE.equals(type)){
                    action = LogActions.DEL_PAGE;
                }
                Logs logs = LogActions.logInstance(action, cid + "", uid, remoteIp);
                logsMapper.insert(logs);
                return true;
            }
        }
        return false;
    }

    /**
     * contents类 内容检查
     *
     * @param contents
     * @param type     add,update
     */
    private void checkContent(Contents contents, String type) {
        if (null == contents) {
            throw new TipRestException(ExceptionType.CONTENTS_ALL_BLANK);
        }
        //更新的情况下 cid 必须存在
        if (TYPE_UPDATE.equals(type) && null == contents.getCid()) {
            throw new TipRestException(ExceptionType.CONTENTS_ALL_BLANK);
        }
        if (StringUtils.isBlank(contents.getTitle())) {
            throw new TipRestException(ExceptionType.CONTENTS_TITLE);
        }
        if (contents.getTitle().length() > WebSiteConst.MAX_TITLE_COUNT) {
            throw new TipRestException(ExceptionType.CONTENTS_TITLE);
        }
        if (StringUtils.isBlank(contents.getContent())) {
            throw new TipRestException(ExceptionType.CONTENTS_BLANK);
        }
        if (contents.getContent().length() > WebSiteConst.MAX_TEXT_COUNT) {
            throw new TipRestException(ExceptionType.CONTENTS_TITLE_LENGTH);
        }
        if (TYPE_ADD.equals(type) && null == contents.getAuthorId()) {
            throw new TipRestException(ExceptionType.CONTENTS_LOGIN);
        }
        if (StringUtils.isNotBlank(contents.getSlug())) {
            if (contents.getSlug().length() < WebSiteConst.MIN_SLUG_COUNT) {
                throw new TipRestException(ExceptionType.CONTENTS_SLUG_LENGTH);
            }
            if (!WebSiteTools.isPath(contents.getSlug())) {
                throw new TipRestException(ExceptionType.CONTENTS_SLUG_PATH);
            }
            //新增情况下
            if (TYPE_ADD.equals(type)) {
                //查询slug是否存在
                Contents contentsCond = new Contents();
                contentsCond.setType(contents.getType());
                contentsCond.setSlug(contents.getSlug());
                int count = contentsMapper.selectCount(contentsCond);
                if (count > 0) {
                    throw new TipRestException(ExceptionType.CONTENTS_SLUG_EXIST);
                }
            }
        }else{
            contents.setSlug(null);
        }
    }
}
