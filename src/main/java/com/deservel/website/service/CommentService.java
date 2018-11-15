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

import com.deservel.website.model.dto.CommentDto;
import com.deservel.website.model.po.Comments;
import com.deservel.website.model.po.Users;
import com.github.pagehelper.PageInfo;

/**
 * @author DeserveL
 * @date 2017/12/7 16:59
 * @since 1.0.0
 */
public interface CommentService {

    /**
     * 获取评论列表
     *
     * @param uid
     * @param page
     * @param limit
     * @return
     */
    PageInfo<Comments> getCommentsWithPage(Integer uid, int page, int limit);

    /**
     * 获取文章评论列表
     *
     * @param uid
     * @param page
     * @param limit
     * @return
     */
    PageInfo<CommentDto> getArticleComments(Integer uid, int page, int limit);

    /**
     * 删除评论
     *
     * @param coid
     * @return
     */
    boolean deleteById(Integer coid);

    /**
     * 更新评论状态
     *
     * @param coid
     * @param status
     * @return
     */
    boolean updateStatusByCoid(Integer coid, String status);

    /**
     * 回复评论
     *
     * @param coid
     * @param content
     * @param userByRequest
     * @param remoteIp
     * @return
     */
    boolean replay(Integer coid, String content, Users userByRequest, String remoteIp);

    /**
     * 保存评论
     *
     * @param comments
     * @return
     */
    boolean saveComment(Comments comments);
}
