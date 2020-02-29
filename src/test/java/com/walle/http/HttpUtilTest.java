package com.walle.http;

import com.alibaba.fastjson.JSONObject;
import com.walle.util.LogUtil;
import com.walle.util.StrUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpUtilTest {
    @Test
    public void testHttpGet() {
        String html = HttpUtil.sendHttpGet("https://blog.51cto.com/13851865");
        String[] ret = StrUtil.parse(html, "<span>[1-9]\\d*</span>");
        LogUtil.info(ret);
        Assert.assertNotNull(ret);
    }

    @Test
    public void testHttpForm() {
        String url = "https://openapi.baidu.com/oauth/2.0/token";
        Map<String, String> headers = new HashMap<String, String>() {{
            put("Content-Type", "application/x-www-form-urlencoded");
        }};
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("grant_type", "client_credentials");
            put("client_id", "kVcnfD9iW2XVZSMaLMrtLYIz");
            put("client_secret", "O9o1O213UgG5LFn0bDGNtoRN3VWl2du6");
        }};

        JSONObject ret = HttpUtil.sendHttpForm(url, headers, params, new RespJsonObj());
        LogUtil.info(ret);
        Assert.assertNotNull(ret);

        String token = ret.getString("access_token");
        LogUtil.info(token);
        testBaiduTts(token);
    }

    public void testBaiduTts(final String token) {
        String url = "https://tsn.baidu.com/text2audio";
        Map<String, String> headers = new HashMap<String, String>() {{
            put("Content-Type", "application/x-www-form-urlencoded");
        }};
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("tex", UrlUtil.encode("SpringBoot搭建分布式Web服务脚手架"));
            put("tok", token);
            put("cuid", "starter_api_http_util");
            put("ctp", "1");
            put("lan", "zh");
            put("spd", "6");
            put("pit", "5");
            put("vol", "5");
            put("per", "1");
            put("aue", "6"); // 3为mp3格式(默认)； 4为pcm-16k；5为pcm-8k；6为wav（内容同pcm-16k）
        }};

        RespData resp = new RespData();
        byte[] ret = HttpUtil.sendHttpForm(url, headers, params, resp);
        Assert.assertNotNull(ret);

        String file = resp.saveFile(String.format("http_util_test.%s", resp.getFileExt()));
        LogUtil.info(file);
    }
}
