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

import com.deservel.website.common.bean.RestResponse;
import com.deservel.website.config.WebSiteTools;
import com.deservel.website.controller.AbstractBaseController;
import com.deservel.website.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.join;

/**
 * @author DeserveL
 * @date 2017/12/11 15:09
 * @since 1.0.0
 */
@Controller
@RequestMapping("/admin/setting")
public class SettingController extends AbstractBaseController {

    @Autowired
    OptionService optionService;

    /**
     * 系统设置
     */
    @GetMapping(value = "")
    public String setting() {
        try {
            Map<String, String> options = optionService.getOptions();
            if (options.get("site_record") == null) {
                options.put("site_record", "");
            }
            setRequestAttribute("options", options);
            return "admin/setting";
        } catch (Exception e) {
            return errorPage(e);
        }
    }

    /**
     * 保存系统设置
     *
     * @param site_theme
     * @return
     */
    @PostMapping(value = "")
    @ResponseBody
    public RestResponse saveSetting(@RequestParam(required = false) String site_theme) {
        Map<String, String[]> parameterMap = getRequest().getParameterMap();
        Map<String, String> querys = new HashMap<>();
        parameterMap.forEach((key, value) -> querys.put(key, join(value)));
        boolean result = optionService.saveOptions(querys, getRemoteIp(), getUserByRequest().getUid());
        if (!result) {
            return RestResponse.fail("保存设置失败");
        }
        Map<String, String> options = optionService.getOptions();
        //网站配置
        WebSiteTools.OPTIONS = options;
        return RestResponse.ok();
    }
}
