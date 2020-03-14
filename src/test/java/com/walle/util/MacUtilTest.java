package com.walle.util;

import org.junit.Assert;
import org.junit.Test;

public class MacUtilTest {
    @Test
    public void testGetMacAddr() {
        String ret = MacUtil.gtMacAddr();
        System.out.println(ret);
        Assert.assertNotNull(ret);
    }
}
