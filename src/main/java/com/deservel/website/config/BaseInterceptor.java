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
package com.deservel.website.config;

import com.deservel.website.common.utils.IpUtils;
import com.deservel.website.model.po.Users;
import com.deservel.website.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 自定义拦截器
 *
 * @author DeserveL
 * @date 2017/12/1 13:30
 * @since 1.0.0
 */
@Component
public class BaseInterceptor implements HandlerInterceptor{
    private static final Logger LOGGE = LoggerFactory.getLogger(BaseInterceptor.class);
    private static final String USER_AGENT = "user-agent";

    @Autowired
    UserService userService;
    @Autowired
    ThymeleafCommons thymeleafCommons;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String uri = request.getRequestURI();
        //打印访问日志
        if(LOGGE.isInfoEnabled()){
            LOGGE.info("UserAgent: {}", request.getHeader(USER_AGENT));
            LOGGE.info("用户访问地址: {}, 来路地址: {}", uri, IpUtils.getIpAddrByRequest(request));
        }

        //在session cookie信息中查找用户信息（cookie可以伪造，有问题）
        Users loginUser = WebSiteTools.getLoginUser(request);
        if(null == loginUser){
            Integer uid = WebSiteTools.getCookieUid(request);
            if(null != uid){
                loginUser = userService.queryUserById(uid);
                WebSiteTools.setLoginUser(request, loginUser);
            }
        }

        //后台页面
        if (uri.startsWith(WebSiteConst.ADMIN_REQUEST) && !uri.startsWith(WebSiteConst.ADMIN_REQUEST_LONGIN) && null == loginUser) {
            //跳转到登录页面
            response.sendRedirect(request.getContextPath() + WebSiteConst.ADMIN_REQUEST_LONGIN);
            return false;
        }
        //TODO 设置get请求的token

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        //一些工具类和公共方法
        request.setAttribute("commons",thymeleafCommons);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }
}
