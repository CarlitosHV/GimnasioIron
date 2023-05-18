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

    public static String CORREO_USUARIO;
    public static boolean DESDE_MENU_ADMIN = false;

    ControladorTransiciones transiciones = new ControladorTransiciones();
    IndexApp indexApp = new IndexApp();
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

    @FXML
    void ActivarModoOscuro() {
        if (IndexApp.Tema == 1) {
            ViewSwitcher.switchTo(View.MENU_ADMINISTRADOR, ViewSwitcher.MODO_CLARO);
            IndexApp.Tema = 0;
            indexApp.EscribirPropiedades("theme", String.valueOf(IndexApp.Tema));
        } else if (IndexApp.Tema == 0) {
            ViewSwitcher.switchTo(View.MENU_ADMINISTRADOR, ViewSwitcher.MODO_OSCURO);
            IndexApp.Tema = 1;
            indexApp.EscribirPropiedades("theme", String.valueOf(IndexApp.Tema));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _clientes = bd.devolverClientes();
        ListView.setCellFactory(lv -> {
            ClienteCell cell = new ClienteCell();
            cell.setOnItemSelected(event -> {
                ClaseClientes clienteSeleccionado = (ClaseClientes) event.getSource();
                CORREO_USUARIO = clienteSeleccionado.getCorreo();
                if (CORREO_USUARIO != null){
                    DESDE_MENU_ADMIN = true;
                    transiciones.CrearAnimacionFade(500, rootPane, View.EDITAR);
                }
            });

            return cell;
        });
        ListView.setItems(FXCollections.observableArrayList(_clientes));
    }
}
