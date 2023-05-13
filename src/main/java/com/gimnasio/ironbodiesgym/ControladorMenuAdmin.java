package com.gimnasio.ironbodiesgym;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControladorMenuAdmin implements Initializable {

    @FXML
    private GridPane rootPane;
    @FXML
    private ListView<ClaseClientes> ListView;

    ControladorTransiciones transiciones = new ControladorTransiciones();
    ControladorBD bd = new ControladorBD();


    /*
    Arreglo que contiene la lista de los clientes
     */
    public static ArrayList<ClaseClientes> _clientes = new ArrayList<>();


    @FXML
    void Abrir_informacion(){
        transiciones.CrearAnimacionFade(500, rootPane, View.CONSULTA_USUARIO);
    }

    @FXML
    void Regresar(){
        transiciones.CrearAnimacionFade(500, rootPane, View.LOGIN);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _clientes = bd.devolverClientes();
        ListView.setCellFactory(lv -> new ClienteCell());
        ListView.setItems(FXCollections.observableArrayList(_clientes));

    }
}
