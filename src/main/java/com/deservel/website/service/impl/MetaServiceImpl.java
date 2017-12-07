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
import com.deservel.website.dao.MetasMapper;
import com.deservel.website.dao.RelationshipsMapper;
import com.deservel.website.model.po.Metas;
import com.deservel.website.model.po.Relationships;
import com.deservel.website.service.MetaService;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    RelationshipsMapper relationshipsMapper;

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

    /**
     * 保存多个项目
     *
     * @param cid   文章id
     * @param names 类型名称列表
     * @param type  类型，tag or category
     */
    @Override
    public void saveMetas(Integer cid, String names, String type) {
        if (null == cid) {
            throw new TipRestException(ExceptionType.MTEA_CID_BLANK);
        }
        if (StringUtils.isNotBlank(names) && StringUtils.isNotBlank(type)) {
            String[] nameArr = names.split(",");
            for (String name : nameArr) {
                this.saveOrUpdate(cid, name, type);
            }
        }
    }

    /**
     * 标签 依赖关系
     *
     * @param cid
     * @param name
     * @param type
     */
    private void saveOrUpdate(Integer cid, String name, String type) {
        Metas metaCondition = new Metas();
        metaCondition.setName(name);
        metaCondition.setType(type);
        List<Metas> select = metasMapper.select(metaCondition);
        //对应关系 用
        int mid;
        if (select != null && select.size() == 1) {
            mid = select.get(0).getMid();
            //存入标签
        } else if (select != null && select.size() == 0) {
            Metas metas = new Metas();
            metas.setSlug(name);
            metas.setName(name);
            metas.setType(type);
            metasMapper.insertSelective(metas);
            mid = metas.getMid();
        } else {
            return;
        }
        //依赖关系
        if (mid != 0) {
            Relationships relationshipsCond = new Relationships();
            relationshipsCond.setCid(cid);
            relationshipsCond.setMid(mid);
            int count = relationshipsMapper.selectCount(relationshipsCond);
            if (count == 0) {
                relationshipsMapper.insert(relationshipsCond);
            }

        }
    }
}
