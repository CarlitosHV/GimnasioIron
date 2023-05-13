package com.gimnasio.ironbodiesgym;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.Optional;

public class ControladorAlertas {

    public static int ALERTA_USUARIO_CREADO = 0, ALERTA_CORREO_DUPLICADO = 1, ALERTA_ERROR_CAMPOS = 2,
            ALERTA_CONTRASENIAS_DIFERENTES = 3, ALERTA_CORREO_NO_ENCONTRADO = 4, ALERTA_CONTRASENIA_ACTUALIZADA = 5,
            ALERTA_ERROR_BD = 6, ALERTA_USUARIO_BLOQUEADO = 7, ALERTA_USUARIO_INVALIDO = 8, ALERTA_SUSCRIPCION_CREADA = 9,
            ALERTA_USUARIO_EDITADO = 10;

    ControladorTransiciones transiciones = new ControladorTransiciones();


    public void CrearAlerta(int TIPO_ALERTA, Node Nodo) throws SQLException {
        switch (TIPO_ALERTA) {
            case 0 -> {
                aplicarTemaAlerta("Bienvenido", "Cuenta creada con éxito", ALERTA_USUARIO_CREADO, Nodo);
                ViewSwitcher.switchTo(View.LOGIN, IndexApp.Tema);
            }
            case 1 -> {
                aplicarTemaAlerta("Usuario existente", "El correo ya está registrado", ALERTA_CORREO_DUPLICADO, Nodo);
            }
            case 2 -> aplicarTemaAlerta("Error", "Campos no llenados correctamente, valida la información", ALERTA_ERROR_CAMPOS, Nodo);
            case 3 -> aplicarTemaAlerta("Error en contraseña", "Las contraseñas no coinciden", ALERTA_CONTRASENIAS_DIFERENTES, Nodo);
            case 4 -> aplicarTemaAlerta("Correo inválido", "El correo que ingresaste no existe", ALERTA_CORREO_NO_ENCONTRADO, Nodo);
            case 5 -> aplicarTemaAlerta("Actualización realizada", "La contraseña ha sido actualizada con éxito", 0, Nodo);
            case 6 -> aplicarTemaAlerta("Error de conexión", "Ocurrió un error con la base de datos :(", ALERTA_ERROR_BD, Nodo);
            case 7 -> aplicarTemaAlerta("Usuario bloqueado", "Tu cuenta ha sido bloqueada. Contacta con tu administrador", ALERTA_USUARIO_BLOQUEADO, Nodo);
            case 8 -> aplicarTemaAlerta("Usuario no encontrado", "El correo o contraseña son incorrectos", ALERTA_USUARIO_INVALIDO, Nodo);
            case 9 -> aplicarTemaAlerta("¡Suscripción activa!", "Se ha creado tu suscripción", ALERTA_SUSCRIPCION_CREADA, Nodo);
            case 10 -> aplicarTemaAlerta("¡Usuario editado!", "El usuario ha sido modificado con éxito", ALERTA_USUARIO_EDITADO, Nodo);
        }
    }


    void aplicarTemaAlerta(String titulo, String contenido, int tipo, Node Nodo) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        DialogPane dialogPane = alert.getDialogPane();
        Label content = new Label(alert.getContentText());
        alert.setHeaderText(null);
        alert.setTitle(titulo);
        content.setText(contenido);
        if (IndexApp.Tema == 0) {
            dialogPane.setStyle("-fx-background-color: white;");
            content.setTextFill(Color.BLACK);
            alert.getDialogPane().setContent(content);
        } else {
            dialogPane.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white");
            content.setTextFill(Color.WHITESMOKE);
            alert.getDialogPane().setContent(content);
        }
        Optional<ButtonType> result = alert.showAndWait();
        switch(tipo){
            case 0 -> {
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    alert.close();
                    transiciones.CrearAnimacionFade(500, Nodo, View.LOGIN);
                }
            }
            case 9, 10 -> {
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    alert.close();
                    transiciones.CrearAnimacionFade(500, Nodo, View.MENU_USUARIO);
                }
            }
        }
        alert.showAndWait();
    }
}
