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

import com.deservel.website.controller.BaseController;
import com.deservel.website.model.dto.Statistics;
import com.deservel.website.model.po.Comments;
import com.deservel.website.model.po.Contents;
import com.deservel.website.model.po.Logs;
import com.deservel.website.service.LogService;
import com.deservel.website.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 后台管理首页
 *
 * @author DeserveL
 * @date 2017/11/30 16:39
 * @since 1.0.0
 */
@Controller
@RequestMapping("/admin")
public class IndexController extends BaseController{

    @Autowired
    SiteService siteService;
    @Autowired
    LogService logService;

    /**
     * 后台首页
     *
     * @return
     */
    @GetMapping(value = {"","/index"})
    public String index(HttpServletRequest request){
        List<Comments> comments = siteService.recentComments(5);
        List<Contents> contents = siteService.recentContents(5);
        Statistics statistics = siteService.getStatistics();
        // 取最新的20条日志
        List<Logs> logs = logService.getLogs(1, 5);

        request.setAttribute("comments", comments);
        request.setAttribute("articles", contents);
        request.setAttribute("statistics", statistics);
        request.setAttribute("logs", logs);
        return "admin/index";
    }
}
