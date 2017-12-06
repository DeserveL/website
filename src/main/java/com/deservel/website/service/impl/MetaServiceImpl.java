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

import com.deservel.website.dao.MetasMapper;
import com.deservel.website.model.po.Metas;
import com.deservel.website.service.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author DeserveL
 * @date 2017/12/6 0006 下午 20:40
 * @since 1.0.0
 */
@Service
public class MetaServiceImpl implements MetaService {

    @Autowired
    MetasMapper metasMapper;

    /**
     * 根据类别获取
     *
     * @param type
     * @return
     */
    @Override
    public List<Metas> getMetas(String type) {
        List<Metas> metaList = metasMapper.selectByType(type);
        return metaList;
    }
}
