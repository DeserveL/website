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

import com.deservel.website.model.po.Attach;
import com.github.pagehelper.PageInfo;

/**
 * @author DeserveL
 * @date 2017/12/10 0010 下午 14:44
 * @since 1.0.0
 */
public interface AttachService {

    /**
     * 获取附件列表
     *
     * @param page
     * @param limit
     * @return
     */
    PageInfo<Attach> getAttachs(int page, int limit) ;

    /**
     * 保存路径到数据库
     *
     * @param fname
     * @param fkey
     * @param ftype
     * @param uid
     * @return
     */
    Attach save(String fname, String fkey, String ftype, Integer uid);

    /**
     * 删除附件
     *
     * @param id
     * @param uid
     * @param remoteIp
     * @return
     */
    boolean deleteById(Integer id, Integer uid, String remoteIp);
}
