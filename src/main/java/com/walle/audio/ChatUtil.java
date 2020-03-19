package com.walle.audio;

import com.alibaba.fastjson.JSONObject;
import com.walle.http.HttpUtil;
import com.walle.http.LocationUtil;
import com.walle.http.RespData;
import com.walle.util.B64Util;
import com.walle.util.MacUtil;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class ChatUtil {
    public static void chat(TimeListener listener) {
        RecordHelper recordHelper = RecordHelper.getInst();
        final ByteArrayOutputStream data = recordHelper.save(new ByteArrayOutputStream());
        final JSONObject location = LocationUtil.getLocation();

        RespData resp = new RespData();
        byte[] ret = HttpUtil.sendHttpPost(
                "http://localhost:8011/speech/walle",
                null, new HashMap<String, Object>() {{
                    put("size", data.size());
                    put("format", "wav");
                    put("uid", MacUtil.gtMacAddr());
                    put("audio", B64Util.encode(data.toByteArray()));
                    put("ip", location == null ? "" : location.getString("ip"));
                }}, resp
        );
        System.out.printf("%s, %s, %s, %d, %d\n",
                resp.getContentType(), resp.getFileName(), resp.getFileExt(),
                resp.getContentLength(), ret == null ? 0 : ret.length
        );

        if (ret != null && ret.length > 0) {
            Player.asyncPlay(ret, listener);
        } else {
            // 播放自己的声音吧
            recordHelper.play(listener);
        }
    }
}
