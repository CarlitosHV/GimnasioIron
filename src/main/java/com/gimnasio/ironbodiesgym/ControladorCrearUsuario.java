package com.gimnasio.ironbodiesgym;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class ControladorCrearUsuario {

    public Button Boton;
    @FXML
    private AnchorPane rootPane;


    public void Regresar(){
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(rootPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(actionEvent -> ViewSwitcher.switchTo(View.LOGIN, ViewSwitcher.MODO_CLARO));
        fadeTransition.play();
    }


}
