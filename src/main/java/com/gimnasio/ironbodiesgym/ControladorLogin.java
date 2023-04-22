package com.gimnasio.ironbodiesgym;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Objects;

public class ControladorLogin {

    public static String correo;
    public static String contra;
    public static boolean usuario_administrador;
    public static boolean bloqueado;

    ArrayList<Object> loginuser = new ArrayList<>();

    ControladorBD controladorBD = new ControladorBD();

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button Boton_ingresar;
    @FXML
    private TextField Campo_correo;
    @FXML
    private PasswordField Campo_contra;

    public void Crear_usuario() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(rootPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(actionEvent -> ViewSwitcher.switchTo(View.CREAR_USUARIO, IndexApp.Tema));
        fadeTransition.play();
    }

    @FXML
    void RecuperarCuenta() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(rootPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(actionEvent -> ViewSwitcher.switchTo(View.RECUPERAR_CONTRASENA, IndexApp.Tema));
        fadeTransition.play();
    }

    @FXML
    void AbrirMenu(ActionEvent event) throws Exception {
        camposValidos();
    }


    private void camposValidos() throws Exception {
        ControladorCifrarContrasena controladorCifrarContrasena = new ControladorCifrarContrasena();
        loginuser = controladorBD.loginUsuario(Campo_correo.getText());
        String contraseniadecifrada = controladorCifrarContrasena.decrypt((String) loginuser.get(1));

        String correo = String.valueOf(loginuser.get(0));
        boolean admin = (boolean) loginuser.get(2);
        boolean bloqueado = (boolean) loginuser.get(3);

        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(rootPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        if (!bloqueado){
            if(Objects.equals(Campo_correo.getText(), correo) &&
                    Campo_contra.getText().equals(contraseniadecifrada)){
                if (admin){
                    fadeTransition.play();
                    fadeTransition.setOnFinished(event -> ViewSwitcher.switchTo(View.MENU_ADMINISTRADOR, IndexApp.Tema));
                }else{
                    fadeTransition.play();
                    fadeTransition.setOnFinished(event -> ViewSwitcher.switchTo(View.MENU_USUARIO, IndexApp.Tema));
                }
            }else{
                System.out.println("Correo erróneo");
            }
        }else{
            System.out.println("Usuario bloqueado :(");
        }
    }


}
