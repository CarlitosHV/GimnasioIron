package com.gimnasio.ironbodiesgym;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControladorRenovarSuscripciones implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ComboBox<String> ComboPlanes, ComboTiempo;
    @FXML
    private Label LabelCosto;
    ArrayList<String> planes = new ArrayList<>();
    ControladorBD bd = new ControladorBD();
    ControladorTransiciones transiciones = new ControladorTransiciones();

    @FXML
    void mostrar_meses() {
        LabelCosto.setText("");
        ComboTiempo.getItems().clear();
        ComboTiempo.setPromptText("Selecciona un plan");
        if (!ComboPlanes.getValue().equals("Selecciona")) {
            ComboTiempo.getItems().addAll("1 Mes", "3 Meses", "6 Meses", "1 Año");
        }
    }

    @FXML
    void mostrar_precios() {
        if (!ComboTiempo.getItems().isEmpty()) {
            switch (ComboTiempo.getValue()) {
                case "1 Mes" -> {
                    LabelCosto.setText("");
                    if (ComboPlanes.getValue().equals("Básico")) {
                        LabelCosto.setText("$400");
                    } else {
                        LabelCosto.setText("$500");
                    }
                }
                case "3 Meses" -> {
                    LabelCosto.setText("");
                    if (ComboPlanes.getValue().equals("Básico")) {
                        LabelCosto.setText("$700");
                    } else {
                        LabelCosto.setText("$800");
                    }
                }
                case "6 Meses" -> {
                    LabelCosto.setText("");
                    if (ComboPlanes.getValue().equals("Básico")) {
                        LabelCosto.setText("$1400");
                    } else {
                        LabelCosto.setText("$1500");
                    }
                }
                case "1 Año" -> {
                    LabelCosto.setText("");
                    if (ComboPlanes.getValue().equals("Básico")) {
                        LabelCosto.setText("$2200");
                    } else {
                        LabelCosto.setText("$2300");
                    }
                }
            }
        }
    }


    @FXML
    void Regresar() {
        transiciones.CrearAnimacionFade(500, rootPane, View.MENU_USUARIO);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        planes = bd.devolverPlanes();
        ComboPlanes.getItems().addAll(planes);
    }
}
