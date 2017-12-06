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
package com.deservel.website.controller.admin;

import com.deservel.website.controller.AbstractBaseController;
import com.deservel.website.model.dto.Types;
import com.deservel.website.model.po.Contents;
import com.deservel.website.model.po.Metas;
import com.deservel.website.service.ContentService;
import com.deservel.website.service.MetaService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文章操作页面
 *
 * @author DeserveL
 * @date 2017/12/6 0006 下午 20:33
 * @since 1.0.0
 */
@Controller
@RequestMapping("/admin/article")
public class ArticleController extends AbstractBaseController{

    @Autowired
    ContentService contentService;

    @Autowired
    MetaService metaService;

    /**
     * 文章页面
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "")
    public String index(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "limit", defaultValue = "15") Integer limit) {
        PageInfo<Contents> contentsPaginator = contentService.getArticlesWithPage(page, limit);
        setRequestAttribute("articles", contentsPaginator);
        return "admin/article_list";
    }

    /**
     * 文章发布页面
     *
     * @return
     */
    @GetMapping(value = "/publish")
    public String newArticle() {
        //文章分类
        List<Metas> categories = metaService.getMetas(Types.CATEGORY);
        setRequestAttribute("categories", categories);
        return "admin/article_edit";
    }

    /**
     * 文章编辑页面
     *
     * @param cid
     * @return
     */
    @GetMapping(value = "/{cid}")
    public String editArticle(@PathVariable String cid) {
        Contents contents = contentService.getContents(cid);
        setRequestAttribute("contents", contents);
        List<Metas> categories = metaService.getMetas(Types.CATEGORY);
        setRequestAttribute("categories", categories);
        setRequestAttribute("active", "article");
        return "admin/article_edit";
    }
}
