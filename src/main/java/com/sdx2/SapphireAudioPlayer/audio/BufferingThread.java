package main.java.com.sdx2.SapphireAudioPlayer.audio;


import main.java.com.sdx2.SapphireAudioPlayer.audio.data.Playlist;
import main.java.com.sdx2.SapphireAudioPlayer.audio.data.Track;
import main.java.com.sdx2.SapphireAudioPlayer.audio.data.TrackBuffer;
import main.java.com.sdx2.SapphireAudioPlayer.audio.mp3.MP3Decoder;
import main.java.com.sdx2.SapphireAudioPlayer.audio.util.AudioUtil;

import javax.sound.sampled.AudioFormat;

public class BufferingThread extends PlayerActor implements Runnable {

    private Playlist playList;

    private final Object lock = new Object();
    private long currentByte = 0;
    private Track currentTrack;
    private Track nextTrack;
    private MP3Decoder decoder;
    private boolean active;

    private TrackBuffer buffer;
    private PlayThread playingThread;
    private boolean stopAfterCurrent = false;

    public BufferingThread(TrackBuffer buffer, PlayThread playingThread) {
        this.buffer = buffer;
        this.playingThread = playingThread;
    }

    @Override
    public void process(Message message) {
        Object[] params = message.getParams();
        switch (message) {
            case OPEN:
                if (params.length > 0 && params[0] instanceof Track) {
                    Track track = (Track) params[0];
                    pause(true);
                    open(track, true);
                }
                break;
            case SEEK:
                if (params.length > 0 && params[0] instanceof Long) {
                    Long sample = (Long) params[0];
                    seek(sample);
                }
                break;
            case STOP:
                stop(true);
                break;
        }
    }

    @SuppressWarnings({"InfiniteLoopStatement"})
    @Override
    public void run() {
        byte[] buf = new byte[65536];
        int len;
        while (true) {
            synchronized (lock) {
                try {
                    while (!active) {
                        lock.wait();
                    }
                    if (decoder == null) {
                        stop(false);
                        continue;
                    }

                    while (active) {
                        if (nextTrack != null) {
                            if (stopAfterCurrent) {
                                stop(false);
                                stopAfterCurrent = false;
                                continue;
                            }
                            open(nextTrack, false);
                            nextTrack = null;
                            continue;
                        }

                        len = decoder.decode(buf);

                        if (len == -1) {
                            nextTrack = null;
                            if (playList != null)
                                nextTrack = playList.next();
                            if (nextTrack == null) {
                                stop(false);
                            }
                            continue;
                        }

                        currentByte += len;

                        buffer.write(buf, 0, len);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop(boolean flush) {
        nextTrack = null;
        pause(flush);
        buffer.addTrack(null, null, -1, false);
        if (decoder != null) {
            decoder.close();
        }
        decoder = null;
    }

    private void pause(boolean flush) {
        active = false;
        if (flush)
            buffer.flush();
        synchronized (lock) {
        }
        if (flush)
            buffer.flush();
    }

    private void start() {
        active = true;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public synchronized void open(Track track, AudioFormat format, boolean forced) {
        if (decoder != null) {
            decoder.close();
        }

        if (track != null) {
            if (track.isFile() && !track.getFile().exists()) {
                track = playList.next();
                if (track == null || (
                        track.isFile() && !track.getFile().exists())) {
                    stop(false);
                    return;
                }
            }
            decoder = new MP3Decoder();
            decoder.open(track);
            currentTrack = track;
            currentByte = 0;

            if (decoder == null || !decoder.open(track)) {
                currentTrack = null;
                stop(false);
                return;
            }

            buffer.addTrack(track, format, false, -1);
            buffer.check("dd");
            if (track.getStartPosition() > 0)
                decoder.seekSample(track.getStartPosition());

            start();
            if (forced)
                playingThread.send(Message.FLUSH);
            playingThread.send(Message.PLAY);
        }
    }

    public synchronized void open(Track track, boolean forced) {
        if (decoder != null) {
            decoder.close();
        }

        if (track != null) {
            if (track.isFile() && !track.getFile().exists()) {
                track = playList.next();
                if (track == null || (
                        track.isFile() && !track.getFile().exists())) {
                    stop(false);
                    return;
                }
            }
            decoder = new MP3Decoder();
            decoder.open(track);
            currentTrack = track;
            currentByte = 0;

            if (decoder == null || !decoder.open(track)) {
                currentTrack = null;
                stop(false);
                return;
            }

            buffer.addTrack(track, decoder.getAudioFormat(), -1, forced);
            buffer.check("dd");
            if (track.getStartPosition() > 0)
                decoder.seekSample(track.getStartPosition());

            start();
            if (forced)
                playingThread.send(Message.FLUSH);
            playingThread.send(Message.PLAY);
        }
    }

    public void seek(long sample) {
        boolean oldState = active;
        pause(true);

        if (decoder != null) {
            decoder.seekSample(currentTrack.getStartPosition() + sample);
            currentByte = AudioUtil.samplesToBytes(sample, decoder.getAudioFormat().getFrameSize());
            buffer.addTrack(currentTrack, decoder.getAudioFormat(), sample, true);
            buffer.addTrack(currentTrack, decoder.getAudioFormat(), false, -1);
            if (oldState) {
                start();
            }
        }
    }

    public Playlist getPlayList() {
        return playList;
    }

    public void setPlayList(Playlist playlist) {
        this.playList = playlist;
    }

    public boolean isActive() {
        return active;
    }


    public void seek(Track track, AudioFormat format, MP3Decoder decoder, int sample) {
        boolean oldState = active;
        pause(true);

        if (decoder != null) {
            decoder.seekSample(track.getStartPosition() + sample);
            currentByte = AudioUtil.samplesToBytes(sample, decoder.getAudioFormat().getFrameSize());
            buffer.addTrack(track, format, true, sample);
            if (oldState) {
                start();
            }
        }
    }
}
