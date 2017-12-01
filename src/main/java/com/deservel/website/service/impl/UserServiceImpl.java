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

import com.deservel.website.dao.UsersMapper;
import com.deservel.website.model.po.Users;
import com.deservel.website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author DeserveL
 * @date 2017/12/1 14:28
 * @since 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UsersMapper usersMapper;

    /**
     * 根据用户uid查找用户
     *
     * @param uid
     * @return
     */
    @Override
    public Users queryUserById(Integer uid) {
        Users users = null;
        if(null != uid){
            users = usersMapper.selectByPrimaryKey(uid);
        }
        return users;
    }
}
