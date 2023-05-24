package com.gimnasio.ironbodiesgym;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ControladorMenuUsuario implements Initializable {

    @FXML
    private GridPane rootPane;

    @FXML private Label LabelNombre, LabelDireccion, LabelCorreo, LabelTipoCuenta,
            LabelHeader, LabelFechaVencimiento, Renovar_suscripcion, LabelNoSuscripcion;

    ControladorLogin controladorLogin = new ControladorLogin();
    ControladorBD bd = new ControladorBD();

    ControladorTransiciones transiciones = new ControladorTransiciones();
    IndexApp indexApp = new IndexApp();
    ArrayList<Object> suscripcion = new ArrayList<>();
    ArrayList<Object> user = new ArrayList<>();

    @FXML
    void Regresar(){
        transiciones.CrearAnimacionFade(500, rootPane, View.LOGIN);
    }
    @FXML
    void Editar() {transiciones.CrearAnimacionFade(500, rootPane, View.EDITAR);
    }
    @FXML
    void Renovar_Suscripcion() {
        transiciones.CrearAnimacionFade(500, rootPane, View.RENOVAR_SUSCRIPCIONES);
    }

    @FXML
    void ActivarModoOscuro() {
        if (IndexApp.Tema == 1) {
            ViewSwitcher.switchTo(View.MENU_USUARIO, ViewSwitcher.MODO_CLARO);
            IndexApp.Tema = 0;
            indexApp.EscribirPropiedades("theme", String.valueOf(IndexApp.Tema));
        } else if (IndexApp.Tema == 0) {
            ViewSwitcher.switchTo(View.MENU_USUARIO, ViewSwitcher.MODO_OSCURO);
            IndexApp.Tema = 1;
            indexApp.EscribirPropiedades("theme", String.valueOf(IndexApp.Tema));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.setMinWidth(850);
            stage.setMinHeight(600);
            user = bd.loginUsuario(ControladorLogin.loginuser.get(3).toString());
            int id = (int) user.get(15);
            String nombre = "Nombre: " + user.get(0).toString() + " " + user.get(1).toString() + " " + user.get(2).toString();
            String direccion = "Dirección: " + user.get(8) + " " + user.get(9);
            String correo = "Correo: " + user.get(3).toString();
            LabelHeader.setText("Bienvenido, " + user.get(0).toString());
            LabelNombre.setText(nombre);
            LabelDireccion.setText(direccion);
            LabelCorreo.setText(correo);
            suscripcion = bd.devolverSuscripcion(id);

            if (!suscripcion.isEmpty()){
                SimpleDateFormat formato = new SimpleDateFormat("dd/MMMM/yyyy");
                Date date = (Date) suscripcion.get(2);
                Renovar_suscripcion.setVisible(false);
                LabelNoSuscripcion.setVisible(false);
                LabelFechaVencimiento.setText("Fecha de término: " + formato.format(date));
                LabelTipoCuenta.setText("Plan: " + suscripcion.get(0).toString());
            }else {
                LabelFechaVencimiento.setText("Fecha vencimiento: " + "Sin fecha");
                LabelTipoCuenta.setText("Tipo suscripción: " + "Sin suscripción");
            }

        });
    }
}
