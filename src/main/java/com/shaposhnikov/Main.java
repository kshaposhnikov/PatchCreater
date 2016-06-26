package com.shaposhnikov;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Patch Creater");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main_view.fxml"));
        Pane pane = fxmlLoader.load();

        final Scene mainScene = new Scene(pane, 600, 400, Color.LIGHTGRAY);
        primaryStage.setScene(mainScene);

        pane.prefWidthProperty().bind(mainScene.widthProperty());
        pane.prefHeightProperty().bind(mainScene.heightProperty());

        primaryStage.show();
    }
}