package main.java.com.sdx2.SapphireAudioPlayer.audio.data;

import javax.sound.sampled.AudioFormat;

public class BufferEntry {
    public Track track;
    public AudioFormat format;
    public long startSample;
    public boolean forced;

    public BufferEntry(Track track, AudioFormat format, long startSample, boolean forced) {
        this.track = track;
        this.format = format;
        this.startSample = startSample;
        this.forced = forced;
    }
}
