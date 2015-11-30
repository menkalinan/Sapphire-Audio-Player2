package test.java.com.sdx2.SapphireAudioPlayer.audio;

import main.java.com.sdx2.SapphireAudioPlayer.audio.util.AudioUtil;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AudioUtilTest {

    @Test
    public void testFormatSeconds() throws Exception {
        assertThat(AudioUtil.formatSeconds(100000, 0)).contains("1d 3:46:40");
    }

    @Test
    public void testSamplesToTime() {
        assertThat(AudioUtil.samplesToTime(10000, 360, 1)).contains("0:27");
    }

    @Test
    public void testRemoveExt() {
        assertThat(AudioUtil.removeExt("ax.mp3")).contains("ax");
    }
}