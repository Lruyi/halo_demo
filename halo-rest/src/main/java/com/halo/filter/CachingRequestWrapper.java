package com.halo.filter;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @Author: liuruyi
 * @Date: 2026/1/16 14:59
 */
public class CachingRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 设置修改后的请求体内容
     */
    private String body;

    /**
     * 获取请求体内容
     */
    public String getBody() {
        return body;
    }

    /**
     * 设置修改后的请求体内容
     */
    public void setBody(String body) {
        this.body = body;
    }

    public CachingRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        // 读取原始请求体内容
        body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return bais.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // 不做处理
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }
}
