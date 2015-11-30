package test.java.com.sdx2.SapphireAudioPlayer.audio;
import main.java.com.sdx2.SapphireAudioPlayer.audio.PlayThread;
import main.java.com.sdx2.SapphireAudioPlayer.audio.Player;
import main.java.com.sdx2.SapphireAudioPlayer.audio.data.TrackBuffer;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IsActiveTest {
    @Test
    public void isPlayingTest(){
        Player pl = new Player();
        PlayThread pt = new PlayThread(pl,new TrackBuffer());
        assertThat(pt.isActive()).isFalse();
    }
}
