package com.emergentes.utiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionDB {

    static String driver = "com.mysql.jdbc.Driver";
    static String url = "jdbc:mysql://localhost:3306/bd_biblio";
    static String usuario = "root";
    static String password = "";

    Connection conexion = null;

    public ConexionDB() {
        try {
            //especificacion del drive
            Class.forName(driver);
            //establece la conexion a la base de datos 
            conexion = DriverManager.getConnection(url, usuario, password);

            if (conexion != null) {
                System.out.println("conexion establecida" + conexion);

            }
        } catch (ClassNotFoundException ex) {
            System.out.println("error en DRIVER" + ex.getMessage());
        } catch (SQLException e) {
            System.out.println("error en SQL" + e.getMessage());

        }
    }

    public Connection conectar() {
        return conexion;
    }

    public void desconectar() {
        try {
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
