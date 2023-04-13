package com.gimnasio.ironbodiesgym;

public enum View {
    LOGIN("VistaLogin.fxml"),
    CREAR_USUARIO("VistaCrearUsuario.fxml"),
<<<<<<< HEAD

    RENOVAR_SUSCRIPCIONES("VistaRenovarSuscripciones.fxml");
=======
    RECUPERAR_CONTRASENA("VistaRecuperarContrasena.fxml");
>>>>>>> 1fd2400260384eb98c846309cccbf4f240135bec

    private String fileName;

    View(String fileName){
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
