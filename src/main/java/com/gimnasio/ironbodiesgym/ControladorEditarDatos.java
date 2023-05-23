package com.gimnasio.ironbodiesgym;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
<<<<<<< HEAD
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
=======
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

>>>>>>> 27a1a36a510f7e4e138794b8da2d59973492c32f
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControladorEditarDatos implements Initializable {
    ClaseUsuario claseUsuario = new ClaseUsuario();
    ControladorBD controladorBD = new ControladorBD();
    ControladorAlertas alertas = new ControladorAlertas();
    ControladorTransiciones transiciones = new ControladorTransiciones();

    public static boolean ADMINISTRADOR;
    @FXML
    private GridPane rootPane;

    private ArrayList<String> _municipios = new ArrayList<>();

    @FXML
    private TextField Campo_nombre, Campo_apellido_paterno, Campo_apellido_materno, Campo_correo,
            Campo_calle, Campo_numero, Campo_codigo_postal, Campo_telefono;
    @FXML
    private PasswordField Campo_contrasenia, Campo_repite_contrasenia;
    @FXML
    private ComboBox<String> Combo_municipio;
    @FXML
    private ComboBox<String> Combo_estado;
    @FXML
    private ProgressIndicator IconoCarga;
    int id;
    String nom, apaterno, amaterno, correo, contrasenia = null, calle, num, telefono, cp;
    boolean administrador = false;

    @FXML
    void Regresar() {
        if (ADMINISTRADOR){
            ADMINISTRADOR = false;
            transiciones.CrearAnimacionFade(500, rootPane, View.MENU_ADMINISTRADOR);
        }else{
            transiciones.CrearAnimacionFade(500, rootPane, View.MENU_USUARIO);
        }
    }

    /*
       Validaciones de los campos
    */
    @FXML
    void validar_campo_nombre() {
        if (Campo_nombre.getText().matches("^[a-zA-Z]+\\s?[a-zA-Z]*$") && !Campo_nombre.getText().isEmpty()) {
            Campo_nombre.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_nombre.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    void validar_campo_apellido_paterno() {
        if (Campo_apellido_paterno.getText().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]{3,10}$") && !Campo_apellido_paterno.getText().isEmpty()) {
            Campo_apellido_paterno.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_apellido_paterno.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    void validar_campo_apellido_materno() {
        if (Campo_apellido_materno.getText().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]{3,10}$") && !Campo_apellido_materno.getText().isEmpty()) {
            Campo_apellido_materno.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_apellido_materno.setStyle("-fx-border-color: red");
        }
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
        if (Campo_repite_contrasenia.getText().matches("^[A-Za-z\\d@$!%*?&]{6,12}$") && !Campo_repite_contrasenia.getText().isEmpty()) {
            Campo_repite_contrasenia.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_repite_contrasenia.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    void validar_campo_calle() {
        if (Campo_calle.getText().matches("^[A-Za-z0-9áéíóúÁÉÍÓÚñÑ.,\\-/ ]+$") && !Campo_calle.getText().isEmpty()) {
            Campo_calle.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_calle.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    void validar_campo_numero() {
        if (Campo_numero.getText().matches("^[0-9]{1,5}$") && !Campo_numero.getText().isEmpty()) {
            Campo_numero.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_numero.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    void validar_campo_codigo_postal() {
        if (Campo_codigo_postal.getText().matches("^[0-9]{1,5}$") && !Campo_codigo_postal.getText().isEmpty()) {
            Campo_codigo_postal.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_codigo_postal.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    void validar_campo_telefono() {
        if (Campo_telefono.getText().matches("^[0-9]{5,10}$") && !Campo_telefono.getText().isEmpty()) {
            Campo_telefono.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_telefono.setStyle("-fx-border-color: red");
        }
    }



    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){
        //Arreglos que guardan la información de los municipios y estados
        ArrayList<String> _estados = controladorBD.devolverEstados();
        Combo_estado.getItems().addAll(FXCollections.observableArrayList(_estados));
        Combo_municipio.setPromptText("Selecciona un estado");
        ArrayList<Object> _usuario;
        if (ADMINISTRADOR){
            _usuario = controladorBD.loginUsuario(ControladorMenuAdmin.CORREO_USUARIO);
        }else{
            _usuario = controladorBD.loginUsuario(ControladorLogin.loginuser.get(3).toString());
        }
        ControladorCrearUsuario.codigoduplicado(IconoCarga, Campo_nombre, Campo_apellido_paterno, Campo_apellido_materno);
        ControladorCrearUsuario.TextFormatter(Campo_correo, Campo_contrasenia, Campo_repite_contrasenia, Campo_calle, Campo_numero, Campo_codigo_postal, Campo_telefono);
        id = (int) _usuario.get(15);
        nom = _usuario.get(0).toString();
        apaterno = _usuario.get(1).toString();
        amaterno = _usuario.get(2).toString();
        correo = _usuario.get(3).toString();
        try {
            contrasenia = retornar_contrasenia(_usuario.get(4).toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        calle = _usuario.get(8).toString();
        num = _usuario.get(9).toString();
        telefono = _usuario.get(5).toString();
        cp = _usuario.get(16).toString();
        Campo_nombre.setText(nom);
        Campo_apellido_paterno.setText(apaterno);
        Campo_apellido_materno.setText(amaterno);
        Campo_correo.setText(correo);
        Campo_contrasenia.setText(contrasenia);
        Campo_repite_contrasenia.setText(contrasenia);
        Campo_calle.setText(calle);
        Campo_numero.setText(num);
        Campo_telefono.setText(telefono);
        Campo_codigo_postal.setText(cp);
        Combo_estado.setValue(_usuario.get(11).toString());
        Combo_municipio.setValue(_usuario.get(10).toString());
        _municipios = controladorBD.devolverMunicipios(_usuario.get(11).toString());
        Combo_municipio.getItems().addAll(FXCollections.observableArrayList(_municipios));
    }

    @FXML
    void mostrarMunicipio(){
        _municipios.clear();
        Combo_municipio.getItems().clear();
        Combo_municipio.setPromptText("Selecciona un estado");
        IconoCarga.setVisible(true);
        IconoCarga.setProgress(-1.0);
        rootPane.setOpacity(0.5);

        Task<ArrayList<String>> traer_municipios = new Task<>() {
            @Override
            protected ArrayList<String> call() {
                return controladorBD.devolverMunicipios(Combo_estado.getValue());
            }
        };
        new Thread(traer_municipios).start();


        traer_municipios.setOnSucceeded(event -> {
            IconoCarga.setVisible(false);
            rootPane.setOpacity(1.0);
            _municipios = traer_municipios.getValue();
            Combo_municipio.getItems().addAll(FXCollections.observableArrayList(_municipios));
        });
    }

    private String retornar_contrasenia(String contraseniaCifrada) throws Exception{
        return ControladorCifrarContrasena.decrypt(contraseniaCifrada);
    }

    private void insertarClaseUsuario() throws Exception {
        ControladorCrearUsuario.LlenarClase(claseUsuario, Campo_nombre, Campo_apellido_paterno, Campo_apellido_materno, Campo_correo, Campo_contrasenia, Campo_telefono);
        claseUsuario.setCalle(Campo_calle.getText());
        claseUsuario.setNumero(Integer.parseInt(Campo_numero.getText()));
        claseUsuario.setCodigo_postal(Integer.parseInt(Campo_codigo_postal.getText()));
        claseUsuario.setMunicipio(String.valueOf(Combo_municipio.getValue()));
        claseUsuario.setEstado(String.valueOf(Combo_estado.getValue()));
    }

    @FXML
    void GuardarUsuario() throws Exception {
        if (Campo_contrasenia.getText().equals(Campo_repite_contrasenia.getText())) {
            insertarClaseUsuario();
            boolean actualizarCuenta = controladorBD.actualizar_Usuario(id, claseUsuario.getNombre(), claseUsuario.getApellido_paterno(), claseUsuario.getApellido_materno(),
                    claseUsuario.getCorreo(), claseUsuario.getContrasenia(), claseUsuario.getTelefono(),
                    claseUsuario.getCalle(), claseUsuario.getNumero(), claseUsuario.getCodigo_postal(), claseUsuario.getMunicipio(),
                    claseUsuario.getEstado());
            if (actualizarCuenta) {
                if(ADMINISTRADOR){
                    ADMINISTRADOR = false;
                    alertas.CrearAlerta(ControladorAlertas.ALERTA_USUARIO_EDITADO_ADMIN, rootPane);
                }else{
                    alertas.CrearAlerta(ControladorAlertas.ALERTA_USUARIO_EDITADO, rootPane);
                }
            } else {
                alertas.CrearAlerta(ControladorAlertas.ALERTA_CORREO_DUPLICADO, rootPane);
            }
        } else {
            alertas.CrearAlerta(ControladorAlertas.ALERTA_CONTRASENIAS_DIFERENTES, rootPane);
        }

    }
}
