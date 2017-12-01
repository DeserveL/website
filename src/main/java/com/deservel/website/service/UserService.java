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
package com.deservel.website.service;

import com.deservel.website.model.po.Users;

/**
 * 用户
 *
 * @author DeserveL
 * @date 2017/12/1 14:26
 * @since 1.0.0
 */
public interface UserService {

    /**
     * 根据用户uid查找用户
     *
     * @param uid
     * @return
     */
    Users queryUserById(Integer uid);
}
