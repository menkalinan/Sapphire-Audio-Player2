package main.java.com.sdx2.SapphireAudioPlayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

class Browser extends Region {

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    private VKWorker vk;
    /**
     * state[0] - pause(true)/play(false)
     * state[1] - is authorized? (no - false)
     * state[2] - main window? (yes - true)
     */
    public static boolean[] state = {false, false, true};

    public Browser(String version) {
        getStyleClass().add("browser");
        URL url = null;
        try {
            url = new File(System.getProperty("user.dir")+"/data/index.html").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (url != null) {
            webEngine.load(url.toString());
        }


        getChildren().add(browser);

        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED && state[2]) {
                    org.w3c.dom.Document doc = webEngine.getDocument();
                    // STOP LISTENER
                    org.w3c.dom.events.EventListener stopListener = evt -> System.out.println("stop");
                    org.w3c.dom.Element el = doc.getElementById("stop");
                    ((org.w3c.dom.events.EventTarget) el).addEventListener("click", stopListener, false);

                    // PLAY/PAUSE LISTENER
                    org.w3c.dom.events.EventListener playListener = evt -> {
                        System.out.println("play");
                        if (!state[0]) {
                            state[0] = true;
                        } else {

                        }
                    };
                    org.w3c.dom.Element el2 = doc.getElementById("play");
                    ((org.w3c.dom.events.EventTarget) el2).addEventListener("click", playListener, false);

                    // PREV LISTENER
                    org.w3c.dom.events.EventListener prevListener = new org.w3c.dom.events.EventListener() {
                        @Override
                        public void handleEvent(org.w3c.dom.events.Event evt) {
                            System.out.println("prev");
                        }

                    };

                    org.w3c.dom.Element el3 = doc.getElementById("prev");
                    ((org.w3c.dom.events.EventTarget) el3).addEventListener("click", prevListener, false);

                    // VOLUME LISTENER
                    org.w3c.dom.events.EventListener volListener = new org.w3c.dom.events.EventListener() {
                        @Override
                        public void handleEvent(org.w3c.dom.events.Event evt) {

                            System.out.println("vol");
                        }

                    };

                    org.w3c.dom.Element el6 = doc.getElementById("volumeRange");
                    ((org.w3c.dom.events.EventTarget) el6).addEventListener("change",volListener,false);

                    // NEXT LISTENER
                    org.w3c.dom.events.EventListener nextListener = evt -> System.out.println("next");

                    org.w3c.dom.Element el4 = doc.getElementById("next");
                    ((org.w3c.dom.events.EventTarget) el4).addEventListener("click", nextListener, false);

                    org.w3c.dom.events.EventListener vkListener = evt -> {
                        System.out.println("vk init");
                        state[2] = false;
                        if (!state[1]) {
                            vk = new VKWorker(browser);
                            vk.login();

                        } else {
                            vk.reLogin();
                        }
                    };
                    org.w3c.dom.Element el5 = doc.getElementById("vk-in");
                    ((org.w3c.dom.events.EventTarget) el5).addEventListener("click", vkListener, false);
                }
            }
        });

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
