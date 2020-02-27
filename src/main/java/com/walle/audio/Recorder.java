package com.walle.audio;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

class Recorder implements Runnable {
    private static ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(
            4,
            new BasicThreadFactory.Builder().namingPattern("audio-player-pool-%d").daemon(true).build()
    );

    private static Long msDuration = null;
    private static Boolean isRecording = false;

    public static void record(ByteArrayOutputStream outputStream, TimeListener timeListener, Long millis) {
        System.out.println("Record thread");
        synchronized (Recorder.class) {
            msDuration = millis;
            isRecording = true;
        }

        // 创建录音线程
        Recorder recorder = new Recorder(outputStream, timeListener);
        executorService.execute(recorder);
    }

    public static void stop() {
        stop(0);
    }

    public static void stop(long millis) {
        synchronized (Recorder.class) {
            msDuration = millis;
        }
    }

    public static boolean isRecording() {
        synchronized (Recorder.class) {
            return isRecording;
        }
    }

    private ByteArrayOutputStream byteOutputStream;
    private TimeListener timeListener;

    private Recorder(ByteArrayOutputStream outputStream, TimeListener timeListener) {
        this.byteOutputStream = outputStream;
        this.timeListener = timeListener;
    }

    @Override
    public void run() {
        long msStart = System.currentTimeMillis();
        System.out.printf("Record start, %s\n", new Date(msStart).toString());

        TargetDataLine targetDataLine = null;
        try {
            AudioFormat audioFormat = FormatUtil.getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) (AudioSystem.getLine(info));
            targetDataLine.open(audioFormat);
            targetDataLine.start();

            byte[] bytes = new byte[1024 * 8];
            while (true) {
                int count = targetDataLine.read(bytes, 0, bytes.length);
                if (count > 0) {
                    byteOutputStream.write(bytes, 0, count);
                }

                long ms = (System.currentTimeMillis() - msStart);
                System.out.printf("Record %d, seconds: %d, stopped: %s\n", count, ms / 1000, String.valueOf(msDuration));

                if (timeListener != null) {
                    timeListener.timeUpdated(ms / 1000);
                }

                synchronized (Recorder.class) {
                    if (msDuration != null && ms >= msDuration) {
                        break;
                    }
                }
            }
        } catch (LineUnavailableException e) {
            System.err.println(e.getMessage());
        } finally {
            if (targetDataLine != null) {
                targetDataLine.close();
            }

            try {
                byteOutputStream.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

        synchronized (Recorder.class) {
            isRecording = false;
        }

        long seconds = (System.currentTimeMillis() - msStart) / 1000;
        System.out.printf("Record stop, %s, seconds: %d\n", new Date().toString(), seconds);

        if (timeListener != null) {
            timeListener.stopped(seconds);
        }
    }
}
