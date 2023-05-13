package com.gimnasio.ironbodiesgym;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ClienteCell extends ListCell<ClaseClientes> {

    private FXMLLoader fxmlLoader;
    private GridPane fondoItem;
    private Label labelNombre, labelCorreo, labelPlan, labelFecha;
    private Button botonEditar, botonEliminar;

    public ClienteCell() {
        super();
        fxmlLoader = new FXMLLoader(getClass().getResource("ListViewItem.fxml"));
        try {
            fondoItem = fxmlLoader.load();
            labelNombre = (Label) fxmlLoader.getNamespace().get("LabelNombre");
            labelCorreo = (Label) fxmlLoader.getNamespace().get("LabelCorreo");
            labelPlan = (Label) fxmlLoader.getNamespace().get("LabelPlan");
            labelFecha = (Label) fxmlLoader.getNamespace().get("LabelFecha");
            botonEditar = (Button) fxmlLoader.getNamespace().get("BotonEditar");
            botonEliminar = (Button) fxmlLoader.getNamespace().get("BotonEliminar");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(ClaseClientes cliente, boolean empty) {
        super.updateItem(cliente, empty);

        if (empty || cliente == null) {
            setGraphic(null);
        } else {
            labelNombre.setText(cliente.getNombre());
            labelCorreo.setText(cliente.getCorreo());
            labelPlan.setText("Suscripción: " + cliente.getTipo_suscripcion());
            labelFecha.setText("Vence: " + String.valueOf(cliente.getFecha_termino()));
            setGraphic(fondoItem);
        }
    }
}
