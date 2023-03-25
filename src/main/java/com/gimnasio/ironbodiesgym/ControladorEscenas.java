package com.gimnasio.ironbodiesgym;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.IOException;

public class s {

    private Stage Ventana;
    private BorderPane borderPane;
    private GridPane gridPane;


    public ControladorEscenas(Stage stage){
        this.Ventana = stage;
        CargarVistaPrincipal();
    }

    public void CargarVistaPrincipal(){
        try {
            //Obtenemos la vista con un FXMLLoader
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("VistaPrincipal.fxml"));
            //Al border pane le asignamos la vista anterior que cargamos
            borderPane = (BorderPane) loader.load();
            //Creamos la escena a la cuál le añadimos el border pane
            Scene s = new Scene(borderPane);
            //Al stage instanciado al principio de clase le asignamos la escena
            Ventana.setScene(s);
            Ventana.setTitle("Iron Bodies Gym");
            Ventana.setMinHeight(450);
            Ventana.setMinWidth(600);
            Ventana.getIcons().add(new Image(ControladorEscenas.class.getResourceAsStream("/assets/IconGym.png")));
            //Mostramos el Stage en pantalla
            Ventana.show();
            //Como cada Vista tiene su controlador, al controlador de escenas le indicamos el controlador de la vista
            ControladorEscenas ce = loader.getController();
            //Aquí directamente cargamos la vista de Login al borderpane
            //MostrarLogin();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*public void MostrarLogin(){
        try {
            //Obtenemos la vista con un FXMLLoader
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("VistaLog.fxml"));
            //Aquí depende el tipo de contenedor que se usará, en este caso es un Anchorpane
            gridPane = (GridPane) loader.load();
            //Seteamos la vista en el centro del Borderpane
            borderPane.setCenter(gridPane);
            //Le asignamos el controlador a la vista
            ControladorLogin cl = loader.getController();
            //Al controlador le asignamos el controlador de escena, ya que tenemos sus getters and setters en la clase
            cl.setControladorEscenas(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
}
