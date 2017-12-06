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
import com.deservel.website.common.exception.TipPageException;
import com.deservel.website.dao.ContentsMapper;
import com.deservel.website.model.dto.Types;
import com.deservel.website.model.po.Contents;
import com.deservel.website.service.ContentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * @author DeserveL
 * @date 2017/12/6 0006 下午 22:55
 * @since 1.0.0
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    ContentsMapper contentsMapper;

    /**
     * 获取文章列表
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageInfo<Contents> getArticlesWithPage(Integer page, Integer limit) {
        Condition condition = new Condition(Contents.class);
        condition.setOrderByClause("created desc");
        condition.createCriteria().andEqualTo("type", Types.ARTICLE);
        PageHelper.startPage(page, limit);
        List<Contents> contents = contentsMapper.selectByCondition(condition);
        return new PageInfo<>(contents);
    }

    /**
     * 获取文章详情
     *
     * @param cid
     * @return
     */
    @Override
    public Contents getContents(String cid) {
        if (StringUtils.isNotBlank(cid)) {
            if (StringUtils.isNumeric(cid)) {
                Contents contents = contentsMapper.selectByPrimaryKey(Integer.valueOf(cid));
                return contents;
            } else {
                Condition condition = new Condition(Contents.class);
                condition.createCriteria().andEqualTo("slug",cid);
                List<Contents> contents = contentsMapper.selectByCondition(condition);
                if(contents.size() != 1){
                    throw new TipPageException(ExceptionType.ID_NOT_ONE);
                }
                return contents.get(0);
            }
        }
        return null;
    }
}
