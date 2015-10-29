package main.java.com.sdx2.SapphireAudioPlayer.audio;

import main.java.com.sdx2.SapphireAudioPlayer.audio.data.Playlist;
import main.java.com.sdx2.SapphireAudioPlayer.audio.data.Track;
import main.java.com.sdx2.SapphireAudioPlayer.audio.data.TrackBuffer;
import main.java.com.sdx2.SapphireAudioPlayer.audio.io.AudioOutput;

import java.util.ArrayList;

public class Player {

    private PlayThread playThread;
    private BufferingThread bufferingThread;
    private static final int BUFFER_SIZE = (int) Math.pow(2, 18);
    private ArrayList<PlayerListener> listeners = new ArrayList<PlayerListener>();


    public Player() {
        TrackBuffer buffer = new TrackBuffer(BUFFER_SIZE);

        playThread = new PlayThread(this, buffer);
        bufferingThread = new BufferingThread(buffer, playThread);
        new Thread(bufferingThread, "BufferThread").start();
        Thread tt = new Thread(playThread, "PlayThread");
        tt.setPriority(Thread.MAX_PRIORITY);
        tt.start();
    }

    public void open(Track track) {
        bufferingThread.send(PlayActor.Message.OPEN, track);
    }

    public void play() {
        if (!isPaused()) {
            Track track = getTrack();
            if (track == null) {
                next();
            } else {
                bufferingThread.send(PlayActor.Message.OPEN, track);
            }
        }
    }

    public void pause() {
        playThread.send(PlayActor.Message.PAUSE);
    }

    public void seek(long sample) {
        bufferingThread.send(PlayActor.Message.SEEK, sample);
    }

    public void stop() {
        bufferingThread.send(PlayActor.Message.STOP);
    }

    public void next() {
        Track s = bufferingThread.getPlayList().next(getTrack());
        if (s != null) {
            open(s);
        } else {
            stop();
        }
    }

    public void prev() {
        Track s = bufferingThread.getPlayList().prev(getTrack());
        if (s != null) {
            open(s);
        } else {
            stop();
        }
    }

    public AudioOutput getAudioOutput() {
        return playThread.getOutput();
    }

    public Track getTrack() {
        return playThread.getCurrentTrack();
    }

    public double getPlaybackTime() {
        return playThread.getPlaybackTime();
    }

    public boolean isPlaying() {
        return playThread.isActive() && getTrack() != null;
    }

    public boolean isPaused() {
        return !isPlaying() && !isStopped();
    }

    public boolean isStopped() {
        return !bufferingThread.isActive();
    }

    public void setStopAfterCurrent(boolean stopAfterCurrent) {
        bufferingThread.setStopAfterCurrent(stopAfterCurrent);
    }

    public void setPlayList(Playlist playlist) {
        bufferingThread.setPlayList(playlist);
    }

    public Playlist getPlayList() {
        return bufferingThread.getPlayList();
    }

    synchronized void fireEvent(PlayerEvent.PlayerEventCode event) {
        PlayerEvent e = new PlayerEvent(event);
        for (PlayerListener listener : listeners) {
            listener.onEvent(e);
        }
    }

    public void addListener(PlayerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(PlayerListener listener) {
        listeners.remove(listener);
    }

}
