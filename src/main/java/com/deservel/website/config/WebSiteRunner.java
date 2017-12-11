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
package com.deservel.website.config;

import com.deservel.website.service.OptionService;
import com.google.gson.Gson;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author DeserveL
 * @date 2017/12/11 17:13
 * @since 1.0.0
 */
@Component
public class WebSiteRunner implements CommandLineRunner {

    @Autowired
    OptionService optionService;

    protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) {
        WebSiteTools.OPTIONS = optionService.getOptions();
        if(logger.isInfoEnabled()){
            logger.info("项目启动了，配置如下：");
            logger.info(new Gson().toJson(WebSiteTools.OPTIONS));
        }
    }
}
