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
package com.deservel.website.service;

import com.deservel.website.model.dto.Statistics;
import com.deservel.website.model.po.Comments;
import com.deservel.website.model.po.Contents;

import java.util.List;

/**
 * @author DeserveL
 * @date 2017/11/30 16:51
 * @since 1.0.0
 */
public interface SiteService {

    /**
     * 最新收到的评论
     *
     * @param limit
     * @return
     */
    List<Comments> recentComments(Integer limit);

    /**
     * 最新发表的文章
     *
     * @param limit
     * @return
     */
    List<Contents> recentContents(Integer limit);

    /**
     * 获取后台统计数据
     *
     * @return
     */
    Statistics getStatistics();
}
