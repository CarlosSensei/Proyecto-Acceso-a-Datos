package org.example.dao;

import java.sql.*;

public class DirectorDAO {

    public void listar() {

        // Query a la BD
        String sql = "SELECT id_director, nombre, nacionalidad FROM directores ORDER BY id_director";

        // Conecta y lista los directores
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id_director") + " | " +
                                rs.getString("nombre") + " | " +
                                rs.getString("nacionalidad")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al listar directores: " + e.getMessage());
        }
    }
}