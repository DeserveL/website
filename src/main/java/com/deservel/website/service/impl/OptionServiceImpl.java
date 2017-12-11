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

import com.deservel.website.dao.LogsMapper;
import com.deservel.website.dao.OptionsMapper;
import com.deservel.website.model.dto.LogActions;
import com.deservel.website.model.po.Logs;
import com.deservel.website.model.po.Options;
import com.deservel.website.service.OptionService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DeserveL
 * @date 2017/12/11 15:19
 * @since 1.0.0
 */
@Service
public class OptionServiceImpl implements OptionService {

    @Autowired
    OptionsMapper optionsMapper;
    @Autowired
    LogsMapper logsMapper;

    /**
     * 获取所有配置项
     *
     * @return
     */
    @Override
    public Map<String, String> getOptions() {
        List<Options> voList = optionsMapper.selectAll();
        Map<String, String> options = new HashMap<>();
        voList.forEach((option) -> options.put(option.getName(), option.getValue()));
        return options;
    }

    /**
     * 保存配置项
     *
     * @param querys
     * @param remoteIp
     * @param uid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOptions(Map<String, String> querys, String remoteIp, Integer uid) {
        querys.forEach((k, v) -> insertOption(k, v));
        Logs logs = LogActions.logInstance(LogActions.SYS_SETTING, new Gson().toJson(querys), uid, remoteIp);
        logsMapper.insertSelective(logs);
        return true;
    }

    public void insertOption(String name, String value) {
        Options optionVo = new Options();
        optionVo.setName(name);
        optionVo.setValue(value);
        if (optionsMapper.selectByPrimaryKey(name) == null) {
            optionsMapper.insertSelective(optionVo);
        } else {
            optionsMapper.updateByPrimaryKeySelective(optionVo);
        }
    }
}
