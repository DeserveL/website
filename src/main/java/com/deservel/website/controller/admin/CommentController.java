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
package com.deservel.website.controller.admin;

import com.deservel.website.controller.AbstractBaseController;
import com.deservel.website.model.po.Comments;
import com.deservel.website.service.CommentService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author DeserveL
 * @date 2017/12/7 16:55
 * @since 1.0.0
 */
@Controller
@RequestMapping("admin/comments")
public class CommentController extends AbstractBaseController {

    @Autowired
    CommentService commentService;

    /**
     * 评论列表页面
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit) {
        try {
            PageInfo<Comments> commentsPaginator = commentService.getCommentsWithPage(this.getUserByRequest().getUid(), page, limit);
            setRequestAttribute("comments", commentsPaginator);
            return "admin/comment_list";
        } catch (Exception e) {
            return errorPage(e);
        }
    }
}
