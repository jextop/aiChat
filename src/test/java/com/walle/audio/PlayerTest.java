package com.walle.audio;

import org.junit.Test;

import java.net.URL;

public class PlayerTest {
    @Test
    public void testPlayUrl() throws Exception {
        URL fileUrl = new URL("http://q671m4cqj.bkt.clouddn.com/a0cd7e78db4dcc2149e7a556531094828.wav");
        Player.asyncPlay(fileUrl);
        Thread.sleep(3000);
    }
}
