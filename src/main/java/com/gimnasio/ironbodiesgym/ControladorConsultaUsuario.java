package com.gimnasio.ironbodiesgym;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControladorConsultaUsuario implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label LabelNombre, LabelCorreo, LabelDireccion, LabelEdad, LabelSexo, LabelTelefono;

    private ArrayList<Object> _usuario = new ArrayList<>();

    public static String correo;

    ControladorBD bd = new ControladorBD();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _usuario.clear();
        _usuario = bd.loginUsuario(correo);

        LabelNombre.setText("Nombre: " + _usuario.get(0) + " " + _usuario.get(1)
                            + " " + _usuario.get(2));

        LabelCorreo.setText("Correo: " + _usuario.get(3));

        LabelDireccion.setText("Dirección: " + _usuario.get(8) + " #" + _usuario.get(9) + ", "
                                + _usuario.get(10) + " ." + _usuario.get(11));

        LabelEdad.setText("Edad: " + _usuario.get(12).toString());

        LabelSexo.setText("Sexo: " + _usuario.get(13).toString());

        LabelTelefono.setText("Teléfono: " + _usuario.get(5));
    }
}
