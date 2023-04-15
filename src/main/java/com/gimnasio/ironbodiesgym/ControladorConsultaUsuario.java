package com.gimnasio.ironbodiesgym;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class ControladorConsultaUsuario {

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
        fadeTransition.setOnFinished(actionEvent -> ViewSwitcher.switchTo(View.MENU_ADMINISTRADOR, IndexApp.Tema));
    }
}
