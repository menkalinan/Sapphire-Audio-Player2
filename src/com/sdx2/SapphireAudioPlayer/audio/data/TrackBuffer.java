package com.sdx2.SapphireAudioPlayer.audio.data;

import javax.sound.sampled.AudioFormat;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class TrackBuffer {
    private DataBuffer buffer;
    private BlockingQueue<BufferEntry> trackQueue = new LinkedBlockingDeque<BufferEntry>();
    private Queue<Integer> when = new LinkedList<Integer>();
    private int bytesLeft = 0;

    public class BufferEntry {
        public Track track;
        public AudioFormat format;
        public long startSample;
        public boolean forced;

        BufferEntry(Track track, AudioFormat format, long startSample, boolean forced) {
            this.track = track;
            this.format = format;
            this.startSample = startSample;
            this.forced = forced;
        }
    }

    public TrackBuffer(int size) {
        buffer = new DataBuffer(size);
    }

    public TrackBuffer() { this(65536); }

    public void write(byte[] b, int off, int len) {
        buffer.put(b, off, len);
    }

    public void addTrack(Track track, AudioFormat format, long startSample, boolean forced) {
        int bytesLeft = available();
        for (Integer left : when) {
            bytesLeft -= left;
        }
        if (trackQueue.isEmpty())
            this.bytesLeft = bytesLeft;
        else
            when.add(bytesLeft);
        trackQueue.add(new BufferEntry(track, format, startSample, forced));
    }

    public BufferEntry pollTrack() {
        BufferEntry bufferEntry = null;
        try {
            bufferEntry = trackQueue.take();
            buffer.setEOF(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!when.isEmpty()) {
            bytesLeft = when.poll();
        } else {
            bytesLeft = -1;
        }
        return bufferEntry;
    }

    public int read(byte[] b, int off, int len) {
        if (bytesLeft > 0) {
            if (bytesLeft < len) {
                len = bytesLeft;
            }
            bytesLeft -= len;
        } else if (bytesLeft == 0) {
            return -1;
        }
        return buffer.get(b, off, len);
    }

    public synchronized int available() {
        return buffer.getAvailable();
    }

    public int size() {
        return buffer.size();
    }

    public void flush() {
        buffer.empty();
    }
}
