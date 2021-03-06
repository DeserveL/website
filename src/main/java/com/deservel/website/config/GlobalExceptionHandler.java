package com.deservel.website.config;

import com.deservel.website.common.bean.RestResponse;
import com.deservel.website.common.exception.TipRestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理类
 *
 * @author DeserveL
 * @date 2017/12/1 17:32
 * @since 1.0.0
 */
@Controller
@ControllerAdvice
public class GlobalExceptionHandler implements ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final static String ERROR_PATH = "/error";

    @ExceptionHandler(value = TipRestException.class)
    @ResponseBody
    public RestResponse tipRestException(TipRestException e) {
        logger.error("find exception:e={}", e.getMessage());
        return RestResponse.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public RestResponse exception(Exception e) {
        logger.error("find exception:", e);
        return RestResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    /**
     * Supports the HTML Error View
     *
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public String errorHtml(HttpServletRequest request) {
        return "comm/error_404";
    }

    /**
     * Supports other formats like JSON, XML
     *
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public RestResponse error(HttpServletRequest request) {
        return RestResponse.fail(500, "未知");
    }

    /**
     * Returns the path of the error page.
     *
     * @return the error path
     */
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
