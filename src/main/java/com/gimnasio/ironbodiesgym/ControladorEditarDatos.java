package com.gimnasio.ironbodiesgym;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class ControladorEditarDatos implements Initializable {

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
        transiciones.CrearAnimacionFade(500, rootPane, View.MENU_USUARIO);
    }




    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int id = (int) ControladorLogin.loginuser.get(15);
        String nom =  ControladorLogin.loginuser.get(0).toString() ;
        String ap = ControladorLogin.loginuser.get(1).toString();
        String am =  ControladorLogin.loginuser.get(2).toString();
        String cor =  ControladorLogin.loginuser.get(3).toString();
        String con =  ControladorLogin.loginuser.get(4).toString();
        String rcon =  ControladorLogin.loginuser.get(4).toString();
        String calle =  ControladorLogin.loginuser.get(8).toString();
        String num =  ControladorLogin.loginuser.get(9).toString();
        String tel =  ControladorLogin.loginuser.get(5).toString();
        String cp =  ControladorLogin.loginuser.get(16).toString();
        Campo_nombre.setText(nom);
        Campo_apellido_paterno.setText(ap);
        Campo_apellido_materno.setText(am);
        Campo_correo.setText(cor);
        Campo_contrasenia.setText(con);
        Campo_repite_contrasenia.setText(rcon);
        Campo_calle.setText(calle);
        Campo_numero.setText(num);
        Campo_telefono.setText(tel);
        Campo_codigo_postal.setText(cp);

    }
}
