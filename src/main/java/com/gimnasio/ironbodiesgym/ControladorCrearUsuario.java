package com.gimnasio.ironbodiesgym;

import javafx.scene.control.Button;

public class ControladorCrearUsuario {

    public Button Boton;
    ControladorEscenas controladorEscenas;

    public void Clic(){
        System.out.println("Un click");
    }

    public ControladorEscenas getControladorEscenas() {
        return controladorEscenas;
    }

    public void setControladorEscenas(ControladorEscenas controladorEscenas) {
        this.controladorEscenas = controladorEscenas;
    }
}
