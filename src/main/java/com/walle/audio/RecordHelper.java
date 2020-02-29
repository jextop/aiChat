package com.walle.audio;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Ding
 */
public class RecordHelper {
    private static RecordHelper inst = null;

    public static RecordHelper getInst() {
        if (inst == null) {
            synchronized (RecordHelper.class) {
                if (inst == null) {
                    inst = new RecordHelper();
                }
            }
        }
        return inst;
    }

    private ByteArrayOutputStream byteOutputStream = null;

    private RecordHelper() {
    }

    public void record(TimeListener timeListener, Long msDuration) {
        stop();

        byteOutputStream = new ByteArrayOutputStream();
        Recorder.record(byteOutputStream, timeListener, msDuration);
    }

    public void stop() {
        Recorder.stop();
    }

    public void stop(long millis) {
        Recorder.stop(millis);
    }

    public boolean isRecording() {
        return Recorder.isRecording();
    }

    public void play(TimeListener timeListener) {
        Player.asyncPlay(byteOutputStream, timeListener);
    }

    public <T> T save(T fileOrStream) {
        if (byteOutputStream == null || fileOrStream == null) {
            return fileOrStream;
        }

        // 录音输入流
        byte[] audioBytes = byteOutputStream.toByteArray();
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(audioBytes);

        AudioFormat audioFormat = FormatUtil.getAudioFormat();
        AudioInputStream audioInputStream = new AudioInputStream(byteInputStream, audioFormat, audioBytes.length / audioFormat.getFrameSize());

        // 写入文件或OutputStream
        try {
            if (fileOrStream instanceof File) {
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, (File) fileOrStream);
            } else if (fileOrStream instanceof OutputStream) {
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, (OutputStream) fileOrStream);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                audioInputStream.close();
                byteInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileOrStream;
    }
}
