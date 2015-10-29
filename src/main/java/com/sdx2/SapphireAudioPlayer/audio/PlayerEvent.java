package main.java.com.sdx2.SapphireAudioPlayer.audio;

public class PlayerEvent {
    public enum PlayerEventCode {FILE_OPENED, PLAYING_STARTED, PAUSED, STOPPED, SEEK_FINISHED}

    private PlayerEventCode eventCode;

    public PlayerEvent(PlayerEventCode eventCode) {
        this.eventCode = eventCode;
    }

    public PlayerEventCode getEventCode() {
        return eventCode;
    }
}
