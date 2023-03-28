package com.gimnasio.ironbodiesgym;

public enum View {
    LOGIN("VistaLogin.fxml"),
    CREAR_USUARIO("VistaCrearUsuario.fxml");

    private String fileName;

    View(String fileName){
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}