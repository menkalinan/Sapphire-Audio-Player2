package test.java.com.sdx2.SapphireAudioPlayer.audio;

import main.java.com.sdx2.SapphireAudioPlayer.audio.data.Track;
import main.java.com.sdx2.SapphireAudioPlayer.audio.mp3.MP3Decoder;
import main.java.com.sdx2.SapphireAudioPlayer.audio.mp3.MP3FileReader;
import org.junit.*;
import java.io.File;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class MP3DecoderFileReaderTest {

    private static MP3Decoder mp3Decoder;
    private static MP3FileReader mp3FileReader;

    @BeforeClass
    public static void setInstance(){
        mp3Decoder = new MP3Decoder();
        mp3FileReader = new MP3FileReader();
    }

    @Test
    @Before
    public void testMP3DecoderOpen(){
        Track track = mp3FileReader.read(new File("test.mp3"));
        assertTrue(mp3Decoder.open(track));
    }

    @Test
    public void testMP3DecoderGetAudioFormat(){
        assertNotNull(mp3Decoder.getAudioFormat());
    }

    @Test
    public void testMP3DecoderSeekSample(){
        assertTrue(mp3Decoder.seekSample(100000));
    }

    @Test
    public void testMP3DecoderDecode(){
        byte [] buf = new byte[65536];
        assertTrue(mp3Decoder.decode(buf) >= 0);
    }

    @Test
    public void testMP3FileReaderReadSingle(){
        Track track = new Track();
        track.setLocation(new File("test.mp3").toURI().toString());

        MP3FileReader reader = mock(MP3FileReader.class);
        when(reader.read(new File(""))).thenReturn(track);
        Track myTrack = reader.read(new File(""));
        reader.readSingle(myTrack);
    }
}
