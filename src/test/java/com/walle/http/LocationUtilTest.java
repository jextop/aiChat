package com.walle.http;

import com.alibaba.fastjson.JSONObject;
import com.walle.util.LogUtil;
import org.junit.Assert;
import org.junit.Test;

public class LocationUtilTest {
    @Test
    public void testGetLocation() {
        /*
        {
            "regionCode": "0",
            "regionNames": "",
            "proCode": "310000",
            "err": "",
            "city": "上海市",
            "cityCode": "310000",
            "ip": "101.229.196.154",
            "pro": "上海市",
            "region": "",
            "addr": "上海市 电信"
        }
         */
        JSONObject ret = LocationUtil.getLocation();
        LogUtil.info(ret);
        Assert.assertNotNull(ret);
    }
}
