package com.gimnasio.ironbodiesgym;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class ControladorMenuUsuario {

    @FXML
    private AnchorPane rootPane;

    @FXML
    void Regresar(){
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(rootPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(actionEvent -> ViewSwitcher.switchTo(View.LOGIN, IndexApp.Tema));
    }

    @FXML
    void Renovar_Suscripcion() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(rootPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(actionEvent -> ViewSwitcher.switchTo(View.RENOVAR_SUSCRIPCIONES, IndexApp.Tema));
    }
}
