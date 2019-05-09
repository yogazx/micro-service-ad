package com.yoge.ad.service.common.advice;

import com.yoge.ad.service.common.annotation.IgnoreResponseAdvice;
import com.yoge.ad.service.common.enums.ResponseCode;
import com.yoge.ad.service.common.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/9
 */
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    /**
     * Whether this component supports the given controller method return type
     * and the selected {@code HttpMessageConverter} type.
     *
     * @param returnType    the return type
     * @param converterType the selected converter type
     * @return {@code true} if {@link #beforeBodyWrite} should be invoked;
     * {@code false} otherwise
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (returnType.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class) || returnType.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        return true;
    }

    /**
     * Invoked after an {@code HttpMessageConverter} is selected and just before
     * its write method is invoked.
     *
     * @param body                  the body to be written
     * @param returnType            the return type of the controller method
     * @param selectedContentType   the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request               the current request
     * @param response              the current response
     * @return the body that was passed in or a modified (possibly new) instance
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        CommonResponse<Object> commonResponse = new CommonResponse<>(ResponseCode.SUCCESS.getCode(), "");
        if (body == null)
            return response;
        else if (body instanceof CommonResponse) {
            commonResponse = (CommonResponse<Object>) body;
        } else {
            commonResponse.setData(body);
        }
        return commonResponse;
    }
    
}
