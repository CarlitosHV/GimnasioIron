package com.gimnasio.ironbodiesgym;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ListItemController implements Initializable {

    @FXML
    private Button BotonEditar, BotonEliminar;

    @FXML
    private Label LabelNombre, LabelCorreo, LabelPlan, LabelFecha;


    void Editar(){
        System.out.println("Haz pulsado el editar");
    }

    void Eliminar(){
        System.out.println("Haz pulsado el eliminar");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BotonEditar.setOnAction(actionEvent -> {
            Editar();
        });


        BotonEliminar.setOnAction(actionEvent -> {
            Eliminar();
        });
    }
}
