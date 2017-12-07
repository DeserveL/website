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

import com.deservel.website.common.bean.RestResponse;
import com.deservel.website.controller.AbstractBaseController;
import com.deservel.website.model.po.Comments;
import com.deservel.website.service.CommentService;
import com.deservel.website.service.SiteService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    SiteService siteService;

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

    /**
     * 删除一条评论
     *
     * @param coid
     * @return
     */
    @PostMapping(value = "delete")
    @ResponseBody
    public RestResponse delete(@RequestParam Integer coid) {
        boolean result = commentService.deleteById(coid);
        if (!result) {
            return RestResponse.fail("评论删除失败");
        }
        //清除后台配置缓存
        siteService.cleanStatisticsCache();
        return RestResponse.ok();
    }

    /**
     * 更新评论状态
     *
     * @param coid
     * @param status
     * @return
     */
    @PostMapping(value = "status")
    @ResponseBody
    public RestResponse status(@RequestParam Integer coid, @RequestParam String status) {
        boolean result = commentService.updateStatusByCoid(coid, status);
        if (!result) {
            return RestResponse.fail("操作失败");
        }
        //清除后台配置缓存
        siteService.cleanStatisticsCache();
        return RestResponse.ok();
    }

    /**
     * 回复评论
     *
     * @param coid
     * @param content
     * @return
     */
    @PostMapping(value = "")
    @ResponseBody
    public RestResponse replay(@RequestParam Integer coid, @RequestParam String content) {
        boolean result = commentService.replay(coid, content, getUserByRequest(), getRemoteIp());
        if (!result) {
            return RestResponse.fail("回复失败");
        }
        //清除后台配置缓存
        siteService.cleanStatisticsCache();
        return RestResponse.ok();
    }
}
