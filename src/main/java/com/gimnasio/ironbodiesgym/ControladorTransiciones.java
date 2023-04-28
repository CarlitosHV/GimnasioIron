package com.gimnasio.ironbodiesgym;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class ControladorTransiciones {

    public void CrearAnimacionFade(float Duracion, Node Nodo, View Vista) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(Duracion));
        fadeTransition.setNode(Nodo);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(actionEvent -> ViewSwitcher.switchTo(Vista, IndexApp.Tema));
        fadeTransition.play();
    }
}
