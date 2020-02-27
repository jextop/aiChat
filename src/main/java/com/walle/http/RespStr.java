package com.walle.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class RespStr implements ResponseHandler<String> {
    @Override
    public String handleResponse(HttpResponse httpResponse) throws IOException {
        HttpEntity entity = httpResponse.getEntity();
        if (entity == null) {
            throw new ClientProtocolException("Response contains no content");
        }

        // 读取返回内容
        ContentType contentType = ContentType.getOrDefault(entity);
        Charset charset = contentType.getCharset();
        return EntityUtils.toString(entity, charset == null ? Charset.forName("utf-8") : charset);
    }
}
