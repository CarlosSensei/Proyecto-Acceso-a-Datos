package org.example.dao;

import java.sql.*;

public class GeneroDAO {

    public void listar() {

        // QUery a la BD
        String sql = "SELECT id_genero, nombre FROM generos ORDER BY id_genero";

        // Conecta y lista los géneros
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id_genero") + " | " +
                                rs.getString("nombre")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al listar géneros: " + e.getMessage());
        }
    }
}