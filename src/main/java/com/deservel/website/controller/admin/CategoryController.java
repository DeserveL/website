package com.deservel.website.controller.admin;

import com.deservel.website.config.WebSiteConst;
import com.deservel.website.controller.AbstractBaseController;
import com.deservel.website.model.dto.MetaDto;
import com.deservel.website.model.dto.Types;
import com.deservel.website.service.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping(value = "")
    public String index() {
        List<MetaDto> categories = metaService.getMetaWhitContentList(Types.CATEGORY, WebSiteConst.MAX_POSTS);
        List<MetaDto> tags = metaService.getMetaWhitContentList(Types.TAG, WebSiteConst.MAX_POSTS);
        setRequestAttribute("categories", categories);
        setRequestAttribute("tags", tags);
        return "admin/category";
    }

}
