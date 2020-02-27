package com.walle.audio;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class RecordHelperTest {
    @Test
    public void testRecord() throws Exception {
        RecordHelper recordHelper = RecordHelper.getInst();

        // Record
        recordHelper.record(null, null);
        recordHelper.stop(5000);

        ByteArrayOutputStream ret = recordHelper.save(new ByteArrayOutputStream());
        System.out.printf("data: %d\n", ret == null ? 0 : ret.size());

        recordHelper.play();
        Thread.sleep(3000);

        File file = recordHelper.save(new File("rec.wav"));
        System.out.println(file.getName());
    }
}
