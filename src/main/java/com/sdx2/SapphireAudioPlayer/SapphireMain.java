package main.java.com.sdx2.SapphireAudioPlayer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;


public class SapphireMain extends Application {
    private Scene scene;
    @Override public void start(Stage stage) {
                String version = "Web View ";
        try {
            version += readFile("src/main/java/version.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle(version);
        scene = new Scene(new Browser(),750,500, Color.web("#666970"));
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
class Browser extends Region {

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();

    public Browser() {
        boolean state = false;
        getStyleClass().add("browser");
        URL url = null;
        try {
            url = new File(System.getProperty("user.dir")+"/data/index.html").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        webEngine.load(url.toString());
        getChildren().add(browser);

        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    org.w3c.dom.Document doc = webEngine.getDocument();
                    // STOP LISTENER
                    org.w3c.dom.events.EventListener stopListener = new org.w3c.dom.events.EventListener() {
                        @Override
                        public void handleEvent(org.w3c.dom.events.Event evt) {
                            System.out.println("stop");
                            //***** ������� ****
                        }

                    };
                    org.w3c.dom.Element el = doc.getElementById("stop");
                    ((org.w3c.dom.events.EventTarget)el).addEventListener("click", stopListener, false);

                    // PLAY/PAUSE LISTENER
                    org.w3c.dom.events.EventListener playListener = new org.w3c.dom.events.EventListener() {
                        @Override
                        public void handleEvent(org.w3c.dom.events.Event evt) {
                            System.out.println("play");
                            if(!state){
                                //***** ������� ****
                            }else {
                                //******** ������� ***
                            }
                        }

                    };
                    org.w3c.dom.Element el2 = doc.getElementById("play");
                    ((org.w3c.dom.events.EventTarget)el2).addEventListener("click", playListener, false);

                    // PREV LISTENER
                    org.w3c.dom.events.EventListener prevListener = new org.w3c.dom.events.EventListener() {
                        @Override
                        public void handleEvent(org.w3c.dom.events.Event evt) {
                            System.out.println("prev");
                            //***** ������� ****
                        }

                    };

                    org.w3c.dom.Element el3 = doc.getElementById("prev");
                    ((org.w3c.dom.events.EventTarget)el3).addEventListener("click", prevListener, false);

                    // NEXT LISTENER
                    org.w3c.dom.events.EventListener nextListener = new org.w3c.dom.events.EventListener() {
                        @Override
                        public void handleEvent(org.w3c.dom.events.Event evt) {
                            System.out.println("next");
                            //***** ������� ****
                        }

                    };

                    org.w3c.dom.Element el4 = doc.getElementById("next");
                    ((org.w3c.dom.events.EventTarget)el4).addEventListener("click", nextListener, false);
                }
            }
        });

    }
    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }

    @Override protected double computePrefWidth(double height) {
        return 750;
    }

    @Override protected double computePrefHeight(double width) {
        return 500;
    }


}
