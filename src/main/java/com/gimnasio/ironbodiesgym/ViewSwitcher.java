package com.gimnasio.ironbodiesgym;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewSwitcher {

    private static Scene scene;

    private static Map<View, Parent> cache = new HashMap<>();

    public static void setScene(Scene scene) {
        ViewSwitcher.scene = scene;
    }

    public static void switchTo(View view){
        if (scene == null){
            System.out.println("No hay escena configurada");
            return;
        }
        try {
            Parent root;
            if (cache.containsKey(view)){
                System.out.println("Cargando desde el caché");
                root = cache.get(view);
            }else{
                System.out.println("Cargando desde el FXML");
                root = FXMLLoader.load(ViewSwitcher.class.getResource(view.getFileName()));
                cache.put(view, root);
            }
            scene.setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
