/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abien.xray.projector;

import java.util.Random;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class HitsPane extends Region implements BeamListener {

    private static final Font FONT_DEFAULT = new Font(Font.getDefault().getFamily(), 30);
    private static final Random RANDOM = new Random();
    private static final Interpolator INTERPOLATOR = Interpolator.SPLINE(0.295, 0.800, 0.305, 1.000);
    private Rectangle frame;

    public HitsPane() {
        setId("HitsPane");
        setPrefSize(1024, 768);
        setOpacity(1);
        createFrame();
        setOnKeyPressed(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ESCAPE) {
                    System.exit(1);
                }
            }
        });
    }

    public void createFrame() {
        this.frame = new Rectangle(0, 0, 1023, 767);
        frame.setFill(null);
        frame.setStroke(Color.GREEN);
        frame.setStrokeWidth(1);
        frame.setOpacity(0.75);
        this.getChildren().add(frame);
    }

    public void onBeamArrival(String message) {
        final Text text = new Text(message);
        text.setOpacity(1);
        text.setFill(Color.GREEN);
        text.setFont(FONT_DEFAULT);
        text.setTextOrigin(VPos.TOP);
        text.setTranslateX((getWidth() - text.getBoundsInLocal().getWidth()) / 2);
        text.setTranslateY((getHeight() - text.getBoundsInLocal().getHeight()) / 2);
        getChildren().add(text);
        final Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                getChildren().remove(text);
            }
        },
                new KeyValue(text.translateXProperty(), getRandom(0.0f, getWidth() - text.getBoundsInLocal().getWidth()), INTERPOLATOR),
                new KeyValue(text.translateYProperty(), getRandom(0.0f, getHeight() - text.getBoundsInLocal().getHeight()), INTERPOLATOR),
                new KeyValue(text.opacityProperty(), 0f)));
        timeline.play();
    }

    private static float getRandom(double min, double max) {
        return (float) (RANDOM.nextFloat() * (max - min) + min);
    }

    void moving() {

    }
}
