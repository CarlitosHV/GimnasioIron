package com.gimnasio.ironbodiesgym;

public enum View {
    LOGIN("VistaLogin.fxml"),
    CREAR_USUARIO("VistaCrearUsuario.fxml"),
    RENOVAR_SUSCRIPCIONES("VistaRenovarSuscripciones.fxml"),
    RECUPERAR_CONTRASENA("VistaRecuperarContrasena.fxml"),
    MENU_ADMINISTRADOR("VistaMenuAdmin.fxml"),
    MENU_USUARIO("VistaMenuUsuario.fxml"),
    CONSULTA_USUARIO("VistaConsultaUsuario.fxml");

    private String fileName;

    View(String fileName){
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
