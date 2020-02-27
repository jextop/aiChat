package com.walle.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class JsonUtil {
    public static String toStr(Object javaObj) {
        return javaObj == null ? null : JSONObject.toJSONString(javaObj);
    }

    public static JSONObject parseObj(String jsonStr) {
        if (StrUtil.isEmpty(jsonStr)) {
            return null;
        }

        try {
            return JSONObject.parseObject(jsonStr);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && msg.length() > 100) {
                msg = msg.substring(0, 100);
            }
            LogUtil.warn("Exception when parseJson", msg);
        }
        return null;
    }

    public static <T> T parseObj(String jsonStr, Class<T> clazz) {
        if (StrUtil.isEmpty(jsonStr)) {
            return null;
        }

        try {
            return JSONObject.parseObject(jsonStr, clazz);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && msg.length() > 100) {
                msg = msg.substring(0, 100);
            }
            LogUtil.warn("Exception when parseObj", msg);
        }
        return null;
    }

    public static JSONArray parseArr(String jsonStr) {
        if (StrUtil.isEmpty(jsonStr)) {
            return null;
        }

        try {
            return JSONObject.parseArray(jsonStr);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && msg.length() > 100) {
                msg = msg.substring(0, 100);
            }
            LogUtil.warn("Exception when parseJson", msg);
        }
        return null;
    }

    public static <T> List<T> parseArr(String jsonStr, Class<T> clazz) {
        if (StrUtil.isEmpty(jsonStr)) {
            return null;
        }

        try {
            return JSONObject.parseArray(jsonStr, clazz);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && msg.length() > 100) {
                msg = msg.substring(0, 100);
            }
            LogUtil.warn("Exception when parseObj", msg);
        }
        return null;
    }
}
