package com.deservel.website.controller.api;

import com.deservel.website.common.bean.RestResponse;
import com.deservel.website.config.WebSiteConst;
import com.deservel.website.controller.AbstractBaseController;
import com.deservel.website.model.dto.MetaDto;
import com.deservel.website.model.dto.Types;
import com.deservel.website.model.po.Contents;
import com.deservel.website.model.po.Metas;
import com.deservel.website.service.ContentService;
import com.deservel.website.service.MetaService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DeserveL
 * @date 2017/12/7 23:55
 * @since 1.0.0
 */
@RestController
@RequestMapping("api")
public class CategoryApiController extends AbstractBaseController {

    @Autowired
    MetaService metaService;

    @Autowired
    ContentService contentService;

    /**
     * 分类条数
     *
     * @return
     */
    @GetMapping(value = "/categories")
    public RestResponse categories() {
        List<MetaDto> categories = metaService.getMetaWhitContentList(Types.CATEGORY, WebSiteConst.MAX_POSTS);
        return RestResponse.ok(categories);
    }

    /**
     * 某个分类详情
     */
    @GetMapping(value = "category/{keyword}")
    public RestResponse categories(@PathVariable String keyword, int limit) {
        return this.categories(keyword, 1, limit);
    }

    /**
     * 某个分类详情分页
     */
    @GetMapping(value = "category/{keyword}/{page}")
    public RestResponse categories(@PathVariable String keyword, @PathVariable int page, int limit) {
        page = page < 0 || page > WebSiteConst.MAX_PAGE ? 1 : page;
        Metas metaDto = metaService.getMeta(Types.CATEGORY, keyword);
        Map<String, Object> rs = new HashMap<>(2);
        rs.put("meta", metaDto);
        if (null != metaDto) {
            PageInfo<Contents> articles = contentService.getArticlesWithPage(metaDto.getMid(), Types.PUBLISH, Types.ARTICLE, page, limit);
            rs.put("articles", articles);
        }
        return RestResponse.ok(rs);
    }

    /**
     * 标签条数
     *
     * @return
     */
    @GetMapping(value = "/tags")
    public RestResponse tags() {
        List<MetaDto> tags = metaService.getMetaWhitContentList(Types.TAG, WebSiteConst.MAX_POSTS);
        return RestResponse.ok(tags);
    }

    /**
     * 某个标签详情
     */
    @GetMapping(value = "tag/{keyword}")
    public RestResponse tags(@PathVariable String keyword, int limit) {
        return this.tags(keyword, 1, limit);
    }

    /**
     * 某个标签详情分页
     */
    @GetMapping(value = "tag/{keyword}/{page}")
    public RestResponse tags(@PathVariable String keyword, @PathVariable int page, int limit) {
        page = page < 0 || page > WebSiteConst.MAX_PAGE ? 1 : page;
        Metas metaDto = metaService.getMeta(Types.TAG, keyword);
        Map<String, Object> rs = new HashMap<>(2);
        rs.put("meta", metaDto);
        if (null != metaDto) {
            PageInfo<Contents> articles = contentService.getArticlesWithPage(metaDto.getMid(), Types.PUBLISH, Types.ARTICLE, page, limit);
            rs.put("articles", articles);
        }
        return RestResponse.ok(rs);
    }
}
