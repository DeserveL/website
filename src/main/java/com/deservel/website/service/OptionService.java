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

import com.deservel.website.model.po.Options;

import java.util.List;
import java.util.Map;

/**
 * @author DeserveL
 * @date 2017/12/11 15:13
 * @since 1.0.0
 */
public interface OptionService {

    /**
     * 获取所有配置项
     *
     * @return
     */
    Map<String, String> getOptions();

    /**
     * 保存配置项
     *
     * @param querys
     * @param remoteIp
     * @param uid
     * @return
     */
    boolean saveOptions(Map<String, String> querys, String remoteIp, Integer uid);
}
