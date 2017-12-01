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

import com.deservel.website.common.utils.AesUtils;
import com.deservel.website.common.utils.CookieUtils;
import com.deservel.website.model.po.Users;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 网站公用方法类
 *
 * @author DeserveL
 * @date 2017/12/1 13:58
 * @since 1.0.0
 */
public class WebSiteTools {

    /**
     * 返回当前登录用户
     *
     * @return
     */
    public static Users getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (null == session) {
            return null;
        }
        return (Users) session.getAttribute(WebSiteConst.LOGIN_SESSION_KEY);
    }

    /**
     * 当前登录用户存入session
     *
     * @return
     */
    public static void setLoginUser(HttpServletRequest request, Users users) {
        HttpSession session = request.getSession();
        session.setAttribute(WebSiteConst.LOGIN_SESSION_KEY, users);
    }

    /**
     * 当前登录用户删除session
     *
     * @return
     */
    public static void removeLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(WebSiteConst.LOGIN_SESSION_KEY);
    }

    /**
     * 获取cookie中的用户id
     *
     * @param request
     * @return
     */
    public static Integer getCookieUid(HttpServletRequest request) {
        if (null != request) {
            Cookie cookie = CookieUtils.cookieRaw(WebSiteConst.USER_IN_COOKIE, request);
            if (cookie != null && cookie.getValue() != null) {
                try {
                    String uid = AesUtils.deAes(cookie.getValue(), WebSiteConst.AES_SALT);
                    return StringUtils.isNotBlank(uid) && StringUtils.isNumeric(uid) ? Integer.valueOf(uid) : null;
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 设置记住密码cookie
     *
     * @param response
     * @param uid
     */
    public static void setCookieUid(HttpServletResponse response, Integer uid) throws Exception {
        String val = AesUtils.enAes(uid.toString(), WebSiteConst.AES_SALT);
        CookieUtils.setCookie(response, WebSiteConst.USER_IN_COOKIE, val);
    }


    /**
     * 删除cookie登录信息
     *
     * @param response
     */
    public static void removeCookieUid(HttpServletResponse response) {
        CookieUtils.removeCookie(response, WebSiteConst.USER_IN_COOKIE);
    }
}
