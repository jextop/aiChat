package com.walle.http;

import com.alibaba.fastjson.JSONObject;
import com.walle.util.JsonUtil;
import com.walle.util.StrUtil;
import org.apache.commons.lang3.ArrayUtils;

public class LocationUtil {
    private static JSONObject location = null;

    public static JSONObject getLocation() {
        if (location == null) {
            synchronized (LocationUtil.class) {
                if (location == null) {
                    location = getLoc();
                }
            }
        }
        return location;
    }

    private static JSONObject getLoc() {
        String address = HttpUtil.sendHttpGet("http://whois.pconline.com.cn/ipJson.jsp");
        if (StrUtil.isEmpty(address)) {
            return null;
        }

        String[] jsonStrArr = StrUtil.parse(address, "\\{\"\\S*\\s*\\S*\"\\}");
        return ArrayUtils.isEmpty(jsonStrArr) ? null : JsonUtil.parseObj(jsonStrArr[0]);
    }
}
