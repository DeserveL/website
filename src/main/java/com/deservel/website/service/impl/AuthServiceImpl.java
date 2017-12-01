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
import com.deservel.website.common.bean.RestResponse;
import com.deservel.website.common.exception.TipRestException;
import com.deservel.website.service.AuthService;
import org.springframework.stereotype.Service;

/**
 * @author DeserveL
 * @date 2017/12/1 17:11
 * @since 1.0.0
 */
@Service
public class AuthServiceImpl implements AuthService {

    protected MapCache cache = MapCache.single();

    /**
     * 登录操作
     *
     * @param username
     * @param password
     * @param remoteIp
     * @return
     */
    @Override
    public RestResponse doLogin(String username, String password, String remoteIp) {
        cache.get("login_error_count"+username);
        throw new TipRestException(ExceptionType.AUTHORIZATION_ERROR);
    }
}
