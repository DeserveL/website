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

import com.deservel.website.model.po.Logs;
import com.deservel.website.service.LogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DeserveL
 * @date 2017/11/30 17:07
 * @since 1.0.0
 */
@Service
public class LogServiceImpl implements LogService {
    /**
     * 获取日志信息
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public List<Logs> getLogs(Integer i, Integer i1) {
        return null;
    }
}
