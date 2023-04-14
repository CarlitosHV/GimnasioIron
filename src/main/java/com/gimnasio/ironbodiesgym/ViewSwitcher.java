package com.gimnasio.ironbodiesgym;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;


public class ViewSwitcher {

    private static Scene scene;

    public static int MODO_CLARO = 0;
    public static int MODO_OSCURO = 1;

    public static void setScene(Scene scene) {
        ViewSwitcher.scene = scene;
    }

    public static void switchTo(View view, int theme){
        if (scene == null){
            System.out.println("No hay escena configurada");
            return;
        }
        try {
            Parent root;
            root = FXMLLoader.load(ViewSwitcher.class.getResource(view.getFileName()));
            if (theme == MODO_CLARO){
                scene.getStylesheets().clear();
                scene.getStylesheets().add(IndexApp.class.getResource("/css/ModoClaro.css").toExternalForm());
            }else if (theme == MODO_OSCURO){
                scene.getStylesheets().clear();
                scene.getStylesheets().add(IndexApp.class.getResource("/css/ModoOscuro.css").toExternalForm());
            }
            scene.setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
