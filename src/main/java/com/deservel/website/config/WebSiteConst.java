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

/**
 * 网站配置表
 *
 * @author DeserveL
 * @date 2017/12/1 13:55
 * @since 1.0.0
 */
public class WebSiteConst {
    /**
     * 用户session key
     */
    public static String LOGIN_SESSION_KEY = "login_user";

    /**
     *用户uid cookie key
     */
    public static final String USER_IN_COOKIE = "S_L_ID";

    /**
     * aes加密加盐
     */
    public static final String AES_SALT = "012345abcdef6789";

    /**
     * 后台请求路径
     */
    public static final String ADMIN_REQUEST = "/admin";

    /**
     * 后台登录请求路径
     */
    public static final String ADMIN_REQUEST_LONGIN = "/admin/login";
}
