package com.gimnasio.ironbodiesgym;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ControladorEditarDatos {

    @FXML
    private AnchorPane rootPane;

    @FXML private TextField Campo_nombre, Campo_apellido_paterno, Campo_apellido_materno, Campo_edad, Campo_correo,
            Campo_calle, Campo_numero, Campo_codigo_postal, Campo_telefono;
    @FXML
    PasswordField Campo_contrasenia, Campo_repite_contrasenia;
    @FXML
    private ComboBox<String> Combo_municipio;
    @FXML
    private ComboBox<String> Combo_estado;

    ControladorLogin controladorLogin = new ControladorLogin();
    ControladorBD bd = new ControladorBD();

    ControladorTransiciones transiciones = new ControladorTransiciones();
    IndexApp indexApp = new IndexApp();
    ArrayList<Object> suscripcion = new ArrayList<>();

    @FXML
    void Regresar(){
        transiciones.CrearAnimacionFade(500, rootPane, View.LOGIN);
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


    public void initialize(URL url, ResourceBundle resourceBundle) {
        int id = (int) ControladorLogin.loginuser.get(15);
        String nombre = "Nombre: " + ControladorLogin.loginuser.get(0).toString() + " " + controladorLogin.loginuser.get(1).toString() + " " + controladorLogin.loginuser.get(2).toString();
        String ap ="Apellido_Paterno: " + ControladorLogin.loginuser.get(2) + " " + controladorLogin.loginuser.get(9);
        String am = "Apellido_Materno: " + ControladorLogin.loginuser.get(3).toString();
        Campo_nombre.setText(nombre);
        Campo_apellido_paterno.setText(ap);
        Campo_apellido_materno.setText(am);

    }
}
