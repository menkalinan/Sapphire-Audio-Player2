package test.java.com.sdx2.SapphireAudioPlayer.audio;
import main.java.com.sdx2.SapphireAudioPlayer.audio.Player;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IsPlayingTest {
    @Test
    public void isPlayingTest(){
        Player pl = new Player();
        assertThat(pl.isPlaying());
    }
}
