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
            ALERTA_CONTRASENIAS_DIFERENTES = 3;

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
        }
    }


    void aplicarTemaAlerta(String titulo, String contenido, int tipo, Node Nodo) throws SQLException {
        Alert alert;
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        DialogPane dialogPane = alert.getDialogPane();
        Label content = new Label(alert.getContentText());
        alert.setHeaderText(null);
        alert.setTitle(titulo);
        content.setText(contenido);
        if (IndexApp.Tema == 0) {
            dialogPane.setStyle("-fx-background-color: white; -fx-text-fill: white");
            content.setTextFill(Color.BLACK);
            alert.getDialogPane().setContent(content);
        } else {
            dialogPane.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white");
            content.setTextFill(Color.WHITESMOKE);
            alert.getDialogPane().setContent(content);
        }
        if (tipo == 0) {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                alert.close();
                transiciones.CrearAnimacionFade(500, Nodo, View.LOGIN);
            }
        }else{
            alert.showAndWait();
        }
    }
}
