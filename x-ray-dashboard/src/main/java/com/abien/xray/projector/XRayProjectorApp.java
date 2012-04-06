/**
 * Copyright (c) 2008, 2011 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */
package com.abien.xray.projector;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 */
public class XRayProjectorApp extends Application {
    private HitsPane hitsPane;
    private String uri = "/x-ray/beam";

    public XRayProjectorApp() {
        hitsPane = new HitsPane();
    }
    
    
    
    private void init(final Stage stage) {
        stage.initStyle(StageStyle.TRANSPARENT);
        Group root = new Group();
        stage.setResizable(true);
        final Scene scene = new Scene(root, 1024,768);
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                stage.setX(me.getScreenX()+me.getSceneX());
                stage.setY(me.getScreenY()+me.getSceneY());
                hitsPane.moving();
            }
        });
        
        scene.setFill(null);
        stage.setScene(scene);
        root.getChildren().add(hitsPane);
        BeamProvider beamProvider = new BeamProvider(initialize(uri));
        beamProvider.setBeamListener(hitsPane);
    }

    public void play() {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                hitsPane.requestFocus();
            }
        });
    }


    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
        play();
    }
    
    public static void main(String[] args) { 
        launch(args); 
    }

    private String initialize(String uri) {
        String base = System.getProperty("server.uri");
        if(base == null){
            base = "http://localhost:8080";
        }
        return base + uri;
    }
}
