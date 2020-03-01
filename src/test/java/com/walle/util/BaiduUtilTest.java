package com.walle.util;

import com.walle.http.RespData;
import org.junit.Assert;
import org.junit.Test;

public class BaiduUtilTest {
    @Test
    public void testToken() {
        String ret = BaiduUtil.token();
        LogUtil.info(ret);
        Assert.assertNotNull(ret);
    }

    @Test
    public void testTts() {
        RespData ret = BaiduUtil.tts("test tts测试语音合成接口");
        LogUtil.info(ret.getContentType(), ret.getContentLength());
        Assert.assertNotNull(ret.getBytes());

        long len = ret.getContentLength();
        String b64Str = B64Util.encode(ret.getBytes());
        String format = ret.getFileExt();

        testAsr(format, b64Str, len);
    }

    public void testAsr(String format, String b64Str, long len) {
        /*
        {
          "corpus_no": "6798700593866868782",
          "err_msg": "success.",
          "err_no": 0,
          "result": [
            "Ss tts测试语音合成接口。"
          ],
          "sn": "383242169911582945835"
        }
        */
        Object ret = BaiduUtil.asr(format, b64Str, len);
        LogUtil.info(ret);
        Assert.assertNotNull(ret);
    }
}
