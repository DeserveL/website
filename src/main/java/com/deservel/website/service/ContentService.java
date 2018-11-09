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

import com.deservel.website.model.po.Contents;
import com.github.pagehelper.PageInfo;

/**
 * @author DeserveL
 * @date 2017/12/6 0006 下午 22:45
 * @since 1.0.0
 */
public interface ContentService {

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
    PageInfo<Contents> getArticlesWithPage(String type, String status, Integer page, Integer limit);

    /**
     * 获取页面列表
     *
     * @param page
     * @param limit
     * @return
     */
    PageInfo<Contents> getPagesWithPage(Integer page, Integer limit);

    /**
     * 获取文章详情
     *
     * @param cid
     * @return
     */
    Contents getContents(String cid);

    /**
     * 获取相邻的文章
     *
     * @param type    上一篇:prev | 下一篇:next
     * @param created 当前文章创建时间
     */
    Contents getNhContent(String type, Integer created);

    /**
     * 添加文章
     *
     * @param contents
     * @return
     */
    boolean publish(Contents contents);

    /**
     * 修改文章
     *
     * @param contents
     * @return
     */
    boolean updateArticle(Contents contents);

    /**
     * 删除文章操作
     *
     * @param cid
     * @param uid
     * @param remoteIp
     * @param type
     * @return
     */
    boolean deleteByCid(Integer cid, Integer uid, String remoteIp, String type);
}
