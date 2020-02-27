package com.walle.audio;

import javax.sound.sampled.AudioFormat;

public class FormatUtil {
    public static AudioFormat getAudioFormat() {
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 16000f;
        int sampleSize = 16;
        int channels = 1;
        boolean bigEndian = true;
        return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
    }
}
