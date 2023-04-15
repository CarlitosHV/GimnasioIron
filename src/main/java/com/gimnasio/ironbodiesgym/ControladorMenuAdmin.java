package com.gimnasio.ironbodiesgym;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class ControladorMenuAdmin {

    @FXML
    private AnchorPane rootPane;


    @FXML
    void Abrir_informacion(ActionEvent event){
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(rootPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(actionEvent -> ViewSwitcher.switchTo(View.CONSULTA_USUARIO, IndexApp.Tema));
    }
}
