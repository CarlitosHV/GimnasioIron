package com.gimnasio.ironbodiesgym;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

public class IndexApp extends Application {

    InputStream configInput = null;
    OutputStream configOutput = null;
    public static int Tema;
    public static String servidor, usuario, contrasenia, base_datos, key;


    //Método que inicia la aplicación
    public static void main(String[] args){
        launch(args);
    }

    //Método que comienza y manda a crear el stage
    @Override
    public void start(Stage stage) {
        ObtenerPropiedades();
        var scene = new Scene(new Pane());
        ViewSwitcher.setScene(scene);
        //Extraemos la variable del tema del archivo
        ViewSwitcher.switchTo(View.LOGIN, Tema);
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(IndexApp.class.getResourceAsStream("/assets/IconGym.png"))));
        stage.setTitle("Iron Bodies Gym");
        stage.show();
    }

    public void ObtenerPropiedades() {
        try {
            Properties config = new Properties();
            configInput = new FileInputStream("src/main/resources/config.properties");
            config.load(configInput);
            Tema = Integer.parseInt(config.getProperty("theme"));
            servidor = config.getProperty("server");
            usuario = config.getProperty("user");
            contrasenia = config.getProperty("password");
            base_datos = config.getProperty("database");
            key = config.getProperty("key");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void EscribirPropiedades(String property, String value) {
        try {
            configOutput = new FileOutputStream("src/main/resources/config.properties");
            Properties config = new Properties();
            config.setProperty(property, value);
            config.setProperty("server", servidor);
            config.setProperty("user", usuario);
            config.setProperty("password", contrasenia);
            config.setProperty("database", base_datos);
            config.setProperty("key", key);
            config.store(configOutput, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Holaaa

}
