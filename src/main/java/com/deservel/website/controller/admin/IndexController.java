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
import com.deservel.website.config.WebSiteTools;
import com.deservel.website.controller.AbstractBaseController;
import com.deservel.website.model.dto.Statistics;
import com.deservel.website.model.dto.Types;
import com.deservel.website.model.po.Comments;
import com.deservel.website.model.po.Contents;
import com.deservel.website.model.po.Logs;
import com.deservel.website.model.po.Users;
import com.deservel.website.service.LogService;
import com.deservel.website.service.SiteService;
import com.deservel.website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理首页
 *
 * @author DeserveL
 * @date 2017/11/30 16:39
 * @since 1.0.0
 */
@Controller
@RequestMapping("/admin")
public class IndexController extends AbstractBaseController {

    @Autowired
    SiteService siteService;
    @Autowired
    LogService logService;
    @Autowired
    UserService userService;

    /**
     * 后台首页
     *
     * @return
     */
    @GetMapping(value = {"", "/index"})
    public String index() {
        try {
            //最新的5条评论
            List<Comments> comments = siteService.recentComments(5);
            //最新的5跳文章
            List<Contents> contents = siteService.recentContents(Types.RECENT_ARTICLE, 5);
            //获取后台统计数据
            Statistics statistics = siteService.getStatistics();
            // 取最新的20条日志
            List<Logs> logs = logService.getLogs(1, 5);

            setRequestAttribute("comments", comments);
            setRequestAttribute("articles", contents);
            setRequestAttribute("statistics", statistics);
            setRequestAttribute("logs", logs);
            return "admin/index";
        } catch (Exception e) {
            return errorPage(e);
        }
    }

    /**
     * 个人设置页面
     */
    @GetMapping(value = "profile")
    public String profile() {
        return "admin/profile";
    }

    /**
     * 保存个人信息
     */
    @PostMapping(value = "profile")
    @ResponseBody
    public RestResponse saveProfile(@RequestParam(value = "screen_name") String screenName, @RequestParam String email) {
        Users user = getUserByRequest();
        boolean result = userService.updateProfile(user.getUid(), screenName, email, getRemoteIp());
        if (!result) {
            return RestResponse.fail("更新信息失败");
        }
        //更新session中的数据
        user.setScreenName(screenName);
        user.setEmail(email);
        WebSiteTools.setLoginUser(getRequest(), user);

        return RestResponse.ok();
    }

    /**
     * 修改密码
     */
    @PostMapping(value = "/password")
    @ResponseBody
    public RestResponse upPwd(@RequestParam String oldPassword, @RequestParam String password) {
        Users user = getUserByRequest();
        boolean result = userService.upPwd(user, oldPassword, password, getRemoteIp());
        if (!result) {
            return RestResponse.fail("密码修改失败");
        }
        return RestResponse.ok();
    }
}
