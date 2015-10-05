package Module;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Oleksandr on 05.10.2015.
 */
public class Playlist {

    private ArrayList<MyTrack> musicList;

    public Playlist(String response){
        musicList = new ArrayList<MyTrack>();
        parseJSON(response);
    }

    public Playlist(){}

    private void parseJSON(String response){
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response) ;
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        MyTrack myTrack = new MyTrack();
        for (int i = 1; i < jsonArray.length() ; i++) {
            try {
                myTrack.setArtist((String)(jsonArray.getJSONObject(i).get("artist")));
                myTrack.setDate((Integer)(jsonArray.getJSONObject(i).get("date")));
                myTrack.setDuration((Integer)(jsonArray.getJSONObject(i).get("duration")));
                myTrack.setGenre_id((Integer)(jsonArray.getJSONObject(i).get("genre_id")));
                myTrack.setLyrics_id((Integer)(jsonArray.getJSONObject(i).get("lyrics_id")));
                myTrack.setTitle((String)(jsonArray.getJSONObject(i).get("title")));
                myTrack.setUrl((String)(jsonArray.getJSONObject(i).get("url")));
                musicList.add(myTrack);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void addTrackToPlayList(MyTrack myTrack){
        this.musicList.add(myTrack);
    }

    public ArrayList<MyTrack> getMusicList(){
        return this.musicList;
    }

    public void savePlaylist() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for(MyTrack track: musicList){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("artist",track.getArtist());
            jsonObject.put("date", track.getDate());
            jsonObject.put("duration", track.getDuration());
            jsonObject.put("genre_id", track.getGenre_id());
            jsonObject.put("lyrics_id", track.getLyrics_id());
            jsonObject.put("title", track.getTitle());
            jsonObject.put("url", track.getUrl());
            jsonArray.put(jsonObject);
        }
        try {

            FileWriter file = new FileWriter("my_playlist.json");
            file.write(jsonArray.toString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPlaylist() throws IOException {
        FileReader file = new FileReader("my_playlist.json");
        String result = "";
        int c;
        while((c=file.read())!=-1){
            result += Character.toString((char)c);
        }
        parseJSON(result);
    }
}
