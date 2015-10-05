package Module;

/**
 * Created by Oleksandr on 05.10.2015.
 */
public class MyTrack {

    private String artist;
    private String title;
    private int duration;
    private int date;
    private String url;
    private int lyrics_id;
    private int genre_id;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLyrics_id() {
        return lyrics_id;
    }

    public void setLyrics_id(int lyrics_id) {
        this.lyrics_id = lyrics_id;
    }

    public int getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(int genre_id) {
        this.genre_id = genre_id;
    }
}
