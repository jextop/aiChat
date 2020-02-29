package com.walle.audio;

import org.junit.Test;

import java.net.URL;

public class PlayerTest {
    static boolean isPlaying = true;

    @Test
    public void testPlayUrl() throws Exception {
        URL fileUrl = new URL("http://q671m4cqj.bkt.clouddn.com/a0cd7e78db4dcc2149e7a556531094828.wav");
        Player.asyncPlay(fileUrl, new TimeListener() {
            public void timeUpdated(long seconds) {
            }

            public void stopped(long seconds) {
                System.out.printf("player stopped: %d\n", seconds);
                synchronized (Player.class) {
                    isPlaying = false;
                }
            }
        });

        isPlaying = true;
        while (true) {
            Thread.sleep(1000);
            synchronized (Player.class) {
                if (!isPlaying) {
                    break;
                }
            }
        }
    }
}
