package com.gimnasio.ironbodiesgym;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ControladorMenuUsuario implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML private Label LabelNombre, LabelDireccion, LabelCorreo, LabelTipoCuenta, LabelFechaVencimiento;

    ControladorLogin controladorLogin = new ControladorLogin();


    ControladorTransiciones transiciones = new ControladorTransiciones();

    @FXML
    void Regresar(){
        transiciones.CrearAnimacionFade(500, rootPane, View.LOGIN);
    }

    @FXML
    void Renovar_Suscripcion() {
        transiciones.CrearAnimacionFade(500, rootPane, View.RENOVAR_SUSCRIPCIONES);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String nombre = "Nombre: " + ControladorLogin.loginuser.get(0).toString() + " " + controladorLogin.loginuser.get(1).toString() + " " + controladorLogin.loginuser.get(2).toString();
        String direccion = "Dirección: " + ControladorLogin.loginuser.get(8) + " " + controladorLogin.loginuser.get(9);
        String correo = "Correo: " + ControladorLogin.loginuser.get(3).toString();
        boolean suscripcion = (boolean) ControladorLogin.loginuser.get(14);
        LabelNombre.setText(nombre);
        LabelDireccion.setText(direccion);
        LabelCorreo.setText(correo);
        if (!suscripcion){
            LabelFechaVencimiento.setText("Fecha vencimiento: " + "Sin fecha");
            LabelTipoCuenta.setText("Tipo suscripción: " + "Sin suscripción");
        }
    }
}
