package com.yoge.ad.service.common.advice;

import com.yoge.ad.service.common.enums.ResponseCode;
import com.yoge.ad.service.common.exception.AdException;
import com.yoge.ad.service.common.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/9
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = {AdException.class})
    public CommonResponse handleAdException(HttpServletRequest request, AdException ex) {
        CommonResponse<Object> commonResponse = new CommonResponse<>(ResponseCode.ERROR.getCode(), "business error");
        commonResponse.setData(ex.getMessage());
        log.error(request.getRequestURI() + " error, " + ex);
        return commonResponse;
    }
}
