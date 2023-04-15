package com.gimnasio.ironbodiesgym;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ControladorBD {

    Connection conn = null;

    public void ConexionBD() {
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/test?" +
                            "user=root&password=");
            if (conn.isValid(500)) {
                System.out.println("Conexión establecida");
            }
            // Do something with the Connection
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}

