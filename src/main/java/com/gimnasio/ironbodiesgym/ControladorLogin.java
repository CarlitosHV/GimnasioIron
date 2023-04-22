package com.gimnasio.ironbodiesgym;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class ControladorLogin {

    public static String correo;
    public static String contrasenia;
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
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(rootPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        TraerUsuario();
    }


    public void TraerUsuario() throws Exception {
        if (camposValidos()){
            System.out.println("Campos correctos");
        }else{
            System.out.println("Campos incorrectos");
        }

    }

    private boolean camposValidos() throws Exception {
        ControladorCrearUsuario controladorCrearUsuario = new ControladorCrearUsuario();
        loginuser = controladorBD.loginUsuario(Campo_correo.getText());
        /*byte[] contracifrada = loginuser.get(2).toString().getBytes();
        String contradecifrada = controladorCrearUsuario.descifra(contracifrada);
        System.out.println(contradecifrada);*/


        String correo = String.valueOf(loginuser.get(1));
        boolean admin = (boolean) loginuser.get(2);
        boolean bloqueado = (boolean) loginuser.get(3);
        if (Campo_correo.getText().equals(correo) && bloqueado){
            if (admin){
                ViewSwitcher.switchTo(View.MENU_ADMINISTRADOR, IndexApp.Tema);
            }else{
                ViewSwitcher.switchTo(View.MENU_USUARIO, IndexApp.Tema);
            }
        }else{
            System.out.println("Usuario incorrecto");
        }
        return true;
    }


}
