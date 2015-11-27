package main.java.com.sdx2.SapphireAudioPlayer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class SapphireMain extends Application {
    private Scene scene;
    @Override public void start(Stage stage) {
        String version = "Ver. ";
        try {
            version += readFile("src/main/java/version.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }

        scene = new Scene(new Browser(version),750,500, Color.web("#666970"));
        stage.setScene(scene);
        scene.getStylesheets().add("webviewsample/BrowserToolbar.css");
        stage.show();
    }

    String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}