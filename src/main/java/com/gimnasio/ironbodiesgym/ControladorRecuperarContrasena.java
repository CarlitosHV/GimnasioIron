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
                        //Obtener el correo electrónico ingresado por el usuario
                        String correo = Campo_correo.getText();
                        //Obtener la nueva contraseña ingresada por el usuario
                        String contraseniaNueva = Campo_contrasenia.getText();
                        //Obtener la confirmación de la nueva contraseña ingresada por el usuario
                        String confirmacionContrasena = Campo_repetir_contrasena.getText();
                        //Validar que los campos de contraseña y repetir contraseña coincidan
                        if (!contraseniaNueva.equals(confirmacionContrasena)) {
                            //Mostrar mensaje de error

                            return;
                        }
                        //Validar que la nueva contraseña cumpla con los requisitos de seguridad necesarios
                        if (!validarContrasena(contraseniaNueva)) {
                            //Mostrar mensaje de error


                            return;
                        }
                        //Actualizar la contraseña en la base de datos para el usuario correspondiente
                        //cambiarContrasenia (correo, contraseniaNueva);
                        //Mostrar mensaje de éxito

                        //alert.setContentText("La contraseña se actualizó correctamente");
                       // alert.showAndWait();
                        //Volver al inicio de sesión
                        //RegresarLogin();
                        break;
                    }
                }
                @FXML
                private boolean validarContrasena(String contrasena) {
                    //Validar que la contraseña tenga al menos 6 caracteres
                    if (contrasena.length() < 6) {
                        return false;
                    }
                    //Validar que la contraseña contenga letras mayúsculas, minúsculas y números
                    boolean tieneMayuscula = false;
                    boolean tieneMinuscula = false;
                    boolean tieneNumero = false;
                    for (char c : contrasena.toCharArray()) {
                        if (Character.isUpperCase(c)) {
                            tieneMayuscula = true;
                        } else if (Character.isLowerCase(c)) {
                            tieneMinuscula = true;
                        } else if (Character.isDigit(c)) {

                }

                @FXML
                public void RegresarLogin(){
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
                    transition.setDuration(Duration.seconds(1.0));
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
        transition.setDuration(Duration.seconds(1.0));
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
