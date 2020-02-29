package com.walle.audio;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class RecordHelperTest {
    static boolean isRecording = false;
    static boolean isPlaying = false;

    @Test
    public void testRecord() throws Exception {
        RecordHelper recordHelper = RecordHelper.getInst();

        // Record
        recordHelper.record(new TimeListener() {
            public void timeUpdated(long seconds) {
            }

            public void stopped(long seconds) {
                System.out.printf("recorder stopped: %d\n", seconds);
                synchronized (Player.class) {
                    isRecording = false;
                }
            }
        }, null);
        recordHelper.stop(5000);

        isRecording = true;
        while (true) {
            Thread.sleep(1000);
            synchronized (Player.class) {
                if (!isRecording) {
                    break;
                }
            }
        }

        // save
        ByteArrayOutputStream ret = recordHelper.save(new ByteArrayOutputStream());
        System.out.printf("data: %d\n", ret == null ? 0 : ret.size());

        File file = recordHelper.save(new File("rec.wav"));
        System.out.println(file.getName());

        // play
        recordHelper.play(new TimeListener() {
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
