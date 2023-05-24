package com.gimnasio.ironbodiesgym;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControladorLogin implements Initializable {

    public static String correo;
    public static String contraseniadecifrada;
    public static boolean usuario_administrador;
    public static boolean bloqueado;

    public static ArrayList<Object> loginuser = new ArrayList<>();

    ControladorBD controladorBD = new ControladorBD();

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button Boton_ingresar;
    @FXML
    private TextField Campo_correo;
    @FXML
    private PasswordField Campo_contra;

    ControladorTransiciones transiciones = new ControladorTransiciones();
    ControladorAlertas alertas = new ControladorAlertas();

    public void Crear_usuario() {
        transiciones.CrearAnimacionFade(500, rootPane, View.CREAR_USUARIO);
    }

    @FXML
    void RecuperarCuenta() {
        transiciones.CrearAnimacionFade(500, rootPane, View.RECUPERAR_CONTRASENA);
    }

    @FXML
    void AbrirMenu(ActionEvent event) throws Exception {
        camposValidos();
    }


    private void camposValidos() throws Exception {
        loginuser = controladorBD.loginUsuario(Campo_correo.getText());
        if(loginuser != null){
            contraseniadecifrada = ControladorCifrarContrasena.decrypt(loginuser.get(4).toString());
            String correo = String.valueOf(loginuser.get(3));
            boolean admin = (boolean) loginuser.get(6);
            boolean bloqueado = (boolean) loginuser.get(7);
            if (!bloqueado){
                if(Objects.equals(Campo_correo.getText(), correo) &&
                        Campo_contra.getText().equals(contraseniadecifrada)){
                    if (admin){
                        transiciones.CrearAnimacionFade(500, rootPane, View.MENU_ADMINISTRADOR);
                    }else{
                        transiciones.CrearAnimacionFade(500, rootPane, View.MENU_USUARIO);
                    }
                }else{
                    alertas.CrearAlerta(ControladorAlertas.ALERTA_USUARIO_INVALIDO, rootPane);
                }
            }else{
                Campo_contra.setText("");
                Campo_correo.setText("");
                alertas.CrearAlerta(ControladorAlertas.ALERTA_USUARIO_BLOQUEADO, rootPane);
            }
        }else{
            Campo_contra.setText("");
            Campo_correo.setText("");
            alertas.CrearAlerta(ControladorAlertas.ALERTA_USUARIO_INEXISTENTE, rootPane);
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.setHeight(370);
            stage.setWidth(396);

            Campo_contra.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER){
                    try {
                        camposValidos();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });


        });


    }
}
