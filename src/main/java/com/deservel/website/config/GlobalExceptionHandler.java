package com.deservel.website.config;

import com.deservel.website.common.bean.RestResponse;
import com.deservel.website.common.exception.TipPageException;
import com.deservel.website.common.exception.TipRestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理类
 *
 * @author DeserveL
 * @date 2017/12/1 17:32
 * @since 1.0.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = TipPageException.class)
    public String tipPageException(TipPageException e, HttpServletRequest request) {
        logger.error("find exception:e={}", e.getMessage());
        request.setAttribute("code", e.getCode());
        request.setAttribute("message", e.getMessage());
        return "comm/error_500";
    }

    @ExceptionHandler(value = TipRestException.class)
    @ResponseBody
    public RestResponse tipRestException(TipRestException e) {
        logger.error("find exception:e={}", e.getMessage());
        return RestResponse.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public String exception(Exception e) {
        logger.error("find exception:e={}", e.getMessage());
        return "comm/error_404";
    }
}
