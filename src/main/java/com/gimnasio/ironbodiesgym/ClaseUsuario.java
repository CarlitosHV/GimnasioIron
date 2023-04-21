package com.gimnasio.ironbodiesgym;

public class ClaseUsuario {

    String nombre;
    String apellido_paterno;
    String Apellido_materno;
    String correo;
    String contrasenia;
    int telefono;
    boolean usuario_administrador;
    String calle;
    int numero;
    int codigo_postal;
    String municipio;
    String estado;
    int edad;
    String sexo;
    boolean bloqueado;
    boolean estado_suscripcion;

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
        return Apellido_materno;
    }

    public void setApellido_materno(String apellido_materno) {
        this.Apellido_materno = apellido_materno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public boolean isUsuario_administrador() {
        return usuario_administrador;
    }

    public void setUsuario_administrador(boolean usuario_administrador) {
        this.usuario_administrador = usuario_administrador;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(int codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public boolean isEstado_suscripcion() {
        return estado_suscripcion;
    }

    public void setEstado_suscripcion(boolean estado_suscripcion) {
        this.estado_suscripcion = estado_suscripcion;
    }

    public ClaseUsuario() {
    }

    public ClaseUsuario(String nombre, String apellido_paterno, String Apellido_materno, String correo, String contrasenia, int telefono,
                        boolean usuario_administrador, String calle, int numero, int codigo_postal, String municipio, String estado,
                        int edad, String sexo, boolean bloqueado, boolean estado_suscripcion) {
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.Apellido_materno = Apellido_materno;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.telefono = telefono;
        this.usuario_administrador = usuario_administrador;
        this.calle = calle;
        this.numero = numero;
        this.codigo_postal = codigo_postal;
        this.municipio = municipio;
        this.estado = estado;
        this.edad = edad;
        this.sexo = sexo;
        this.bloqueado = bloqueado;
        this.estado_suscripcion = estado_suscripcion;
    }
}
