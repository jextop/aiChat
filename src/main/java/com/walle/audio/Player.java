package com.walle.audio;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

class Player implements Runnable {
    private static ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(
            4,
            new BasicThreadFactory.Builder().namingPattern("audio-player-pool-%d").daemon(true).build()
    );

    public static void asyncPlay(byte[] audioBytes, TimeListener listener) {
        if (audioBytes == null || audioBytes.length <= 0) {
            return;
        }

        ByteArrayInputStream audioStream = new ByteArrayInputStream(audioBytes);

        // 播放进程
        Player player = new Player();
        player.listener = listener;

        try {
            player.audioStream = AudioSystem.getAudioInputStream(audioStream);
        } catch (UnsupportedAudioFileException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        executorService.execute(player);
    }

    public static void asyncPlay(URL fileUrl, TimeListener listener) {
        if (fileUrl == null) {
            return;
        }

        // 播放进程
        Player player = new Player();
        player.listener = listener;

        try {
            player.audioStream = AudioSystem.getAudioInputStream(fileUrl);
        } catch (UnsupportedAudioFileException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        executorService.execute(player);
    }

    public static void asyncPlay(ByteArrayOutputStream byteOutputStream, TimeListener listener) {
        if (byteOutputStream == null || byteOutputStream.size() <= 0) {
            return;
        }

        // 输入流
        byte[] audioBytes = byteOutputStream.toByteArray();
        int len = audioBytes.length;
        ByteArrayInputStream audioStream = new ByteArrayInputStream(audioBytes);
        AudioFormat audioFormat = FormatUtil.getAudioFormat();

        // 播放进程
        Player player = new Player();
        player.listener = listener;
        player.audioFormat = audioFormat;
        player.audioStream = new AudioInputStream(audioStream, audioFormat, len / audioFormat.getFrameSize());
        executorService.execute(player);
    }

    private AudioFormat audioFormat;
    private AudioInputStream audioStream;
    private TimeListener listener;

    private Player(){
    }

    @Override
    public void run() {
        try {
            play(audioStream, audioFormat, listener);
        } catch (LineUnavailableException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            if (audioStream != null) {
                try {
                    audioStream.close();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    public static void play(AudioInputStream audioStream, AudioFormat audioFormat, TimeListener listener)
            throws IOException, LineUnavailableException {
        if (audioStream == null) {
            return;
        }

        if (audioFormat == null) {
            audioFormat = audioStream.getFormat();
        }

        DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine dataLine = (SourceDataLine) AudioSystem.getLine(lineInfo);
        dataLine.open(audioFormat, 1024);
        dataLine.start();

        long startTime = System.currentTimeMillis();
        byte[] bytes = new byte[1024];
        int count;
        while ((count = audioStream.read(bytes)) > 0) {
            dataLine.write(bytes, 0, count);

            if (listener != null) {
                listener.timeUpdated((System.currentTimeMillis() - startTime) / 1000);
            }
        }

        dataLine.drain();
        dataLine.close();

        if (listener != null) {
            listener.stopped((System.currentTimeMillis() - startTime) / 1000);
        }
    }
}
