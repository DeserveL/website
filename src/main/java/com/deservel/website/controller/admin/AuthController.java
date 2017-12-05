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
import com.deservel.website.config.WebSiteTools;
import com.deservel.website.controller.AbstractBaseController;
import com.deservel.website.model.po.Users;
import com.deservel.website.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 用户后台登录/登出
 *
 * @author DeserveL
 * @date 2017/12/1 15:49
 * @since 1.0.0
 */
@Controller
@RequestMapping("/admin")
public class AuthController extends AbstractBaseController {

    @Autowired
    AuthService authService;

    /**
     * 登录页面
     *
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    /**
     * 登录验证
     *
     * @param username
     * @param password
     * @param remeber_me
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public RestResponse doLogin(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam(required = false) String remeber_me) {
        Users user = authService.doLogin(username, password, getRemoteIp());
        //存入session
        getSession().setAttribute(WebSiteConst.LOGIN_SESSION_KEY, user);
        //是否存入cookie
        if (StringUtils.isBlank(remeber_me) && user != null) {
            try {
                WebSiteTools.setCookieUid(getResponse(), user.getUid());
            } catch (Exception e) {
                logger.error("记住用户失败",e);
            }
        }
        return RestResponse.ok(user);
    }

    @RequestMapping("/logout")
    public void logout(){
        try {
            WebSiteTools.removeLoginUser(getRequest());
            WebSiteTools.removeCookieUid(getResponse());
            getResponse().sendRedirect("admin/login");
        } catch (IOException e) {
            logger.error("注销失败",e);
        }
    }
}
