package com.deservel.website.controller.admin;

import com.deservel.website.common.bean.RestResponse;
import com.deservel.website.config.WebSiteConst;
import com.deservel.website.controller.AbstractBaseController;
import com.deservel.website.model.dto.MetaDto;
import com.deservel.website.model.dto.Types;
import com.deservel.website.service.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author DeserveL
 * @date 2017/12/7 23:55
 * @since 1.0.0
 */
@Controller
@RequestMapping("admin/category")
public class CategoryController extends AbstractBaseController {

    @Autowired
    MetaService metaService;

    /**
     * 分类，标签管理页面
     *
     * @return
     */
    @GetMapping(value = "")
    public String index() {
        try {
            List<MetaDto> categories = metaService.getMetaWhitContentList(Types.CATEGORY, WebSiteConst.MAX_POSTS);
            List<MetaDto> tags = metaService.getMetaWhitContentList(Types.TAG, WebSiteConst.MAX_POSTS);
            setRequestAttribute("categories", categories);
            setRequestAttribute("tags", tags);
            return "admin/category";
        } catch (Exception e) {
            return errorPage(e);
        }
    }

    /**
     * 保存或修改分类
     *
     * @param cname
     * @param mid
     * @return
     */
    @PostMapping(value = "save")
    @ResponseBody
    public RestResponse saveCategory(@RequestParam String cname, @RequestParam Integer mid) {
        boolean result = metaService.saveMeta(Types.CATEGORY, cname, mid);
        if(!result){
            return RestResponse.fail("保存失败");
        }
        return RestResponse.ok();
    }

    /**
     * 删除分类
     *
     * @param mid
     * @return
     */
    @PostMapping(value = "delete")
    @ResponseBody
    public RestResponse deleteCategory(@RequestParam Integer mid) {
        boolean result = metaService.deleteMeta(mid);
        if(!result){
            return RestResponse.fail("删除失败");
        }
        return RestResponse.ok();
    }
}
