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
package com.deservel.website.controller.api;

import com.deservel.website.common.bean.RestResponse;
import com.deservel.website.config.WebSiteTools;
import com.deservel.website.controller.AbstractBaseController;
import com.deservel.website.model.dto.Types;
import com.deservel.website.model.po.Contents;
import com.deservel.website.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DeserveL
 * @date 2017/12/14 11:07
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/article")
public class ArticleApiController extends AbstractBaseController{

    @Autowired
    ContentService contentService;

    /**
     * 文章数据
     *
     * @param cid
     * @return
     */
    @GetMapping("{cid}")
    public RestResponse article(@PathVariable(value = "cid") String cid){
        Contents contents = contentService.getContents(cid);
        //文章内容处理
        contents.setContent(WebSiteTools.article(contents.getContent()));
        //下一篇
        Contents nextContent = contentService.getNhContent(Types.NEXT, contents.getCreated());
        //前一篇
        Contents prevContent = contentService.getNhContent(Types.PREV, contents.getCreated());

        Map<String, Contents> rs = new HashMap<>(3);
        rs.put("content", contents);
        rs.put("nextContent", nextContent);
        rs.put("prevContent", prevContent);
        return RestResponse.ok(rs);
        // ceshi
    }
}
