package com.deservel.website.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * 向mvc中添加自定义组件
 *
 * @author DeserveL
 * @date 2017/12/1 18:04
 * @since 1.0.0
 */
@Component
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private BaseInterceptor baseInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(baseInterceptor);
    }
}
