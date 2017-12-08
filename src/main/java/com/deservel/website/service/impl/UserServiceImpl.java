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
import com.deservel.website.common.exception.TipRestException;
import com.deservel.website.config.WebSiteConst;
import com.deservel.website.dao.LogsMapper;
import com.deservel.website.dao.UsersMapper;
import com.deservel.website.model.dto.LogActions;
import com.deservel.website.model.po.Logs;
import com.deservel.website.model.po.Users;
import com.deservel.website.service.UserService;
import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DeserveL
 * @date 2017/12/1 14:28
 * @since 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UsersMapper usersMapper;
    @Autowired
    LogsMapper logsMapper;

    /**
     * 根据用户uid查找用户
     *
     * @param uid
     * @return
     */
    @Override
    public Users queryUserById(Integer uid) {
        Users users = null;
        if (null != uid) {
            users = usersMapper.selectByPrimaryKey(uid);
        }
        return users;
    }

    /**
     * 更新个人信息
     *
     * @param uid
     * @param screenName
     * @param email
     * @param remoteIp
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProfile(Integer uid, String screenName, String email, String remoteIp) {
        if (StringUtils.isNotBlank(screenName) && StringUtils.isNotBlank(email)) {
            Users temp = new Users();
            temp.setUid(uid);
            temp.setScreenName(screenName);
            temp.setEmail(email);
            int i = usersMapper.updateByPrimaryKeySelective(temp);
            if (i > 0) {
                Logs logs = LogActions.logInstance(LogActions.UP_INFO, new Gson().toJson(temp), uid, remoteIp);
                logsMapper.insert(logs);
                return true;
            }
        }else{
            throw new TipRestException(ExceptionType.PASSWORD_BLANK);
        }
        return false;
    }

    /**
     * 更新密码
     *
     * @param user
     * @param oldPassword
     * @param password
     * @param remoteIp
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean upPwd(Users user, String oldPassword, String password, String remoteIp) {
        if (null == user || StringUtils.isBlank(oldPassword) || StringUtils.isBlank(password)) {
            throw new TipRestException(ExceptionType.PASSWORD_BLANK);
        }

        if (!user.getPassword().equals(DigestUtils.md5Hex(user.getUsername() + oldPassword))) {
            throw new TipRestException(ExceptionType.OLD_PASSWORD_ERROR);
        }
        if (password.length() < WebSiteConst.PASSWORD_MIN_COUNT || password.length() > WebSiteConst.PASSWORD_MAX_COUNT) {
            throw new TipRestException(ExceptionType.PASSWORD_LEN);
        }

        Users userUp = new Users();
        userUp.setUid(user.getUid());
        userUp.setPassword(DigestUtils.md5Hex(user.getUsername() + password));

        int i = usersMapper.updateByPrimaryKeySelective(userUp);
        if (i > 0) {
            Logs logs = LogActions.logInstance(LogActions.UP_PWD, null, user.getUid(), remoteIp);
            logsMapper.insert(logs);
            return true;
        }
        return false;
    }
}
