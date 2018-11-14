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

import com.deservel.website.common.utils.AesUtils;
import com.deservel.website.common.utils.CookieUtils;
import com.deservel.website.common.utils.HtmlUtils;
import com.deservel.website.model.po.Contents;
import com.deservel.website.model.po.Users;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网站公用方法类
 *
 * @author DeserveL
 * @date 2017/12/1 13:58
 * @since 1.0.0
 */
public class WebSiteTools {
    /**
     * 整个网站配置项
     */
    public static Map<String, String> OPTIONS = new HashMap<>();

    private static final Pattern SLUG_REGEX = Pattern.compile("^[A-Za-z0-9_-]{5,100}$", Pattern.CASE_INSENSITIVE);

    /**
     *图片路径 http://localhost:8081
     * public static final String IMAGE_URL = "http://www.deservel.top";
     */
    public static final String IMAGE_URL = "http://www.deservel.top";

    /**
     * 返回当前登录用户
     *
     * @return
     */
    public static Users getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (null == session) {
            return null;
        }
        return (Users) session.getAttribute(WebSiteConst.LOGIN_SESSION_KEY);
    }

    /**
     * 当前登录用户存入session
     *
     * @return
     */
    public static void setLoginUser(HttpServletRequest request, Users users) {
        HttpSession session = request.getSession();
        session.setAttribute(WebSiteConst.LOGIN_SESSION_KEY, users);
    }

    /**
     * 当前登录用户删除session
     *
     * @return
     */
    public static void removeLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(WebSiteConst.LOGIN_SESSION_KEY);
    }

    /**
     * 获取cookie中的用户id
     *
     * @param request
     * @return
     */
    public static Integer getCookieUid(HttpServletRequest request) {
        if (null != request) {
            Cookie cookie = CookieUtils.cookieRaw(WebSiteConst.USER_IN_COOKIE, request);
            if (cookie != null && cookie.getValue() != null) {
                try {
                    String uid = AesUtils.deAes(cookie.getValue(), WebSiteConst.AES_SALT);
                    return StringUtils.isNotBlank(uid) && StringUtils.isNumeric(uid) ? Integer.valueOf(uid) : null;
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 设置记住密码cookie
     *
     * @param response
     * @param uid
     */
    public static void setCookieUid(HttpServletResponse response, Integer uid) throws Exception {
        String val = AesUtils.enAes(uid.toString(), WebSiteConst.AES_SALT);
        CookieUtils.setCookie(response, WebSiteConst.USER_IN_COOKIE, val);
    }

    /**
     * 删除cookie登录信息
     *
     * @param response
     */
    public static void removeCookieUid(HttpServletResponse response) {
        CookieUtils.removeCookie(response, WebSiteConst.USER_IN_COOKIE);
    }

    /**
     * 判断是否是合法路径
     *
     * @param slug
     * @return
     */
    public static boolean isPath(String slug) {
        if (StringUtils.isNotBlank(slug)) {
            if (slug.contains("/") || slug.contains(" ") || slug.contains(".")) {
                return false;
            }
            Matcher matcher = SLUG_REGEX.matcher(slug);
            return matcher.find();
        }
        return false;
    }

    /**
     * 生成文件路径
     *
     * @param name
     * @return
     */
    public static String getFileKey(String path, String name) {
        String prefix = "/upload/" + DateFormatUtils.format(new Date(), "yyyy/MM");
        if (!new File(path + prefix).exists()) {
            new File(path + prefix).mkdirs();
        }

        name = StringUtils.trimToNull(name);
        if (name == null) {
            return prefix + "/" + UUID.randomUUID().toString() + "." + null;
        } else {
            name = name.replace('\\', '/');
            name = name.substring(name.lastIndexOf("/") + 1);
            int index = name.lastIndexOf(".");
            String ext = null;
            if (index >= 0) {
                ext = StringUtils.trimToNull(name.substring(index + 1));
            }
            return prefix + "/" + UUID.randomUUID().toString() + "." + (ext == null ? null : (ext));
        }
    }

    /**
     * 判断文件是否是图片类型
     *
     * @param imageFile
     * @return
     */
    public static boolean isImage(InputStream imageFile) {
        try {
            Image img = ImageIO.read(imageFile);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取保存文件的位置,jar所在目录的路径
     *
     * @return
     */
    public static String getUplodFilePath() {
        String path = WebSiteTools.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.substring(1, path.length());
        try {
            path = java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int lastIndex = path.lastIndexOf("/") + 1;
        path = path.substring(0, lastIndex);
        File file = new File("");
        return file.getAbsolutePath() + "/";
    }

    /**
     * 截取文章摘要
     *
     * @param value 文章内容
     * @param len   要截取文字的个数
     * @return
     */
    public static String intro(String value, int len) {
        int pos = value.indexOf("<!--more-->");
        if (pos != -1) {
            String html = value.substring(0, pos);
            return HtmlUtils.htmlToText(HtmlUtils.mdToHtml(html));
        } else {
            String text = HtmlUtils.htmlToText(HtmlUtils.mdToHtml(value));
            if (text.length() > len) {
                return text.substring(0, len);
            }
            return text;
        }
    }

    /**
     * 显示文章缩略图，顺序为：文章第一张图 -> 随机获取
     *
     * @return
     */
    public static String show_thumb(Contents contents) {
        if (null == contents) {
            return "";
        }
        if (StringUtils.isNotBlank(contents.getThumbImg())) {
            // 图片url地址，数据库取; IMAGE_URL
            return ThymeleafCommons.site_url() + contents.getThumbImg();
        }
        String content = article(contents.getContent());
        String img = HtmlUtils.show_thumb(content);
        if (StringUtils.isNotBlank(img)) {
            return img;
        }
        int cid  = contents.getCid();
        int size = cid % 20;
        size = size == 0 ? 1 : size;
        return "static/image/rand/" + size + ".jpg";
    }

    /**
     * 显示文章内容，转换markdown为html
     *
     * @param value
     * @return
     */
    public static String article(String value) {
        if (StringUtils.isNotBlank(value)) {
            value = value.replace("<!--more-->", "\r\n");
            return HtmlUtils.mdToHtml(value);
        }
        return "";
    }
}
