package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Define la BD
    private static final String URL = "jdbc:postgresql://localhost:5432/CinefrikiDB";
    private static final String USUARIO = "iCarlos";
    private static final String PASSWORD = "";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
