package com.gimnasio.ironbodiesgym;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ControladorRecuperarContrasena implements Initializable {

    @FXML
    private TextField Campo_correo;
    @FXML
    private PasswordField Campo_contrasenia, Campo_repetir_contrasena;
    @FXML
    private Button Boton_validar;
    @FXML
    public AnchorPane PanelPrin;

    @FXML
    void ValidarCorreo(){
        /*
        Aquí va a ir la validación del correo y traerlo para mostrarlo en pantalla
         */
        switch (Boton_validar.getText()){
            case "Validar":
                //Traemos la info de la bd
                TrasladarItems();
                break;
            case "Actualizar":
                //Mandamos a actualizar la nueva contraseña
                break;
        }

    }

    @FXML
    void RegresarLogin(){
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(PanelPrin);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(actionEvent -> ViewSwitcher.switchTo(View.LOGIN, IndexApp.Tema));
        fadeTransition.play();
    }

    @FXML
    void TrasladarItems(){
        /*
            Si encuentra el correo en BD, nos muestra los demás campos y los reacomoda y
            también desactiva el campo correo
         */
        animar(Campo_correo, -15.0);
        animar(Boton_validar, 120.0);

    }

    private void animar(Node elemento, double Y){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(elemento);
        transition.setByY(Y);
        transition.setDuration(Duration.seconds(2.0));
        transition.play();
        transition.setOnFinished(event -> {
            Campo_contrasenia.setVisible(true);
            Campo_repetir_contrasena.setVisible(true);
            Boton_validar.setText("Actualizar");
            Campo_correo.setEditable(false);
        });
    }

    void OcultarItems(){
        //Ocultamos los campos que no se muestran en un principio y trasladamos los que se ven un poco al centro
        Campo_contrasenia.setVisible(false);
        Campo_repetir_contrasena.setVisible(false);
        Campo_correo.setLayoutX(123.0);
        Campo_correo.setLayoutY(152.0);
        Boton_validar.setLayoutX(225.0);
        Boton_validar.setLayoutY(205.0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        OcultarItems();
    }

}
