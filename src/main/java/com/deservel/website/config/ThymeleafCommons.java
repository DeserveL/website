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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 主题公共函数
 *
 * @author DeserveL
 * @date 2017/12/4 16:50
 * @since 1.0.0
 */
@Component
public class ThymeleafCommons {

    /**
     * 获取随机数
     *
     * @param max
     * @param str
     * @return
     */
    public static String random(int max, String str) {
        return RandomUtils.nextInt(1, max) + str;
    }

    /**
     * 返回github头像地址
     *
     * @param email
     * @return
     */
    public static String gravatar(String email) {
        String avatarUrl = "https://github.com/identicons/";
        if (StringUtils.isBlank(email)) {
            email = "user@hanshuai.xin";
        }
        String hash = DigestUtils.md5Hex(email.trim().toLowerCase());
        return avatarUrl + hash + ".png";
    }
}
