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

import com.deservel.website.dao.CommentsMapper;
import com.deservel.website.model.po.Comments;
import com.deservel.website.service.CommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

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
}
