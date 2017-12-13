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
import com.deservel.website.model.dto.IndexDto;
import com.deservel.website.model.dto.Types;
import com.deservel.website.model.po.Comments;
import com.deservel.website.model.po.Contents;
import com.deservel.website.service.ContentService;
import com.deservel.website.service.OptionService;
import com.deservel.website.service.SiteService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author DeserveL
 * @date 2017/12/12 19:01
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/index")
public class IndexApiController extends AbstractBaseController{

    @Autowired
    SiteService siteService;
    @Autowired
    OptionService optionService;
    @Autowired
    ContentService contentService;

    /**
     * 首页数据
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("")
    public RestResponse index(@RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "limit", defaultValue = "12") Integer limit){
        PageInfo<Contents> articlesWithPage = contentService.getArticlesWithPage(Types.ARTICLE, Types.PUBLISH, page, limit);
        List<Contents> contentlist = articlesWithPage.getList();
        //把文章内容替换为文章摘要
        contentlist.forEach(c -> c.setContent(WebSiteTools.intro(c.getContent(),75)));
        return RestResponse.ok(articlesWithPage);
    }

    @GetMapping("/foot")
    public RestResponse foot(){
        List<Comments> comments = siteService.recentComments(8);
        List<Contents> contents = siteService.recentContents(Types.RECENT_ARTICLE, 8);
        Map<String, String> options = optionService.getOptions();
        IndexDto indexDto = new IndexDto();
        indexDto.setRecentComments(comments);
        indexDto.setRecentArticles(contents);
        indexDto.setOptions(options);
        return RestResponse.ok(indexDto);
    }
}
