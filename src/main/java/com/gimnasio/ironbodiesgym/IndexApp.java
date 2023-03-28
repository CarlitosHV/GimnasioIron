package com.gimnasio.ironbodiesgym;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class IndexApp extends Application {

    //Método que inicia la aplicación
    public static void main(String[] args){
        launch(args);
    }

    //Método que comienza y manda a crear el stage
    @Override
    public void start(Stage stage) throws IOException {
        var scene = new Scene(new Pane());
        ViewSwitcher.setScene(scene);
        ViewSwitcher.switchTo(View.LOGIN);
        stage.setScene(scene);
        stage.getIcons().add(new Image(IndexApp.class.getResourceAsStream("/assets/IconGym.png")));
        stage.setTitle("Iron Bodies Gym");
        stage.show();
    }

}