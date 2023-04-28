package com.gimnasio.ironbodiesgym;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;

public class ControladorBD {

    Connection conn = null;
    ControladorCifrarContrasena cifrarContrasena = new ControladorCifrarContrasena();

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
            ResultSet resultSet =selectStmt.executeQuery();
            boolean usuarioexistente = !resultSet.next();

            if (usuarioexistente){
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
                login.add(resultSet.getString("correo"));
                login.add(resultSet.getString("contrasenia"));
                login.add(resultSet.getBoolean("usuario_administrador"));
                login.add(resultSet.getBoolean("bloqueado"));
                ControladorLogin.contra = resultSet.getString("contrasenia");
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
}

