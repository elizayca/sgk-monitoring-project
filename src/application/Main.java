package application;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static javafx.fxml.FXMLLoader.*;
//import library.AlertBox;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent parent = (Parent) load(getClass().getResource("MainPane.fxml"));
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Register Window");
        stage.show();
        stage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                deneme();
            }

        });

    }

    public static void main(String[] args) {
        launch(args);


    }

    public void Do() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("Alert.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Alert Window");
        stage.show();

    }
    public void deneme() {
        for (int i = 0; i <=1; i++) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                               public void run() {
                                   // System.out.println("30 Seconds Later");
                                   Platform.runLater(() -> {

                                   });

                               }

                           }, 10000
            );
        }
    }









}//sonparantez






