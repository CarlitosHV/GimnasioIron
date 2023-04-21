package com.gimnasio.ironbodiesgym;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ResourceBundle;

public class ControladorCrearUsuario implements Initializable {

    ClaseUsuario claseUsuario = new ClaseUsuario();
    ControladorBD controladorBD = new ControladorBD();

    private boolean campo_nombre, campo_apellido_paterno, campo_apellido_materno, campo_edad, campo_correo, campo_calle, campo_numero,campo_codigo_postal, campo_telefono;

    @FXML
    private TextField Campo_nombre, Campo_apellido_paterno, Campo_apellido_materno, Campo_edad, Campo_correo,
            Campo_calle, Campo_numero, Campo_codigo_postal, Campo_telefono;

    @FXML
    PasswordField Campo_contrasenia, Campo_repite_contrasenia;

    @FXML
    private ComboBox Combo_sexo, Combo_municipio,Combo_estado;

    @FXML
    private Button Boton_regresar, Boton_crear;
    @FXML
    private AnchorPane rootPane;

    /*
        Inicializar los campos
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Combo_estado.getItems().addAll("Estado México", "CDMX");
        Combo_municipio.getItems().addAll("Tenango", "Metepec", "Toluca");
        Combo_sexo.getItems().addAll("M", "F");

        Campo_nombre.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() > 16) {
                return null;
            }
            return change;
        }));

        Campo_apellido_paterno.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() > 10) {
                return null;
            }
            return change;
        }));

        Campo_apellido_materno.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() > 10) {
                return null;
            }
            return change;
        }));

        Campo_edad.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() > 2) {
                return null;
            }
            return change;
        }));

        Campo_correo.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() > 45) {
                return null;
            }
            return change;
        }));

        Campo_calle.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() > 20) {
                return null;
            }
            return change;
        }));

        Campo_numero.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() > 5) {
                return null;
            }
            return change;
        }));

        Campo_codigo_postal.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() > 5) {
                return null;
            }
            return change;
        }));

        Campo_telefono.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() > 10) {
                return null;
            }
            return change;
        }));
    }



    /*
        Validaciones de los campos
     */
    @FXML
    void validar_campo_nombre(javafx.scene.input.KeyEvent keyEvent){
        if (Campo_nombre.getText().matches("^[a-zA-Z]{3,15}$") && !Campo_nombre.getText().isEmpty()) {
            campo_nombre = true;
            Campo_nombre.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_nombre.setStyle("-fx-border-color: red");
            campo_nombre = false;
        }
    }

    @FXML
    void validar_campo_apellido_paterno(javafx.scene.input.KeyEvent keyEvent){
        if (Campo_apellido_paterno.getText().matches("^[a-zA-Z]{3,10}$") && !Campo_apellido_paterno.getText().isEmpty()) {
            campo_apellido_paterno = true;
            Campo_apellido_paterno.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_apellido_paterno.setStyle("-fx-border-color: red");
            campo_apellido_paterno = false;
        }
    }

    @FXML void validar_campo_apellido_materno(javafx.scene.input.KeyEvent keyEvent){
        if (Campo_apellido_materno.getText().matches("^[a-zA-Z]{3,10}$") && !Campo_apellido_materno.getText().isEmpty()) {
            campo_apellido_materno = true;
            Campo_apellido_materno.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_apellido_materno.setStyle("-fx-border-color: red");
            campo_apellido_materno = false;
        }
    }
    @FXML void validar_campo_edad(javafx.scene.input.KeyEvent keyEvent){
        if (Campo_edad.getText().matches("^[0-9]{1,2}$") && !Campo_edad.getText().isEmpty()) {
            campo_edad = true;
            Campo_edad.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_edad.setStyle("-fx-border-color: red");
            campo_edad = false;
        }
    }

    @FXML void validar_campo_correo(javafx.scene.input.KeyEvent keyEvent){
        if (Campo_correo.getText().matches("^[a-zA-Z],[0-9],[@*/._+-]{20,45}$") && !Campo_correo.getText().isEmpty()) {
            campo_correo = true;
            Campo_correo.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_correo.setStyle("-fx-border-color: red");
            campo_correo = false;
        }
    }
    @FXML void validar_campo_calle(javafx.scene.input.KeyEvent keyEvent){
        if (Campo_calle.getText().matches("^[a-zA-Z]{5,20}$") && !Campo_calle.getText().isEmpty()) {
            campo_calle = true;
            Campo_calle.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_calle.setStyle("-fx-border-color: red");
            campo_calle = false;
        }
    }

    @FXML void validar_campo_numero(javafx.scene.input.KeyEvent keyEvent){
        if (Campo_numero.getText().matches("^[0-9]{1,5}$") && !Campo_numero.getText().isEmpty()) {
            campo_numero = true;
            Campo_numero.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_numero.setStyle("-fx-border-color: red");
            campo_numero = false;
        }
    }

    @FXML void validar_campo_codigo_postal(javafx.scene.input.KeyEvent keyEvent){
        if (Campo_codigo_postal.getText().matches("^[0-9]{1,5}$") && !Campo_codigo_postal.getText().isEmpty()) {
            campo_codigo_postal = true;
            Campo_codigo_postal.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_codigo_postal.setStyle("-fx-border-color: red");
            campo_codigo_postal = false;
        }
    }

    @FXML void validar_campo_telefono(javafx.scene.input.KeyEvent keyEvent){
        if (Campo_telefono.getText().matches("^[0-9]{5,10}$") && !Campo_telefono.getText().isEmpty()) {
            campo_telefono = true;
            Campo_telefono.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_telefono.setStyle("-fx-border-color: red");
            campo_telefono = false;
        }
    }
    boolean CamposValidos(){
        if (campo_nombre && campo_apellido_paterno){
            return true;
        }else{
            return false;
        }
    }


    @FXML
    void Regresar(){
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(rootPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(actionEvent -> ViewSwitcher.switchTo(View.LOGIN, IndexApp.Tema));
        fadeTransition.play();
    }

    @FXML
    void GuardarUsuario() throws Exception {
        claseUsuario.setNombre(Campo_nombre.getText());
        claseUsuario.setApellido_paterno(Campo_apellido_paterno.getText());
        claseUsuario.setApellido_materno(Campo_apellido_materno.getText());
        claseUsuario.setCorreo(Campo_correo.getText());
        String contra = Contra();
        claseUsuario.setContrasenia(Contra());
        claseUsuario.setTelefono(Integer.parseInt(Campo_telefono.getText()));
        claseUsuario.setUsuario_administrador(false);
        claseUsuario.setCalle(Campo_calle.getText());
        claseUsuario.setNumero(Integer.parseInt(Campo_numero.getText()));
        claseUsuario.setCodigo_postal(Integer.parseInt(Campo_codigo_postal.getText()));
        claseUsuario.setMunicipio(String.valueOf(Combo_municipio.getValue()));
        claseUsuario.setEdad(Integer.parseInt(Campo_edad.getText()));
        claseUsuario.setEstado(String.valueOf(Combo_estado.getValue()));
        claseUsuario.setSexo(String.valueOf(Combo_sexo.getValue()));
        claseUsuario.setBloqueado(false);
        claseUsuario.setEstado_suscripcion(false);
        if (CamposValidos()){
            controladorBD.insertar_Usuario(claseUsuario.getNombre(), claseUsuario.getApellido_paterno(), claseUsuario.getApellido_materno(),
                    claseUsuario.getCorreo(), claseUsuario.getContrasenia(), claseUsuario.getTelefono(), claseUsuario.isUsuario_administrador(),
                    claseUsuario.getCalle(), claseUsuario.getNumero(), claseUsuario.getCodigo_postal(), claseUsuario.getMunicipio(),
                    claseUsuario.getEstado(), claseUsuario.getEdad(), claseUsuario.getSexo(), claseUsuario.isBloqueado(), claseUsuario.isEstado_suscripcion());
        }else{
            System.out.println("Campos no llenados correctamente");
        }

    }

    String Contra() throws Exception {
        if (Campo_contrasenia.getText().equals(Campo_repite_contrasenia.getText())){
            String contrasenia = Campo_contrasenia.getText();
            byte[] contra = cifra(contrasenia);
            return new String(contra);
        }else{
            System.out.println("No coinciden las contras");
            return null;
        }
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
