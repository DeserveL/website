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
import com.deservel.website.config.ThymeleafCommons;
import com.deservel.website.config.WebSiteConst;
import com.deservel.website.config.WebSiteTools;
import com.deservel.website.controller.AbstractBaseController;
import com.deservel.website.model.dto.Types;
import com.deservel.website.model.po.Attach;
import com.deservel.website.model.po.Users;
import com.deservel.website.service.AttachService;
import com.deservel.website.service.SiteService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 附件管理
 *
 * @author DeserveL
 * @date 2017/12/10 0010 下午 14:41
 * @since 1.0.0
 */
@Controller
@RequestMapping("admin/attach")
public class AttachController extends AbstractBaseController {

    @Autowired
    AttachService attachService;
    @Autowired
    SiteService siteService;

    /**
     * 附件页面
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "12") int limit) {
        try {
            PageInfo<Attach> attachPaginator = attachService.getAttachs(page, limit);
            setRequestAttribute("attachs", attachPaginator);
            setRequestAttribute(Types.ATTACH_URL, ThymeleafCommons.site_option(Types.ATTACH_URL, ThymeleafCommons.site_url()));
            setRequestAttribute("max_file_size", WebSiteConst.MAX_FILE_SIZE / 1024);
            return "admin/attach";
        } catch (Exception e) {
            return errorPage(e);
        }
    }

    /**
     * 上传文件接口（支持同时多上传）
     *
     * @return
     */
    @PostMapping(value = "upload")
    @ResponseBody
    public RestResponse upload(MultipartHttpServletRequest request) throws IOException {
        //用户id
        Integer uid = getUserByRequest().getUid();
        //上传错误文件用
        List<Attach> errorFiles = new ArrayList<>();
        //正确上传的连接
        List<Attach> urls = new ArrayList<>();
        //获取所有上传文件
        Iterator<String> itr = request.getFileNames();
        while (itr.hasNext()) {
            String uploadedFile = itr.next();

            MultipartFile multipartFile = request.getFile(uploadedFile);
            String fname = multipartFile.getOriginalFilename();
            if (multipartFile.getSize() / 1024 <= WebSiteConst.MAX_FILE_SIZE) {
                String fkey = WebSiteTools.getFileKey(WebSiteTools.getUplodFilePath(), fname);
                String ftype = WebSiteTools.isImage(multipartFile.getInputStream()) ? Types.IMAGE : Types.FILE;
                File file = new File(WebSiteTools.getUplodFilePath() + fkey);
                //写文件
                FileCopyUtils.copy(multipartFile.getInputStream(), new FileOutputStream(file));

                Attach save = attachService.save(fname, fkey, ftype, uid);
                //清楚缓存
                siteService.cleanStatisticsCache();
                urls.add(save);
            } else {
                Attach attach = new Attach();
                attach.setFname(fname);
                errorFiles.add(attach);
            }
        }
        if (errorFiles.size() > 0) {
            return new RestResponse(false, errorFiles);
        }
        return RestResponse.ok(urls);
    }

    /**
     * 删除附件
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public RestResponse delete(@RequestParam Integer id) {
        boolean result = attachService.deleteById(id, getUserByRequest().getUid(), getRemoteIp());
        if (result) {
            return RestResponse.fail("附件删除失败");
        }
        return RestResponse.ok();
    }
}
