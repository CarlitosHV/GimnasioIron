package com.gimnasio.ironbodiesgym;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class ControladorPrincipal extends Application {

    //Método que inicia la aplicación
    public static void main(String[] args){
        launch(args);
    }

    //Método que comienza y manda a crear el stage
    @Override
    public void start(Stage stage) throws IOException {
        new ControladorEscenas(stage);
    }

}
