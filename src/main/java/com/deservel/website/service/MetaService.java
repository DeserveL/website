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

import com.deservel.website.model.dto.MetaDto;
import com.deservel.website.model.po.Metas;

import java.util.List;

/**
 * @author DeserveL
 * @date 2017/12/6 0006 下午 20:36
 * @since 1.0.0
 */
public interface MetaService {

    /**
     * 根据类别获取
     *
     * @param type
     * @return
     */
    List<Metas> getMetas(String type);

    /**
     * 保存多个项目
     *
     * @param cid
     * @param names
     * @param type
     */
    void saveMetas(Integer cid, String names, String type);

    /**
     * 根据类型查询项目列表，带项目下面的文章数
     *
     * @param type
     * @param maxPosts
     * @return
     */
    List<MetaDto> getMetaWhitContentList(String type, Integer maxPosts);

    /**
     * 保存或修改分类
     *
     * @param type
     * @param name
     * @param mid
     * @return
     */
    boolean saveMeta(String type, String name, Integer mid);

    /**
     * 删除分类
     *
     * @param mid
     * @return
     */
    boolean deleteMeta(Integer mid);
}
