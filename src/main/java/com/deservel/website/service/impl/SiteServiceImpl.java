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
import com.deservel.website.dao.ContentsMapper;
import com.deservel.website.model.dto.Statistics;
import com.deservel.website.model.po.Comments;
import com.deservel.website.model.po.Contents;
import com.deservel.website.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * @author DeserveL
 * @date 2017/11/30 17:07
 * @since 1.0.0
 */
@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    CommentsMapper commentsMapper;
    @Autowired
    ContentsMapper contentsMapper;


    /**
     * 最新收到的评论
     *
     * @param i
     * @return
     */
    @Override
    public List<Comments> recentComments(Integer i) {
        return null;
    }

    /**
     * 最新发表的文章
     *
     * @param i
     * @return
     */
    @Override
    public List<Contents> recentContents(Integer i) {
        return null;
    }

    /**
     * 获取后台统计数据
     *
     * @return
     */
    @Override
    public Statistics getStatistics() {
        return null;
    }
}
