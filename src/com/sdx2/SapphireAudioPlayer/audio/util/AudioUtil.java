package com.sdx2.SapphireAudioPlayer.audio.util;

import javax.sound.sampled.AudioFormat;

public class AudioUtil {
    public static long bytesToSamples(long bytes, int frameSize) {
        return bytes / frameSize;
    }

    public static long samplesToBytes(long samples, int frameSize) {
        return samples * frameSize;
    }

    public static double samplesToMillis(long samples, int sampleRate) {
        return (double) samples / sampleRate * 1000;
    }

    public static double bytesToMillis(long bytes, AudioFormat fmt) {
        long l = AudioUtil.bytesToSamples(bytes, fmt.getFrameSize());
        return samplesToMillis(l, (int) fmt.getSampleRate());
    }

    public static int convertBuffer(byte[] input, int[] output, int len, AudioFormat fmt) {
        int bps = fmt.getSampleSizeInBits() / 8;
        int target = 0;
        int i = 0;
        while (target < len) {
            switch (bps) {
                case 1:
                    output[i++] = input[target++];
                    break;
                case 2:
                    output[i++] = (short)((input[target++] & 0xFF) | (input[target++] << 8));
                    break;
                case 3:
                    output[i++] = (input[target++] & 0xFF) | (input[target++] << 8 & 0xFF00) | (input[target++] << 16);
                    break;
            }
        }
        return i;
    }

    public static String formatSeconds(double seconds, int precision) {
        int min = (int) ((Math.round(seconds)) / 60);
        int hrs = min / 60;
        if (min > 0) seconds -= min * 60;
        if (seconds < 0) seconds = 0;
        if (hrs > 0) min -= hrs * 60;
        int days = hrs / 24;
        if (days > 0) hrs -= days * 24;
        int weeks = days / 7;
        if (weeks > 0) days -= weeks * 7;

        StringBuilder builder = new StringBuilder();
        if (weeks > 0) builder.append(weeks).append("wk ");
        if (days > 0) builder.append(days).append("d ");
        if (hrs > 0) builder.append(hrs).append(":");
        if (hrs > 0 && min < 10) builder.append("0");
        builder.append(min).append(":");
        int sec = (int) seconds;
        if (sec < 10) builder.append("0");
        builder.append(Math.round(sec));
        return builder.toString();
    }

    public static String samplesToTime(long samples, int sampleRate, int precision) {
        if (samples <= 0)
            return "-:--";
        double seconds = AudioUtil.samplesToMillis(samples, sampleRate) / 1000f;
        return formatSeconds(seconds, precision);
    }

    public static String removeExt(String s) {
        int index = s.lastIndexOf(".");
        if (index == -1) index = s.length();
        return s.substring(0, index);
    }

    public static int millisToSamples(int millis, int sampleRate) {
        return millis / 1000 * sampleRate;
    }
}
