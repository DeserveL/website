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
package com.deservel.website.controller;

import com.deservel.website.common.exception.TipPageException;
import com.deservel.website.common.utils.IpUtils;
import com.deservel.website.config.WebSiteTools;
import com.deservel.website.model.po.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 基础controller,各个web项目应该在此基础上扩展自己的基础controller.
 *
 * @author DeserveL
 * @date 2017/11/30 16:38
 * @since 1.0.0
 */
public abstract class AbstractBaseController {
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 存放当前线程的HttpServletRequest对象
     */
    private static ThreadLocal<HttpServletRequest> httpServletRequestThreadLocal = new ThreadLocal<>();

    /**
     * 存放当前线程的HttpServletResponse对象
     */
    private static ThreadLocal<HttpServletResponse> httpServletResponseThreadLocal = new ThreadLocal<>();

    /**
     * 存放当前线程的Model对象
     */
    private static ThreadLocal<Model> modelThreadLocal = new ThreadLocal<>();

    /**
     * 使用@ModelAttribute注解标识的方法会在每个控制器中的方法访问之前先调用
     *
     * @param request
     * @param model
     */
    @ModelAttribute
    protected void setThreadLocal(HttpServletRequest request, HttpServletResponse response, Model model) {
        httpServletRequestThreadLocal.set(request);
        httpServletResponseThreadLocal.set(response);
        modelThreadLocal.set(model);
    }

    /**
     * 获取当前线程的HttpServletRequest对象
     *
     * @return 当前线程的HttpServletRequest对象
     */
    protected HttpServletRequest getRequest() {
        return httpServletRequestThreadLocal.get();
    }

    /**
     * 获取当前线程的HttpServletResponse对象
     *
     * @return 当前线程的HHttpServletResponse对象
     */
    protected HttpServletResponse getResponse() {
        return httpServletResponseThreadLocal.get();
    }

    /**
     * 获取当前线程的HttpSession对象
     *
     * @return 当前线程的HttpSession对象
     */
    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 获取当前线程的Model对象
     *
     * @return 当前线程的Model对象
     */
    protected Model getModel() {
        return modelThreadLocal.get();
    }

    /**
     * 获取当前的ServletContext对象
     *
     * @return 当前的ServletContext对象
     */
    protected ServletContext getContext() {
        return getRequest().getServletContext();
    }


    /**
     * 向 Model 设置属性值
     *
     * @param name  属性名
     * @param value 属性值
     */
    protected void setModelAttribute(String name, Object value) {
        getModel().addAttribute(name, value);
    }

    /**
     * 向 HttpServletRequest 设置属性值
     *
     * @param name  属性名
     * @param value 属性值
     */
    protected void setRequestAttribute(String name, Object value) {
        HttpServletRequest request = this.getRequest();
        request.setAttribute(name, value);
    }

    /**
     * 向 HttpSession 设置属性值
     *
     * @param name  属性名
     * @param value 属性值
     */
    public void setSessionAttribute(String name, Object value) {
        HttpSession session = this.getSession();
        session.setAttribute(name, value);
    }

    /**
     * 从 HttpSession 中获取属性值
     *
     * @param name 属性名
     * @return 属性值
     */
    protected Object getSessionAttribute(String name) {
        HttpSession session = this.getSession();
        Object value = session.getAttribute(name);
        return value;
    }

    /**
     * 从 HttpServletRequest 中获取属性值
     *
     * @param name 属性名
     * @return 属性值
     */
    protected Object getRequestAttribute(String name) {
        HttpServletRequest request = this.getRequest();
        Object value = request.getAttribute(name);
        return value;
    }

    /**
     * 获取访问ip
     *
     * @return
     */
    protected String getRemoteIp() {
        return IpUtils.getIpAddrByRequest(getRequest());
    }

    /**
     * 获取请求绑定的登录对象
     *
     * @return
     */
    public Users getUserByRequest() {
        return WebSiteTools.getLoginUser(getRequest());
    }

    /**
     * 页面跳转异常
     *
     * @param e
     * @return
     */
    public String errorPage(Exception e){
        logger.error("页面获取失败",e);
        getRequest().setAttribute("code", 500);
        getRequest().setAttribute("message", e.getMessage());
        return "comm/error_500";
    }
}
