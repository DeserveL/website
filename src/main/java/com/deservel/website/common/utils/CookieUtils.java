package com.deservel.website.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie 工具类
 *
 * @author DeserveL
 * @date 2017/12/1 14:02
 * @since 1.0.0
 */
public interface CookieUtils {

    /**
     * 从cookies中获取指定cookie
     *
     * @param name    名称
     * @param request 请求
     * @return cookie
     */
    static Cookie cookieRaw(String name, HttpServletRequest request) {
        javax.servlet.http.Cookie[] servletCookies = request.getCookies();
        if (servletCookies == null) {
            return null;
        }
        for (javax.servlet.http.Cookie c : servletCookies) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * 设置cookie 带默认值
     *
     * @param response response
     * @param key key
     * @param val val
     */
    static void setCookie(HttpServletResponse response, String key, String val) {
        setCookie(response,key,val,"/",60*30,false);
    }

    /**
     * 设置cookie
     *
     * @param response response
     * @param key key
     * @param val val
     * @param uri 路径
     * @param expiry 过期时间
     * @param flag Secure
     */
    static void setCookie(HttpServletResponse response, String key, String val,String uri, int expiry, boolean flag) {
        Cookie cookie = new Cookie(key, val);
        cookie.setPath(uri);
        cookie.setMaxAge(expiry);
        cookie.setSecure(flag);
        response.addCookie(cookie);
    }

    /**
     * 删除cookie
     *
     * @param response response
     * @param key key
     */
    static void removeCookie(HttpServletResponse response, String key) {
        setCookie(response,key,"","/",0,false);
    }
}
