package org.example;

import org.example.dao.ConexionBD;
import org.example.vista.Menu;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        // Conecta con la BD
        try (Connection _ = ConexionBD.obtenerConexion()) {
            System.out.println("Conexión correcta con PostgreSQL.");
        } catch (Exception e) {
            System.out.println("Error al conectar con la base de datos.");
            System.out.println(e.getMessage());
            return;
        }

        // Lanza el menú
        Menu menu = new Menu();
        menu.mostrar();
    }
}
