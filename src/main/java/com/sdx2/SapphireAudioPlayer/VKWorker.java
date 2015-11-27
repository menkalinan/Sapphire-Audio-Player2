package main.java.com.sdx2.SapphireAudioPlayer;

import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * Created by Дмитро on 30.10.2015.
 */
public class VKWorker {

    WebView browser;
    WebEngine webEngine;
    private String accessToken;
    private String id;

    public VKWorker(WebView browser){
        this.browser = browser;
        webEngine = browser.getEngine();
    }

    public void login(){
        System.out.println("Логін");
        String logUrl = "https://oauth.vk.com/authorize?client_id=5110645&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=audio&response_type=token&v=5.37";
        webEngine.load(logUrl);
        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                String access = webEngine.getLocation();
                if (access.contains("access_token")) {
                    System.out.println("Юху!");
                    Pattern p1 = Pattern.compile("access_token=");
                    Pattern p2 = Pattern.compile("user_id=");
                    accessToken = p1.split(access)[1].substring(0, p1.split(access)[1].indexOf("&"));
                    id = p2.split(access)[1];
                    URL url = null;
                    try {
                        url = new File(System.getProperty("user.dir") + "/data/index.html").toURI().toURL();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    if (url != null) {
                        Browser.state[2]=true;
                        Browser.state[1]=true;
                        webEngine.load(url.toString());
                    }
                }
            }
        });
    }

    public boolean reLogin(){
        Stage popup = new Stage();
        popup.initModality(Modality.WINDOW_MODAL);
        StackPane content = new StackPane();
        content.getChildren().setAll(
                new Label("Ви уже увійшли!")
        );
        content.setPrefSize(200, 100);

        popup.setScene(new Scene(content));
        popup.showAndWait();
        return true;
    }

    public Playlist getPlaylist() {
        String url = "https://api.vk.com/method/audio.get?user_id=" + id + "&access_token=" + accessToken;
        String json = HTMLExtractor.getHTML(url);
        Playlist playlist = null;
        try {
            JSONObject response = new JSONObject(json);
            JSONArray array = response.getJSONArray("response");
            if(array.length()!=0) {
                playlist = new Playlist();
                for (int i = 0; i < array.length(); i++) {
                    Song song = new Song();
                    song.setArtist(array.getJSONObject(i).getString("artist"));
                    song.setDuration(array.getJSONObject(i).getString("duration"));
                    song.setTitle(array.getJSONObject(i).getString("title"));
                    song.setUrl(array.getJSONObject(i).getString("url"));
                    playlist.addSong(song);
                }
            }else {return null;}

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return playlist;
    }

    public Playlist search(String q){
        try {
            q = URLEncoder.encode(q, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(q);
        Playlist pl = new Playlist();
        for (int i = 0; i <= 4; i++) {
            String url = "https://api.vk.com/method/audio.search?q=" + q + "&auto_complete=1&search_own=1&user_id=" + id + "&count=200&offset="+200*i + "&access_token=" + accessToken;
            String json = HTMLExtractor.getHTML(url);
            try {
                JSONObject response = new JSONObject(json);

                if(!response.getJSONArray("response").getString(0).equals("0")) {
                    JSONArray res = response.getJSONArray("response");
                    for (int j = 1; j < res.length(); j++) {
                        JSONObject buf = new JSONObject(response.getJSONArray("response").getString(j));
                        Song song = new Song();
                        song.setArtist(buf.getString("artist"));
                        song.setTitle(buf.getString("title"));
                        song.setUrl(buf.getString("url"));
                        song.setDuration(buf.getString("duration"));
                        pl.addSong(song);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return pl;
    }

    public void saveFile(String url) {
        URL urll;
        try {
            urll = new URL(url);

            URLConnection conn = urll.openConnection();
            InputStream is = conn.getInputStream();

            JFileChooser fc = new JFileChooser();
            if ( fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ) {
                OutputStream outstream = new FileOutputStream(fc.getSelectedFile());
                byte[] buffer = new byte[4096];
                int len;
                while ((len = is.read(buffer)) > 0) {
                    outstream.write(buffer, 0, len);
                }
                outstream.close();
            }
        } catch (MalformedURLException | FileNotFoundException e) {
            System.out.println("Файл не знайдено!");;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

