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

import com.deservel.website.common.cache.MapCache;
import com.deservel.website.config.WebSiteConst;
import com.deservel.website.dao.AttachMapper;
import com.deservel.website.dao.CommentsMapper;
import com.deservel.website.dao.ContentsMapper;
import com.deservel.website.dao.MetasMapper;
import com.deservel.website.model.dto.Statistics;
import com.deservel.website.model.dto.Types;
import com.deservel.website.model.po.Attach;
import com.deservel.website.model.po.Comments;
import com.deservel.website.model.po.Contents;
import com.deservel.website.model.po.Metas;
import com.deservel.website.service.SiteService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DeserveL
 * @date 2017/11/30 17:07
 * @since 1.0.0
 */
@Service
public class SiteServiceImpl implements SiteService {

    protected MapCache cache = MapCache.single();

    @Autowired
    CommentsMapper commentsMapper;
    @Autowired
    ContentsMapper contentsMapper;
    @Autowired
    AttachMapper attachMapper;
    @Autowired
    MetasMapper metasMapper;


    /**
     * 最新收到的评论
     *
     * @param limit
     * @return
     */
    @Override
    public List<Comments> recentComments(Integer limit) {
        //创建日期倒序
        Condition condition = new Condition(Comments.class);
        condition.setOrderByClause("created desc");
        //分页
        PageHelper.startPage(1, limit);
        List<Comments> comments = commentsMapper.selectByCondition(condition);
        return comments;
    }

    /**
     * 最新或者随机发表的文章
     *
     *
     * @param type
     * @param limit
     * @return
     */
    @Override
    public List<Contents> recentContents(String type, Integer limit) {

        // 最新文章
        if (Types.RECENT_ARTICLE.equals(type)) {
            //分页
            PageHelper.startPage(1, limit);
            //创建日期倒叙
            Condition condition = new Condition(Contents.class);
            condition.setOrderByClause("created desc");
            condition.createCriteria().andEqualTo("status", Types.PUBLISH).andEqualTo("type", Types.ARTICLE);
            List<Contents> contents = contentsMapper.selectByCondition(condition);
            return contents;
        }

        // 随机文章
        if (Types.RANDOM_ARTICLE.equals(type)) {
            List<Contents> contents = contentsMapper.selectRandomArticle(type, Types.PUBLISH, limit);
            return contents;
        }
        return new ArrayList<>();
    }

    /**
     * 获取后台统计数据
     *
     * @return
     */
    @Override
    public Statistics getStatistics() {
        Statistics statistics = null;
        if (cache.get(Types.C_STATISTICS) instanceof Statistics) {
            statistics = cache.get(Types.C_STATISTICS);
        }
        // 是否存在缓存数据
        if (null != statistics) {
            return statistics;
        }

        long articles = contentsMapper.countContentsByTypeAndStatus(Types.ARTICLE, Types.PUBLISH);
        long pages = contentsMapper.countContentsByTypeAndStatus(Types.PAGE, Types.PUBLISH);
        long comments = commentsMapper.selectCount(new Comments());
        long attachs = attachMapper.selectCount(new Attach());
        Metas metas = new Metas();
        metas.setType(Types.TAG);
        long tags = metasMapper.selectCount(metas);
        metas.setType(Types.CATEGORY);
        long categories = metasMapper.selectCount(metas);
        metas.setType(Types.LINK);
        long links = metasMapper.selectCount(metas);

        statistics = new Statistics();
        statistics.setArticles(articles);
        statistics.setPages(pages);
        statistics.setComments(comments);
        statistics.setAttachs(attachs);
        statistics.setTags(tags);
        statistics.setCategories(categories);
        statistics.setLinks(links);

        //加入缓存
        cache.set(Types.C_STATISTICS, statistics, WebSiteConst.STATISTICS_TIME);
        return statistics;
    }

    /**
     * 清除后台统计数据缓存
     *
     */
    @Override
    public void cleanStatisticsCache(){
        cache.del(Types.C_STATISTICS);
    }


}
