package org.example.dao;

import java.sql.*;

public class ValoracionDAO {

    // Añadir una valoración
    public void insertarValoracion(int idPelicula, String usuario, double puntuacion, String comentario) {
        String sql = """
            INSERT INTO valoraciones (id_pelicula, usuario, puntuacion, comentario)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPelicula);
            ps.setString(2, usuario);
            ps.setDouble(3, puntuacion);
            ps.setString(4, comentario);

            ps.executeUpdate();

            System.out.println("Valoración añadida correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al añadir valoración: " + e.getMessage());
        }
    }

    // Buscar ID de pelicula por nombre normalizado.
    public Integer buscarIdPeliculaPorNombre(String titulo) {
        String sql = """
            SELECT id_pelicula, titulo
            FROM peliculas
            WHERE LOWER(titulo) LIKE LOWER(?)
            ORDER BY id_pelicula ASC
        """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, "%" + titulo + "%");

            ResultSet rs = ps.executeQuery();

            Integer idEncontrado = null;
            int contador = 0;

            while (rs.next()) {
                contador++;
                idEncontrado = rs.getInt("id_pelicula");

                System.out.println(
                        rs.getInt("id_pelicula") + " | " +
                                rs.getString("titulo")
                );
            }

            if (contador == 0) {
                System.out.println("No se encontró ninguna película con ese nombre.");
                return null;
            }

            if (contador > 1) {
                System.out.println("Hay varias coincidencias. Usa el ID exacto desde la lista.");
                return null;
            }

            return idEncontrado;

        } catch (SQLException e) {
            System.out.println("Error al buscar película: " + e.getMessage());
            return null;
        }
    }

    // Listar valoraciones
    public void listarValoraciones() {

        // Query a la BD
        String sql = """
            SELECT 
                v.id_valoracion,
                p.titulo,
                v.usuario,
                v.puntuacion,
                v.comentario,
            FROM valoraciones v
            JOIN peliculas p ON v.id_pelicula = p.id_pelicula
            ORDER BY v.id_valoracion
        """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n===== LISTADO DE VALORACIONES =====");

            boolean hayResultados = false;

            while (rs.next()) {
                hayResultados = true;

                System.out.println(
                        rs.getInt("id_valoracion") + " | " +
                                rs.getString("titulo") + " | " +
                                rs.getString("usuario") + " | " +
                                rs.getDouble("puntuacion") + " | " +
                                rs.getString("comentario")
                );
            }

            if (!hayResultados) {
                System.out.println("No hay valoraciones registradas.");
            }

        } catch (SQLException e) {
            System.out.println("Error al listar valoraciones: " + e.getMessage());
        }
    }
}