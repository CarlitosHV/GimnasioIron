package com.gimnasio.ironbodiesgym;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class ControladorLogin {
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
        fadeTransition.setOnFinished(actionEvent -> ViewSwitcher.switchTo(View.RECUPERAR_CONTRASENA, ViewSwitcher.MODO_CLARO));
        fadeTransition.play();
    }

    @FXML
    void AbrirMenu(ActionEvent event) {
        int prueba = Integer.parseInt(Campo_correo.getText());
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(rootPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        if (prueba == 1) {
            fadeTransition.play();
            fadeTransition.setOnFinished(actionEvent -> ViewSwitcher.switchTo(View.MENU_ADMINISTRADOR, IndexApp.Tema));
        } else if (prueba == 2) {
            fadeTransition.play();
            fadeTransition.setOnFinished(actionEvent -> ViewSwitcher.switchTo(View.MENU_USUARIO, IndexApp.Tema));
        }
    }


    @FXML
    void Contra(ActionEvent event) throws Exception {
        String contrasenia = Campo_contra.getText();
        byte[] contra = cifra(contrasenia);
        String descifra = descifra(contra);
        System.out.println(contra);
        System.out.println("-----------------");
        System.out.println(descifra);

    }

    public byte[] cifra(String sinCifrar) throws Exception {
        final byte[] bytes = sinCifrar.getBytes("UTF-8");
        final Cipher aes = obtieneCipher(true);
        final byte[] cifrado = aes.doFinal(bytes);
        return cifrado;
    }

    public String descifra(byte[] cifrado) throws Exception {
        final Cipher aes = obtieneCipher(false);
        final byte[] bytes = aes.doFinal(cifrado);
        final String sinCifrar = new String(bytes, "UTF-8");
        return sinCifrar;
    }

    private Cipher obtieneCipher(boolean paraCifrar) throws Exception {
        final String frase = "NuestraLLave";
        final MessageDigest digest = MessageDigest.getInstance("SHA");
        digest.update(frase.getBytes("UTF-8"));
        final SecretKeySpec key = new SecretKeySpec(digest.digest(), 0, 16, "AES");

        final Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        if (paraCifrar) {
            aes.init(Cipher.ENCRYPT_MODE, key);
        } else {
            aes.init(Cipher.DECRYPT_MODE, key);
        }

        return aes;
    }

}
