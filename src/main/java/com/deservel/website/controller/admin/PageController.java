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
import com.deservel.website.config.WebSiteConst;
import com.deservel.website.controller.AbstractBaseController;
import com.deservel.website.model.dto.Types;
import com.deservel.website.model.po.Contents;
import com.deservel.website.model.po.Users;
import com.deservel.website.service.ContentService;
import com.deservel.website.service.SiteService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author DeserveL
 * @date 2017/12/8 0008 下午 21:43
 * @since 1.0.0
 */
@Controller()
@RequestMapping("admin/page")
public class PageController extends AbstractBaseController {

    @Autowired
    ContentService contentService;

    @Autowired
    SiteService siteService;

    /**
     * 页面管理页面
     *
     * @param request
     * @return
     */
    @GetMapping(value = "")
    public String index(HttpServletRequest request) {
        try {
            PageInfo<Contents> contentsPaginator = contentService.getPagesWithPage(0, WebSiteConst.MAX_POSTS);
            setRequestAttribute("articles", contentsPaginator);
            return "admin/page_list";
        } catch (Exception e) {
            return errorPage(e);
        }
    }

    /**
     * 新建页面
     *
     * @return
     */
    @GetMapping(value = "new")
    public String newPage() {
        return "admin/page_edit";
    }

    /**
     * 编辑页面
     *
     * @param cid
     * @param request
     * @return
     */
    @GetMapping(value = "/{cid}")
    public String editPage(@PathVariable String cid, HttpServletRequest request) {
        try {
            Contents contents = contentService.getContents(cid);
            request.setAttribute("contents", contents);
            return "admin/page_edit";
        } catch (Exception e) {
            return errorPage(e);
        }
    }

    /**
     * 发布页面内容
     *
     * @param contents
     * @return
     */
    @PostMapping(value = "publish")
    @ResponseBody
    public RestResponse publishPage(Contents contents) {
        Users users = this.getUserByRequest();
        contents.setAuthorId(users.getUid());
        contents.setType(Types.PAGE);
        contents.setAllowPing(true);
        boolean result = contentService.publish(contents);
        if (!result) {
            return RestResponse.fail("页面发布失败！");
        }
        //清除后台配置缓存
        siteService.cleanStatisticsCache();
        return RestResponse.ok();
    }

    /**
     * 修改页面内容
     *
     * @param contents
     * @return
     */
    @PostMapping(value = "modify")
    @ResponseBody
    public RestResponse modifyArticle(Contents contents) {
        contents.setType(Types.PAGE);
        boolean result = contentService.updateArticle(contents);
        if (!result) {
            return RestResponse.fail("修改页面失败！");
        }
        return RestResponse.ok(contents.getCid());
    }

    /**
     * 删除页面
     *
     * @param cid
     * @return
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public RestResponse delete(@RequestParam int cid) {
        boolean result = contentService.deleteByCid(cid, this.getUserByRequest().getUid(), getRemoteIp(), Types.PAGE);
        if (!result) {
            return RestResponse.fail("删除页面失败！");
        }
        //清除后台配置缓存
        siteService.cleanStatisticsCache();
        return RestResponse.ok();
    }
}
