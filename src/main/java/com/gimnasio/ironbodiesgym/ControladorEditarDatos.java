package com.gimnasio.ironbodiesgym;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControladorEditarDatos implements Initializable {
    ClaseUsuario claseUsuario = new ClaseUsuario();
    ControladorBD controladorBD = new ControladorBD();
    ControladorAlertas alertas = new ControladorAlertas();
    ControladorTransiciones transiciones = new ControladorTransiciones();
    @FXML
    private GridPane rootPane;

    //Arreglos que guardan la información de los municipios y estados
    private ArrayList<String> _estados = new ArrayList<>();
    private ArrayList<String> _municipios = new ArrayList<>();
    private ArrayList<Object> _usuario = new ArrayList<>();

    private boolean campo_nombre, campo_apellido_paterno, campo_apellido_materno,
            campo_correo, campo_contrasenia, campo_repertir_contrasenia, campo_calle, campo_numero, campo_codigo_postal, campo_telefono;

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
    String nom, apaterno, amaterno, correo, contraseña = null, calle, num, telefono, cp;

    @FXML
    void Regresar() {
        transiciones.CrearAnimacionFade(500, rootPane, View.MENU_USUARIO);
    }

    /*
       Validaciones de los campos
    */
    @FXML
    void validar_campo_nombre() {
        if (Campo_nombre.getText().matches("^[a-zA-Z]+\\s?[a-zA-Z]*$") && !Campo_nombre.getText().isEmpty()) {
            campo_nombre = true;
            Campo_nombre.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_nombre.setStyle("-fx-border-color: red");
            campo_nombre = false;
        }
    }

    @FXML
    void validar_campo_apellido_paterno() {
        if (Campo_apellido_paterno.getText().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]{3,10}$") && !Campo_apellido_paterno.getText().isEmpty()) {
            campo_apellido_paterno = true;
            Campo_apellido_paterno.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_apellido_paterno.setStyle("-fx-border-color: red");
            campo_apellido_paterno = false;
        }
    }

    @FXML
    void validar_campo_apellido_materno() {
        if (Campo_apellido_materno.getText().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]{3,10}$") && !Campo_apellido_materno.getText().isEmpty()) {
            campo_apellido_materno = true;
            Campo_apellido_materno.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_apellido_materno.setStyle("-fx-border-color: red");
            campo_apellido_materno = false;
        }
    }



    @FXML
    void validar_campo_correo() {
        if (Campo_correo.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") && !Campo_correo.getText().isEmpty()) {
            campo_correo = true;
            Campo_correo.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_correo.setStyle("-fx-border-color: red");
            campo_correo = false;
        }
    }

    @FXML
    void validar_campo_contrasenia() {
        if (Campo_contrasenia.getText().matches("^[A-Za-z\\d@$!%*?&]{6,12}$") && !Campo_contrasenia.getText().isEmpty()) {
            campo_contrasenia = true;
            Campo_contrasenia.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_contrasenia.setStyle("-fx-border-color: red");
            campo_contrasenia = false;
        }
    }

    @FXML
    void validar_campo_repetir_contrasenia() {
        if (Campo_repite_contrasenia.getText().matches("^[A-Za-z\\d@$!%*?&]{6,12}$") && !Campo_repite_contrasenia.getText().isEmpty()) {
            campo_repertir_contrasenia = true;
            Campo_repite_contrasenia.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_repite_contrasenia.setStyle("-fx-border-color: red");
            campo_repertir_contrasenia = false;
        }
    }

    @FXML
    void validar_campo_calle() {
        if (Campo_calle.getText().matches("^[A-Za-z0-9áéíóúÁÉÍÓÚñÑ.,\\-/ ]+$") && !Campo_calle.getText().isEmpty()) {
            campo_calle = true;
            Campo_calle.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_calle.setStyle("-fx-border-color: red");
            campo_calle = false;
        }
    }

    @FXML
    void validar_campo_numero() {
        if (Campo_numero.getText().matches("^[0-9]{1,5}$") && !Campo_numero.getText().isEmpty()) {
            campo_numero = true;
            Campo_numero.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_numero.setStyle("-fx-border-color: red");
            campo_numero = false;
        }
    }

    @FXML
    void validar_campo_codigo_postal() {
        if (Campo_codigo_postal.getText().matches("^[0-9]{1,5}$") && !Campo_codigo_postal.getText().isEmpty()) {
            campo_codigo_postal = true;
            Campo_codigo_postal.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_codigo_postal.setStyle("-fx-border-color: red");
            campo_codigo_postal = false;
        }
    }

    @FXML
    void validar_campo_telefono() {
        if (Campo_telefono.getText().matches("^[0-9]{5,10}$") && !Campo_telefono.getText().isEmpty()) {
            campo_telefono = true;
            Campo_telefono.setStyle("-fx-border-color: #4a97f0");
        } else {
            Campo_telefono.setStyle("-fx-border-color: red");
            campo_telefono = false;
        }
    }



    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){
        _estados = controladorBD.devolverEstados();
        Combo_estado.getItems().addAll(FXCollections.observableArrayList(_estados));
        Combo_municipio.setPromptText("Selecciona un estado");
        if (ControladorMenuAdmin.DESDE_MENU_ADMIN){
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
            contraseña = retornar_contrasenia(_usuario.get(4).toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        calle = _usuario.get(8).toString();
        num = _usuario.get(9).toString();
        telefono = _usuario.get(5).toString();
        String cp = _usuario.get(16).toString();
        Campo_nombre.setText(nom);
        Campo_apellido_paterno.setText(apaterno);
        Campo_apellido_materno.setText(amaterno);
        Campo_correo.setText(correo);
        Campo_contrasenia.setText(contraseña);
        Campo_repite_contrasenia.setText(contraseña);
        Campo_calle.setText(calle);
        Campo_numero.setText(num);
        Campo_telefono.setText(telefono);
        Campo_codigo_postal.setText(cp);

    }

    @FXML
    void mostrarMunicipio(){
        _municipios.clear();
        Combo_municipio.getItems().clear();
        Combo_municipio.setPromptText("Selecciona un estado");
        IconoCarga.setVisible(true);
        IconoCarga.setProgress(-1.0);
        rootPane.setOpacity(0.5);

        Task<ArrayList<String>> traer_municipios = new Task<ArrayList<String>>() {
            @Override
            protected ArrayList<String> call() throws Exception {
                ArrayList<String> result = controladorBD.devolverMunicipios(Combo_estado.getValue());
                return result;
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
        String contraseniaDecifrada = ControladorCifrarContrasena.decrypt(contraseniaCifrada);
        return contraseniaDecifrada;
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
                alertas.CrearAlerta(ControladorAlertas.ALERTA_USUARIO_EDITADO, rootPane);
            } else {
                alertas.CrearAlerta(ControladorAlertas.ALERTA_CORREO_DUPLICADO, rootPane);
            }
        } else {
            alertas.CrearAlerta(ControladorAlertas.ALERTA_CONTRASENIAS_DIFERENTES, rootPane);
        }

    }



}
