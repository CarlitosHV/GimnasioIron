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

    private boolean campo_nombre, campo_apellido_paterno;

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
