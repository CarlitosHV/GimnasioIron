package com.gimnasio.ironbodiesgym;

import java.util.Date;

public class ClaseClientes {

    public int id_usuario;
    public String nombre;
    public String apellido_paterno;
    public String apellido_materno;
    public String correo;
    public String tipo_suscripcion;
    public Date fecha_termino;

    public ClaseClientes() {
    }

    public ClaseClientes(int id_usuario, String nombre, String apellido_paterno, String apellido_materno,
                         String correo, String tipo_suscripcion, Date fecha_termino) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.correo = correo;
        this.tipo_suscripcion = tipo_suscripcion;
        this.fecha_termino = fecha_termino;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido_paterno() {
        return apellido_paterno;
    }

    public void setApellido_paterno(String apellido_paterno) {
        this.apellido_paterno = apellido_paterno;
    }

    public String getApellido_materno() {
        return apellido_materno;
    }

    public void setApellido_materno(String apellido_materno) {
        this.apellido_materno = apellido_materno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipo_suscripcion() {
        return tipo_suscripcion;
    }

    public void setTipo_suscripcion(String tipo_suscripcion) {
        this.tipo_suscripcion = tipo_suscripcion;
    }

    public Date getFecha_termino() {
        return fecha_termino;
    }

    public void setFecha_termino(Date fecha_termino) {
        this.fecha_termino = fecha_termino;
    }
}
