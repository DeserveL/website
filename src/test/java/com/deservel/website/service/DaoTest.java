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

import com.deservel.website.AbstractSpringBootTest;
import com.deservel.website.dao.ContentsMapper;
import com.deservel.website.dao.LogsMapper;
import com.deservel.website.dao.MetasMapper;
import com.deservel.website.model.dto.Types;
import com.deservel.website.model.po.Contents;
import com.deservel.website.model.po.Logs;
import com.deservel.website.model.po.Metas;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author DeserveL
 * @date 2017/11/30 14:38
 * @since 1.0.0
 */
public class DaoTest extends AbstractSpringBootTest{

    @Autowired
    LogsMapper logsMapper;
    @Autowired
    MetasMapper metasMapper;
    @Autowired
    ContentsMapper contentsMapper;

    @Test
    public void logs(){
        List<Logs> logs = logsMapper.selectAll();
    }

    @Test
    public void metas(){
        List<Metas> metas = metasMapper.selectByType(Types.CATEGORY);
        System.out.println(metas);
    }

    @Test
    public void contents(){
        List<Contents> contents = contentsMapper.selectRandomArticle(Types.ARTICLE, Types.PUBLISH, 10);
        System.out.println(contents);
    }
}
