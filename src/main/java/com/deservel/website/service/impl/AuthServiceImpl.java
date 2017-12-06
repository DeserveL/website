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
package com.deservel.website.service.impl;

import com.deservel.website.common.bean.ExceptionType;
import com.deservel.website.common.cache.MapCache;
import com.deservel.website.common.exception.TipRestException;
import com.deservel.website.config.WebSiteConst;
import com.deservel.website.dao.LogsMapper;
import com.deservel.website.dao.UsersMapper;
import com.deservel.website.model.dto.LogActions;
import com.deservel.website.model.po.Logs;
import com.deservel.website.model.po.Users;
import com.deservel.website.service.AuthService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DeserveL
 * @date 2017/12/1 17:11
 * @since 1.0.0
 */
@Service
public class AuthServiceImpl implements AuthService {

    protected MapCache cache = MapCache.single();

    @Autowired
    UsersMapper usersMapper;
    @Autowired
    LogsMapper logsMapper;

    /**
     * 登录操作
     *
     * @param username
     * @param password
     * @param remoteIp
     * @return
     */
    @Override
    public Users doLogin(String username, String password, String remoteIp) {
        //非空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new TipRestException(ExceptionType.USERNAME_PASSWORD_BLANK);
        }
        //用户错误次数
        Integer error_count = cache.get("login_error_count" + username);
        if (null != error_count && error_count > WebSiteConst.PASSWORD_ERROR_COUNT) {
            throw new TipRestException(ExceptionType.USERNAME_PASSWORD_ERROR_COUNT);
        }
        Users users = new Users();
        users.setUsername(username);
        //查询用户
        int count = usersMapper.selectCount(users);
        if (count < 1) {
            throw new TipRestException(ExceptionType.USER_NOT_FOUND);
        }
        String pwd = DigestUtils.md5Hex(username + password);
        users.setPassword(pwd);
        List<Users> userList = usersMapper.select(users);
        if (userList.size() != 1) {
            error_count = null == error_count ? 1 : error_count + 1;
            cache.set("login_error_count" + username, error_count, WebSiteConst.PASSWORD_ERROR_TIME);
            throw new TipRestException(ExceptionType.USERNAME_PASSWORD_ERROR);
        }
        Logs logs = LogActions.logInstance(LogActions.LOGIN, null, userList.get(0).getUid(), remoteIp);
        logsMapper.insert(logs);
        return userList.get(0);
    }
}
