package com.gimnasio.ironbodiesgym;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControladorMenuAdmin implements Initializable {

    @FXML
    private GridPane rootPane;
    @FXML
    private ListView<ClaseClientes> ListView;
    @FXML
    private TextField Buscador;
    @FXML
    private Button Boton_buscar;
    @FXML
    private ProgressIndicator IconoCarga;
    @FXML
    private Label LabelAgregar;

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

    void buscar(String palabra){
        IconoCarga.setVisible(true);
        IconoCarga.setProgress(-1.0);

        Task<ArrayList<ClaseClientes>> traer_busqueda = new Task<ArrayList<ClaseClientes>>() {
            @Override
            protected ArrayList<ClaseClientes> call() throws Exception {
                return bd.buscarClientes(palabra);
            }
        };

        new Thread(traer_busqueda).start();


        traer_busqueda.setOnSucceeded(event -> {
            Buscador.setEditable(true);
            IconoCarga.setVisible(false);
            if (!_clientes.isEmpty()){
                _clientes.clear();
            }
            ListView.getItems().clear();
            _clientes = traer_busqueda.getValue();
            ListView.setItems(FXCollections.observableArrayList(_clientes));
        });

        if(traer_busqueda.isRunning()){
            Buscador.setEditable(false);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setMinWidth(850);
            stage.setMinHeight(600);
            _clientes = bd.devolverClientes();
            IconoCarga.setVisible(false);
            ListView.setCellFactory(lv -> {
                ClienteCell cell = new ClienteCell();
                cell.setOnItemSelected(event -> {
                    ClaseClientes clienteSeleccionado = (ClaseClientes) event.getSource();
                    CORREO_USUARIO = clienteSeleccionado.getCorreo();
                    if (CORREO_USUARIO != null){
                        ControladorEditarDatos.ADMINISTRADOR = true;
                        transiciones.CrearAnimacionFade(500, rootPane, View.EDITAR);
                    }
                });
                return cell;
            });
            ListView.setItems(FXCollections.observableArrayList(_clientes));

            Boton_buscar.setOnAction(actionEvent -> buscar(Buscador.getText()));

            Buscador.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.length() > 0){
                    buscar(newValue);
                }else{
                    if (!_clientes.isEmpty()){
                        _clientes.clear();
                    }
                    ListView.getItems().clear();
                    _clientes = bd.devolverClientes();
                    ListView.setItems(FXCollections.observableArrayList(_clientes));
                }
            });

            LabelAgregar.setOnMouseClicked(evt -> {
                ControladorCrearUsuario.ADMINISTRADOR = true;
                transiciones.CrearAnimacionFade(500, rootPane, View.CREAR_USUARIO);
            });

        });
    }
}
