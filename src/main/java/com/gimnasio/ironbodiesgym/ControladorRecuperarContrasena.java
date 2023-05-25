package com.gimnasio.ironbodiesgym;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControladorRecuperarContrasena implements Initializable {

    @FXML
    private TextField Campo_correo;
    @FXML
    private PasswordField Campo_contrasenia, Campo_repetir_contrasena;
    @FXML
    private Button Boton_validar;
    @FXML
    public AnchorPane rootPane;

    ControladorBD bd = new ControladorBD();
    ControladorAlertas alertas = new ControladorAlertas();
    ControladorTransiciones transiciones = new ControladorTransiciones();


    @FXML
    void ValidarCorreo() throws Exception {
        /*
        Aquí va a ir la validación del correo y traerlo para mostrarlo en pantalla
         */
        switch (Boton_validar.getText()) {
            case "Validar" -> {
                if (!Campo_correo.getText().isEmpty()){
                    boolean encontrado = bd.validarCorreo(Campo_correo.getText());
                    if (encontrado){
                        TrasladarItems();
                    }else{
                        alertas.CrearAlerta(ControladorAlertas.ALERTA_CORREO_NO_ENCONTRADO, rootPane);
                    }
                }else{
                    alertas.CrearAlerta(ControladorAlertas.ALERTA_ERROR_CAMPOS, rootPane);
                }
            }
            case "Actualizar" -> {
                if (Campo_contrasenia.getText().equals(Campo_repetir_contrasena.getText())){
                    String contracifrada = ControladorCifrarContrasena.encript(Campo_contrasenia.getText());
                    boolean actualizada = bd.cambiarContrasenia(Campo_correo.getText(), contracifrada);
                    if (actualizada){
                        alertas.CrearAlerta(ControladorAlertas.ALERTA_CONTRASENIA_ACTUALIZADA, rootPane);
                    }else{
                        alertas.CrearAlerta(ControladorAlertas.ALERTA_ERROR_BD, rootPane);
                    }
                }else{
                    alertas.CrearAlerta(ControladorAlertas.ALERTA_CONTRASENIAS_DIFERENTES, rootPane);
                }
            }

        }
    }

    @FXML
    public void RegresarLogin() {
        transiciones.CrearAnimacionFade(500, rootPane, View.LOGIN);
    }

    @FXML
    void TrasladarItems() {
        /*
            Si encuentra el correo en BD, nos muestra los demás campos y los reacomoda y
            también desactiva el campo correo
         */
        Campo_correo.setEditable(false);
        animar(Campo_correo, 55.0);
        animar(Boton_validar, 160.0);

    }

    private void animar(Node elemento, double Y) {
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

    void OcultarItems() {
        //Ocultamos los campos que no se muestran en un principio y trasladamos los que se ven un poco al centro
        Campo_contrasenia.setVisible(false);
        Campo_repetir_contrasena.setVisible(false);
        Campo_correo.setLayoutX(123.0);
        Campo_correo.setLayoutY(152.0);
        Boton_validar.setLayoutX(225.0);
        Boton_validar.setLayoutY(205.0);
    }

    @FXML
    void validar_campo_correo() {
        if (Campo_correo.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") && !Campo_correo.getText().isEmpty()) {
            Campo_correo.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_correo.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    void validar_campo_contrasenia() {
        if (Campo_contrasenia.getText().matches("^[A-Za-z\\d@$!%*?&]{6,12}$") && !Campo_contrasenia.getText().isEmpty()) {
            Campo_contrasenia.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_contrasenia.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    void validar_campo_repetir_contrasenia() {
        if (Campo_repetir_contrasena.getText().matches("^[A-Za-z\\d@$!%*?&]{6,12}$") && !Campo_repetir_contrasena.getText().isEmpty()) {
            Campo_repetir_contrasena.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_repetir_contrasena.setStyle("-fx-border-color: red");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setMinWidth(530);
            stage.setMinHeight(400);
        });
        OcultarItems();

        Campo_correo.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() > 40) {
                return null;
            }
            return change;
        }));

        Campo_contrasenia.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() > 14) {
                return null;
            }
            return change;
        }));

        Campo_repetir_contrasena.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() > 14) {
                return null;
            }
            return change;
        }));
    }
}