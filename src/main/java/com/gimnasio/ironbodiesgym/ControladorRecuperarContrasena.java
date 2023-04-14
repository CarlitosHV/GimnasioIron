package com.gimnasio.ironbodiesgym;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class ControladorRecuperarContrasena {

    @FXML
    public AnchorPane PanelPrin;

    public void RegresarLogin(){
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(PanelPrin);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(actionEvent -> ViewSwitcher.switchTo(View.LOGIN, IndexApp.Tema));
        fadeTransition.play();
    }
}
