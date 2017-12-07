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

    CONTENTS_ALL_BLANK(700, "文章对象为空"),
    CONTENTS_TITLE(701, "文章标题不能为空"),
    CONTENTS_TITLE_LENGTH(702, "文章标题最多可以输入" + WebSiteConst.MAX_TITLE_COUNT + "个字符"),
    CONTENTS_BLANK(703, "文章内容不能为空"),
    CONTENTS_BLANK_LENGTH(704, "文章内容最多可以输入" + WebSiteConst.MAX_TEXT_COUNT + "个字符"),
    CONTENTS_LOGIN(705, "请登录后发布文章"),
    CONTENTS_SLUG_LENGTH(706, "路径不能少于" + WebSiteConst.MIN_SLUG_COUNT + "个字符"),
    CONTENTS_SLUG_PATH(707, "您输入的路径不合法"),
    CONTENTS_SLUG_EXIST(708, "该路径已经存在，请重新输入"),

    COMMENT_COID_BLANK(710, "不存在该评论"),
    COMMENT_BLANK(711, "请输入完整后评论"),
    COMMENT_COUNT(712, "请输入" + WebSiteConst.MIN_COMMENT_COUNT + "-"+ WebSiteConst.MAX_COMMENT_COUNT +"个字符内的回复"),
    COMMENT_CID_BLANK(713, "评论文章不能为空"),
    COMMENT_CONTENT_BLANK(714, "不存在的文章"),

    MTEA_CID_BLANK(720, "项目关联id不能为空");

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
