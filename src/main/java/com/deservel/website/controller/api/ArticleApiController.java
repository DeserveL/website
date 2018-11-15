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
import com.deservel.website.config.WebSiteConst;
import com.deservel.website.config.WebSiteTools;
import com.deservel.website.controller.AbstractBaseController;
import com.deservel.website.model.dto.ArchiveDto;
import com.deservel.website.model.dto.CommentDto;
import com.deservel.website.model.dto.Types;
import com.deservel.website.model.po.Comments;
import com.deservel.website.model.po.Contents;
import com.deservel.website.service.CommentService;
import com.deservel.website.service.ContentService;
import com.deservel.website.service.SiteService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.security.validator.ValidatorException;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DeserveL
 * @date 2017/12/14 11:07
 * @since 1.0.0
 */
@RestController
@RequestMapping("api")
@Slf4j
public class ArticleApiController extends AbstractBaseController {

    @Autowired
    ContentService contentService;
    @Autowired
    CommentService commentService;
    @Autowired
    SiteService siteService;

    /**
     * 文章数据
     *
     * @param cid
     * @return
     */
    @GetMapping("article/{cid}")
    public RestResponse article(@PathVariable(value = "cid") String cid) {
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
    }

    /**
     * 归档文章
     *
     * @return
     */
    @GetMapping("archives")
    public RestResponse archives() {
        List<ArchiveDto> archives = contentService.getArchives();
        return RestResponse.ok(archives);
    }

    /**
     * 搜索文章
     *
     * @return
     */
    @GetMapping("search/{keyword}")
    public RestResponse search(@PathVariable String keyword, int limit) {
        return search(keyword, 1, limit);
    }

    /**
     * 搜索文章
     *
     * @return
     */
    @GetMapping("search")
    public RestResponse searchS(String s, int limit) {
        return search(s, 1, limit);
    }

    /**
     * 搜索文章
     *
     * @return
     */
    @GetMapping("search/{keyword}/{page}")
    public RestResponse search(@PathVariable String keyword, @PathVariable int page, int limit) {
        page = page < 0 || page > WebSiteConst.MAX_PAGE ? 1 : page;
        PageInfo<Contents> articles = contentService.getArticlesWithPage(keyword, page, limit);
        Map<String, Object> rs = new HashMap<>(2);
        rs.put("articles", articles);
        return RestResponse.ok(rs);
    }

    @GetMapping("/comments")
    public RestResponse getCommentsByArticleId(@RequestParam(value = "cid") Integer cid,
                                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                                               @RequestParam(value = "limit", defaultValue = "12") Integer limit) {
        page = page < 0 || page > WebSiteConst.MAX_PAGE ? 1 : page;
        PageInfo<CommentDto> pageInfo = commentService.getArticleComments(cid, page, limit);
        return RestResponse.ok(pageInfo);
    }

    /**
     * 评论操作
     */
    @PostMapping("/saveComment")
    public RestResponse<?> comment(@RequestBody Comments comments) {
        HttpServletRequest request = getRequest();
        String val = request.getRequestURL().toString() + ":" + comments.getCid();
        Integer count = cache.hget(Types.COMMENTS_FREQUENCY, val);
        if (null != count && count > 0) {
            return RestResponse.fail("您发表评论太快了，请过会再试");
        }
        comments.setIp(getRemoteIp());
        comments.setAgent(request.getHeader("User-Agent"));
        if (!WebSiteConst.OPTION_ALLOW_COMMENT_AUDIT) {
            comments.setStatus(WebSiteConst.COMMENT_NO_AUDIT);
        } else {
            comments.setStatus(WebSiteConst.COMMENT_APPROVED);
        }
        try {
            commentService.saveComment(comments);
            addCookie("tale_remember_author", URLEncoder.encode(comments.getAuthor(), "UTF-8"));
            addCookie("tale_remember_mail", URLEncoder.encode(comments.getMail(), "UTF-8"));
            if (StringUtils.isNotBlank(comments.getUrl())) {
                addCookie("tale_remember_url", URLEncoder.encode(comments.getUrl(), "UTF-8"));
            }
            // 设置对每个文章30秒可以评论一次
            cache.hset(Types.COMMENTS_FREQUENCY, val, 1, 60);
            System.out.println((Integer) cache.hget(Types.COMMENTS_FREQUENCY, val));
            siteService.cleanStatisticsCache();
            return RestResponse.ok();
        } catch (Exception e) {
            String msg = "评论发布失败";
            if (e instanceof ValidatorException) {
                msg = e.getMessage();
            } else {
                log.error(msg, e);
            }
            return RestResponse.fail(msg);
        }
    }

}
