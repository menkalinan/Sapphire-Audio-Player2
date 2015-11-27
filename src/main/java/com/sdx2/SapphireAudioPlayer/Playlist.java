package main.java.com.sdx2.SapphireAudioPlayer;

import java.util.ArrayList;

/**
 * Created by Äלטענמ on 26.11.2015.
 */
public class Playlist {
    private ArrayList <Song> playlist;

    public Playlist(){
        playlist = new ArrayList<>();
    }

    public ArrayList getPlaylist(){
        return playlist;
    }

    public void addSong(Song song){
        playlist.add(song);
    }

}
