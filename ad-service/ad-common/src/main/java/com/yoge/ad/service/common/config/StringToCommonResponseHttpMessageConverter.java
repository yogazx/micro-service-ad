package com.yoge.ad.service.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoge.ad.service.common.vo.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * DESC 自定义对string类型的返回值，解决spring自带的StringHttpMessageConverter转换后相应头里Content-Type是text/plain
 *
 * @author You Jia Ge
 * Created Time 2019/5/10
 */
public class StringToCommonResponseHttpMessageConverter extends AbstractHttpMessageConverter<String> {

    public static final Charset DEFAULT_CHARSET =  Charset.forName("utf-8");
    private ObjectMapper objectMapper;

    public StringToCommonResponseHttpMessageConverter() {
        this(DEFAULT_CHARSET);
    }
    public StringToCommonResponseHttpMessageConverter(Charset charset) {
        // 设置 application/json;utf-8 的响应头
        super(charset, MediaType.APPLICATION_JSON_UTF8, MediaType.ALL);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return String.class == clazz;
    }

    @Override
    protected String readInternal(Class<? extends String> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType());
        return StreamUtils.copyToString(inputMessage.getBody(), charset);
    }

    @Override
    protected void writeInternal(String s, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        Charset charset = getContentTypeCharset(outputMessage.getHeaders().getContentType());
        // 重点只在这一句，把字符串使用 Result 包装
        CommonResponse<String> commonResponse = new CommonResponse<>();
        commonResponse.setData(s);
        StreamUtils.copy(objectMapper.writeValueAsString(commonResponse), charset, outputMessage.getBody());
    }

    private Charset getContentTypeCharset(MediaType contentType) {
        if (contentType != null && contentType.getCharset() != null) {
            return contentType.getCharset();
        } else {
            return getDefaultCharset();
        }
    }

    @Autowired
    public StringToCommonResponseHttpMessageConverter setMapper(ObjectMapper mapper) {
        this.objectMapper = mapper;
        return this;
    }

}
