package com.gimnasio.ironbodiesgym;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class ControladorBD {

    Connection conn = null;

    public boolean insertar_Usuario(String nombre, String apellido_paterno, String Apellido_materno, String correo,
                                    String contrasenia, BigInteger telefono, boolean usuario_administrador, String calle,
                                    int numero, int codigo_postal, String municipio, String estado, int edad, String sexo, boolean bloqueado,
                                    boolean estado_suscripcion) {
        try {
            CallableStatement stmt = null;
            conn =
                    DriverManager.getConnection("jdbc:mysql://" + IndexApp.servidor + "/" + IndexApp.base_datos + "?" +
                            "user=" + IndexApp.usuario + "&password=" + IndexApp.contrasenia);

            String consulta = "SELECT * FROM USUARIOS WHERE correo = '" + correo + "';";
            PreparedStatement selectStmt = conn.prepareStatement(consulta);
            ResultSet resultSet = selectStmt.executeQuery();
            boolean usuarioexistente = !resultSet.next();

            if (usuarioexistente) {
                String sql = "{call insertar_usuario (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
                stmt = conn.prepareCall(sql);
                //Campos a mandar
                stmt.setString(1, nombre);
                stmt.setString(2, apellido_paterno);
                stmt.setString(3, Apellido_materno);
                stmt.setString(4, correo);
                stmt.setString(5, contrasenia);
                stmt.setBigDecimal(6, new BigDecimal(telefono));
                stmt.setBoolean(7, usuario_administrador);
                stmt.setString(8, calle);
                stmt.setInt(9, numero);
                stmt.setInt(10, codigo_postal);
                stmt.setString(11, municipio);
                stmt.setString(12, estado);
                stmt.setInt(13, edad);
                stmt.setString(14, sexo);
                stmt.setBoolean(15, bloqueado);
                stmt.setBoolean(16, estado_suscripcion);

                stmt.execute();
                stmt.close();
            }

            conn.close();
            return usuarioexistente;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
    }

    public ArrayList<Object> loginUsuario(String correo) {
        ArrayList<Object> login = new ArrayList<>();
        try {
            CallableStatement stmt = null;
            conn =
                    DriverManager.getConnection("jdbc:mysql://" + IndexApp.servidor + "/" + IndexApp.base_datos + "?" +
                            "user=" + IndexApp.usuario + "&password=" + IndexApp.contrasenia);


            String sql = "{call login_usuario (?)}";
            stmt = conn.prepareCall(sql);

            //Campos a mandar
            stmt.setString(1, correo);

            stmt.execute();

            ResultSet resultSet = stmt.getResultSet();


            while (resultSet.next()) {
                login.add(resultSet.getString("nombre"));
                login.add(resultSet.getString("apellido_paterno"));
                login.add(resultSet.getString("apellido_materno"));
                login.add(resultSet.getString("correo"));
                login.add(resultSet.getString("contrasenia"));
                login.add(resultSet.getString("telefono"));
                login.add(resultSet.getBoolean("usuario_administrador"));
                login.add(resultSet.getBoolean("bloqueado"));
                login.add(resultSet.getString("calle"));
                login.add(resultSet.getInt("numero"));
                login.add(resultSet.getString("municipio"));
                login.add(resultSet.getString("estado"));
                login.add(resultSet.getString("edad"));
                login.add(resultSet.getString("sexo"));
                login.add(resultSet.getBoolean("estado_suscripcion"));
                login.add(resultSet.getInt("id_usuario"));
            }

            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        if (!login.isEmpty()) {
            return login;
        } else {
            return null;
        }
    }

    public boolean cambiarContrasenia(String correo, String contraseniaNueva) {
        try {
            CallableStatement stmt = null;
            conn =
                    DriverManager.getConnection("jdbc:mysql://" + IndexApp.servidor + "/" + IndexApp.base_datos + "?" +
                            "user=" + IndexApp.usuario + "&password=" + IndexApp.contrasenia);


            String sql = "{call actualizar_contrasenia (?, ?)}";
            stmt = conn.prepareCall(sql);
            //Campos a mandar
            stmt.setString(1, correo);
            stmt.setString(2, contraseniaNueva);

            PreparedStatement statement = conn.prepareStatement("SELECT contrasenia FROM USUARIOS WHERE contrasenia = '" + contraseniaNueva + "';");
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            boolean actualizado = !resultSet.next();


            stmt.execute();
            stmt.close();
            conn.close();

            return actualizado;

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
    }

    public boolean validarCorreo(String correo) {
        try {
            CallableStatement stmt = null;
            conn =
                    DriverManager.getConnection("jdbc:mysql://" + IndexApp.servidor + "/" + IndexApp.base_datos + "?" +
                            "user=" + IndexApp.usuario + "&password=" + IndexApp.contrasenia);


            String sql = "{call consultar_cuenta (?)}";
            stmt = conn.prepareCall(sql);

            //Campos a mandar
            stmt.setString(1, correo);

            stmt.execute();

            ResultSet resultSet = stmt.getResultSet();

            boolean encontrado = resultSet.next();

            stmt.close();
            conn.close();

            return encontrado;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
    }

    public ArrayList<String> devolverPlanes(){
        try {
            ArrayList<String> planes = new ArrayList<>();
            conn =
                    DriverManager.getConnection("jdbc:mysql://" + IndexApp.servidor + "/" + IndexApp.base_datos + "?" +
                            "user=" + IndexApp.usuario + "&password=" + IndexApp.contrasenia);

            CallableStatement stmt = conn.prepareCall("{call obtener_planes()}");

            stmt.execute();

            ResultSet resultSet = stmt.getResultSet();

            while (resultSet.next()){
                planes.add(resultSet.getString("nombre"));
            }

            stmt.close();
            conn.close();
            return planes;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
    }

    public ArrayList<ClaseClientes> devolverClientes(){
        ArrayList<ClaseClientes> _informacion = new ArrayList<>();
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://" + IndexApp.servidor + "/" + IndexApp.base_datos + "?" +
                            "user=" + IndexApp.usuario + "&password=" + IndexApp.contrasenia);

            CallableStatement stmt = conn.prepareCall("{call consultar_clientes()}");

            stmt.execute();

            ResultSet resultSet = stmt.getResultSet();

            while (resultSet.next()){
                int id = resultSet.getInt("id_usuario");
                String nombre = resultSet.getString("nombre");
                String apellido_paterno = resultSet.getString("apellido_paterno");
                String apellido_materno = resultSet.getString("apellido_materno");
                String correo = resultSet.getString("correo");
                String tipo_suscripcion = resultSet.getString("tipo_suscripcion");
                Date fecha_termino = resultSet.getDate("fecha_termino");

                ClaseClientes clientes = new ClaseClientes(id, nombre, apellido_paterno, apellido_materno,
                        correo, tipo_suscripcion, fecha_termino);

                _informacion.add(clientes);

            }

            stmt.close();
            conn.close();
            return _informacion;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
    }

    public ArrayList<Object> devolverSuscripcion(int id_usuario){
        try {
            ArrayList<Object> suscripcion = new ArrayList<>();
            conn =
                    DriverManager.getConnection("jdbc:mysql://" + IndexApp.servidor + "/" + IndexApp.base_datos + "?" +
                            "user=" + IndexApp.usuario + "&password=" + IndexApp.contrasenia);

            CallableStatement stmt = conn.prepareCall("{call consultar_suscripcion(?)}");

            stmt.setInt(1, id_usuario);

            stmt.execute();

            ResultSet resultSet = stmt.getResultSet();

            while (resultSet.next()){
                suscripcion.add(resultSet.getString("tipo_suscripcion"));
                suscripcion.add(resultSet.getDate("fecha_inicio"));
                suscripcion.add(resultSet.getDate("fecha_termino"));
            }

            stmt.close();
            conn.close();
            return suscripcion;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
    }

    public boolean insertar_suscripcion(int id_usuario, String tipo_suscripcion, Date fecha_inicio, Date fecha_termino, float pago){
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://" + IndexApp.servidor + "/" + IndexApp.base_datos + "?" +
                            "user=" + IndexApp.usuario + "&password=" + IndexApp.contrasenia);

            String consulta = "SELECT * FROM SUSCRIPCIONES WHERE id_usuario = '" + id_usuario + "' AND fecha_termino > NOW();";
            PreparedStatement selectStmt = conn.prepareStatement(consulta);
            ResultSet resultSet = selectStmt.executeQuery();
            boolean suscripcionexistente = !resultSet.next();

            if (suscripcionexistente){
                CallableStatement stmt = conn.prepareCall("{call insertar_suscripcion(?,?,?,?,?)}");
                stmt.setInt(1, id_usuario);
                stmt.setString(2, tipo_suscripcion);
                stmt.setDate(3, fecha_inicio);
                stmt.setDate(4, fecha_termino);
                stmt.setFloat(5, pago);
                stmt.execute();
                stmt.close();
            }
            conn.close();
            return suscripcionexistente;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
    }
}

