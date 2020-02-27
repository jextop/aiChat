package com.walle.audio;

import com.walle.http.HttpUtil;
import com.walle.http.RespData;
import com.walle.util.B64Util;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class ChatUtil {
    public static void chat() {
        RecordHelper recordHelper = RecordHelper.getInst();
        final ByteArrayOutputStream data = recordHelper.save(new ByteArrayOutputStream());

        RespData resp = new RespData();
        byte[] ret = HttpUtil.sendHttpPost(
                "http://localhost:8011/speech/walle",
                null, new HashMap<String, Object>() {{
                    put("size", data.size());
                    put("format", "wav");
                    put("audio", B64Util.encode(data.toByteArray()));
                }}, resp
        );
        System.out.printf("%s, %s, %s, %d, %d\n",
                resp.getContentType(), resp.getFileName(), resp.getFileExt(),
                resp.getContentLength(), ret == null ? 0 : ret.length
        );

        if (ret != null && ret.length > 0) {
            Player.asyncPlay(ret);
        } else {
            // 播放自己的声音吧
            recordHelper.play();
        }
    }
}
