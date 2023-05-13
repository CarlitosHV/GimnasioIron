package com.gimnasio.ironbodiesgym;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ControladorRenovarSuscripciones implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ComboBox<String> ComboPlanes, ComboTiempo;
    @FXML
    private Label LabelCosto, LabelNoSuscripcion, Renovar_suscripcion;
    ArrayList<String> planes = new ArrayList<>();
    ControladorBD bd = new ControladorBD();
    ControladorTransiciones transiciones = new ControladorTransiciones();
    ControladorAlertas alertas = new ControladorAlertas();
    LocalDate fecha_inicio = LocalDate.now();
    LocalDate fecha_termino;
    int id = (int) ControladorLogin.loginuser.get(15);
    float pago;

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
                    fecha_termino = fecha_inicio.plusMonths(1);
                    if (ComboPlanes.getValue().equals("Básico")) {
                        LabelCosto.setText("$400");
                    } else {
                        LabelCosto.setText("$500");
                    }
                }
                case "3 Meses" -> {
                    LabelCosto.setText("");
                    fecha_termino = fecha_inicio.plusMonths(3);
                    if (ComboPlanes.getValue().equals("Básico")) {
                        pago = 700;
                        LabelCosto.setText("$" + pago);
                    } else {
                        pago = 800;
                        LabelCosto.setText("$" + pago);
                    }
                }
                case "6 Meses" -> {
                    LabelCosto.setText("");
                    fecha_termino = fecha_inicio.plusMonths(6);
                    if (ComboPlanes.getValue().equals("Básico")) {
                        pago = 1400;
                        LabelCosto.setText("$" + pago);
                    } else {
                        pago = 1500;
                        LabelCosto.setText("$" + pago);
                    }
                }
                case "1 Año" -> {
                    LabelCosto.setText("");
                    fecha_termino = fecha_inicio.plusMonths(12);
                    if (ComboPlanes.getValue().equals("Básico")) {
                        pago = 2200;
                        LabelCosto.setText("$" + pago);
                    } else {
                        pago = 2300;
                        LabelCosto.setText("$" + pago);
                    }
                }
            }
        }
    }


    @FXML
    void Regresar() {
        transiciones.CrearAnimacionFade(500, rootPane, View.MENU_USUARIO);
    }

    @FXML
    void Guardar() throws SQLException {
        String plan = ComboPlanes.getValue();
        if (!ComboPlanes.getValue().equals("Selecciona") && !ComboTiempo.getValue().equals("Selecciona")){
            boolean creada = bd.insertar_suscripcion(id, plan, Date.valueOf(fecha_inicio), Date.valueOf(fecha_termino), pago);
            if (creada){
                alertas.CrearAlerta(ControladorAlertas.ALERTA_SUSCRIPCION_CREADA, rootPane);
            }else{
                alertas.CrearAlerta(ControladorAlertas.ALERTA_ERROR_BD, rootPane);
            }
        }else{
            alertas.CrearAlerta(ControladorAlertas.ALERTA_ERROR_CAMPOS, rootPane);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        planes = bd.devolverPlanes();
        ComboPlanes.getItems().addAll(planes);
    }
}
