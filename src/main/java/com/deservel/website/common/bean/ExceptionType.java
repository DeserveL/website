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
package com.deservel.website.common.bean;

import com.deservel.website.config.WebSiteConst;

/**
 * 异常类型
 *
 * @author DeserveL
 * @date 2017/12/1 17:27
 * @since 1.0.0
 */
public enum ExceptionType {

    AUTHORIZATION_ERROR(401, "请先登录"),
    USERNAME_PASSWORD_BLANK(601, "用户名和密码不能为空"),
    USERNAME_PASSWORD_ERROR_COUNT(602, "您输入密码已经错误超过" + WebSiteConst.PASSWORD_ERROR_COUNT + "次，请" + WebSiteConst.PASSWORD_ERROR_TIME + "秒后尝试"),
    USER_NOT_FOUND(603, "用户不存在"),
    USERNAME_PASSWORD_ERROR(604, "用户名或密码错误"),
    ID_NOT_ONE(605, "query content by id and return is not one"),
    DATA_NOT_FOUND(404, "数据未找到"),
    PARAMETER_ILLEGAL(403, "参数无效"),
    ARTICLE_THUMBUP_ERROR(603, "已经点过赞");

    private int code;

    private String message;

    ExceptionType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
