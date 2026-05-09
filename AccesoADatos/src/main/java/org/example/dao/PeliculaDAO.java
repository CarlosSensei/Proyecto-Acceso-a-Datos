package org.example.dao;

import java.sql.*;

public class PeliculaDAO {

    // Metodo para listar las películas
    public void listar() {

        // query a la base de datos
        String sql = """
        SELECT 
            p.id_pelicula,
            p.titulo,
            p.anio,
            p.duracion,
            d.nombre AS director,
            STRING_AGG(DISTINCT g.nombre, ', ') AS generos,
            COALESCE(ROUND(AVG(v.puntuacion), 2), 0) AS media_valoracion,
            COUNT(DISTINCT v.id_valoracion) AS total_valoraciones
        FROM peliculas p
        JOIN directores d 
            ON p.id_director = d.id_director
        LEFT JOIN pelicula_genero pg 
            ON p.id_pelicula = pg.id_pelicula
        LEFT JOIN generos g 
            ON pg.id_genero = g.id_genero
        LEFT JOIN valoraciones v
            ON p.id_pelicula = v.id_pelicula
        GROUP BY 
            p.id_pelicula,
            p.titulo,
            p.anio,
            p.duracion,
            d.nombre
        ORDER BY p.id_pelicula ASC
    """;
        // conectar y lanzar el listado de peliculas
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n===== LISTADO DE PELÍCULAS =====");

            boolean hayResultados = false;

            while (rs.next()) {
                hayResultados = true;

                String generos = rs.getString("generos");

                if (generos == null) {
                    generos = "Sin género";
                }

                System.out.println(
                        rs.getInt("id_pelicula") + " | " +
                                rs.getString("titulo") + " | " +
                                rs.getInt("anio") + " | " +
                                rs.getInt("duracion") + " min | " +
                                rs.getString("director") + " | Géneros: " +
                                generos + " | Valoración media: " +
                                rs.getDouble("media_valoracion") + " | Nº valoraciones: " +
                                rs.getInt("total_valoraciones")
                );
            }

            if (!hayResultados) {
                System.out.println("No hay películas registradas.");
            }

        } catch (SQLException e) {
            System.out.println("Error al listar películas: " + e.getMessage());
        }
    }

    // Metodo para eliminar una pelicula.
    public void eliminar(int idPelicula) {

        if (!existePelicula(idPelicula)) {
            System.out.println("Error: no existe ninguna película con ese ID.");
            return;
        }

        // query a la base de datos.
        String sql = "DELETE FROM peliculas WHERE id_pelicula = ?";

        // conecta y actualiza datos.
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPelicula);
            ps.executeUpdate();

            System.out.println("Película eliminada correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al eliminar película: " + e.getMessage());
        }
    }

    // Modificar los datos de la pelicula
    public void modificar(int idPelicula, String titulo, int anio, int duracion, int idDirector, String sinopsis) {

        if (!existePelicula(idPelicula)) {
            System.out.println("Error: no existe ninguna película con ese ID.");
            return;
        }

        if (!existeDirector(idDirector)) {
            System.out.println("Error: no existe ningún director con ese ID.");
            return;
        }

        if (titulo == null || titulo.trim().isEmpty()) {
            System.out.println("Error: el título no puede estar vacío.");
            return;
        }

        if (anio < 1900 || anio > 2100) {
            System.out.println("Error: el año introducido no es válido.");
            return;
        }

        if (duracion <= 0) {
            System.out.println("Error: la duración debe ser mayor que 0.");
            return;
        }

        // query a la base de datos
        String sql = """
        UPDATE peliculas
        SET titulo = ?, anio = ?, duracion = ?, id_director = ?, sinopsis = ?
        WHERE id_pelicula = ?
    """;

        // conecta y lanza la modificacion
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, titulo);
            ps.setInt(2, anio);
            ps.setInt(3, duracion);
            ps.setInt(4, idDirector);
            ps.setString(5, sinopsis);
            ps.setInt(6, idPelicula);

            ps.executeUpdate();

            System.out.println("Película modificada correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al modificar película: " + e.getMessage());
        }
    }

    // Query de búsqueda avanzada por género, años y valoracion minima.
    public void busquedaAvanzada(String genero, Integer anioMin, Integer anioMax, Double puntuacionMinima) {

        // Query a la BD
        String sql = """
        SELECT 
            p.titulo,
            p.anio,
            d.nombre AS director,
            COALESCE(ROUND(AVG(v.puntuacion), 2), 0) AS media
        FROM peliculas p
        JOIN directores d ON p.id_director = d.id_director
        LEFT JOIN pelicula_genero pg ON p.id_pelicula = pg.id_pelicula
        LEFT JOIN generos g ON pg.id_genero = g.id_genero
        LEFT JOIN valoraciones v ON p.id_pelicula = v.id_pelicula
        WHERE (? IS NULL OR g.nombre ILIKE ?)
        AND (? IS NULL OR p.anio >= ?)
        AND (? IS NULL OR p.anio <= ?)
        GROUP BY p.id_pelicula, d.nombre
        HAVING (? IS NULL OR COALESCE(AVG(v.puntuacion), 0) >= ?)
        ORDER BY media DESC NULLS LAST, p.anio DESC
    """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            String filtroGenero = null;

            if (genero != null && !genero.trim().isEmpty()) {
                filtroGenero = "%" + genero.trim() + "%";
            }

            ps.setString(1, filtroGenero);
            ps.setString(2, filtroGenero);

            if (anioMin == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
                ps.setNull(4, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, anioMin);
                ps.setInt(4, anioMin);
            }

            if (anioMax == null) {
                ps.setNull(5, java.sql.Types.INTEGER);
                ps.setNull(6, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, anioMax);
                ps.setInt(6, anioMax);
            }

            if (puntuacionMinima == null) {
                ps.setNull(7, java.sql.Types.DOUBLE);
                ps.setNull(8, java.sql.Types.DOUBLE);
            } else {
                ps.setDouble(7, puntuacionMinima);
                ps.setDouble(8, puntuacionMinima);
            }

            ResultSet rs = ps.executeQuery();

            boolean hayResultados = false;

            while (rs.next()) {
                hayResultados = true;

                System.out.println(
                        rs.getString("titulo") + " | " +
                                rs.getInt("anio") + " | " +
                                rs.getString("director") + " | Nota media: " +
                                rs.getDouble("media")
                );
            }

            if (!hayResultados) {
                System.out.println("No se encontraron películas con esos filtros.");
            }

        } catch (SQLException e) {
            System.out.println("Error en búsqueda avanzada: " + e.getMessage());
        }
    }

    // TOP 10 Peliculas mejor valoradas
    public void topPeliculasValoradas() {

        // Query a la BD
        String sql = """
        SELECT 
            p.titulo,
            p.anio,
            d.nombre AS director,
            ROUND(AVG(v.puntuacion), 2) AS media,
            COUNT(v.id_valoracion) AS total_valoraciones
        FROM peliculas p
        JOIN directores d ON p.id_director = d.id_director
        JOIN valoraciones v ON p.id_pelicula = v.id_pelicula
        GROUP BY p.id_pelicula, d.nombre
        ORDER BY media DESC, total_valoraciones DESC
        LIMIT 10
    """;

        // Conecta y lanza el listado
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n===== TOP PELÍCULAS VALORADAS =====");

            boolean hayResultados = false;

            while (rs.next()) {
                hayResultados = true;

                System.out.println(
                        rs.getString("titulo") + " | " +
                                rs.getInt("anio") + " | " +
                                rs.getString("director") + " | Media: " +
                                rs.getDouble("media") + " | Valoraciones: " +
                                rs.getInt("total_valoraciones")
                );
            }

            if (!hayResultados) {
                System.out.println("No hay películas registradas.");
            }

        } catch (SQLException e) {
            System.out.println("Error al generar informe: " + e.getMessage());
        }
    }

    // Metodo auxiliar para ver si existe la pelicula
    public boolean existePelicula(int idPelicula) {

        // Query a la BD
        String sql = "SELECT COUNT(*) FROM peliculas WHERE id_pelicula = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPelicula);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al comprobar la película: " + e.getMessage());
        }

        return false;
    }

    // Metodo auxiliar para ver si existe director.
    public boolean existeDirector(int idDirector) {

        // Query a la BD
        String sql = "SELECT COUNT(*) FROM directores WHERE id_director = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idDirector);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al comprobar el director: " + e.getMessage());
        }

        return false;
    }

    // INserta los datos completos de una pelicula
    public void insertarPeliculaCompleta(
            String titulo,
            int anio,
            int duracion,
            String nombreDirector,
            String nacionalidadDirector,
            String nombreGenero,
            String sinopsis,
            String usuarioValoracion,
            double puntuacion,
            String comentarioValoracion
    )
    // Querys para cada tabla de la BD
    {
        String buscarDirector = "SELECT id_director FROM directores WHERE LOWER(nombre) = LOWER(?)";
        String insertarDirector = "INSERT INTO directores (nombre, nacionalidad) VALUES (?, ?) RETURNING id_director";

        String buscarGenero = "SELECT id_genero FROM generos WHERE LOWER(nombre) = LOWER(?)";
        String insertarGenero = "INSERT INTO generos (nombre) VALUES (?) RETURNING id_genero";

        String insertarPelicula = """
        INSERT INTO peliculas (titulo, anio, duracion, id_director, sinopsis)
        VALUES (?, ?, ?, ?, ?)
        RETURNING id_pelicula
    """;

        String insertarRelacion = """
        INSERT INTO pelicula_genero (id_pelicula, id_genero)
        VALUES (?, ?)
        ON CONFLICT DO NOTHING
    """;

        String insertarValoracion = """
        INSERT INTO valoraciones (id_pelicula, usuario, puntuacion, comentario)
        VALUES (?, ?, ?, ?)
    """;

        try (Connection conexion = ConexionBD.obtenerConexion()) {

            conexion.setAutoCommit(false);

            int idDirector;
            int idGenero;
            int idPelicula;

            try {
                // Buscar o crear director
                try (PreparedStatement ps = conexion.prepareStatement(buscarDirector)) {
                    ps.setString(1, nombreDirector);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        idDirector = rs.getInt("id_director");
                    } else {
                        try (PreparedStatement psInsert = conexion.prepareStatement(insertarDirector)) {
                            psInsert.setString(1, nombreDirector);
                            psInsert.setString(2, nacionalidadDirector);

                            ResultSet rsInsert = psInsert.executeQuery();
                            rsInsert.next();
                            idDirector = rsInsert.getInt("id_director");
                        }
                    }
                }

                // Buscar o crear género
                try (PreparedStatement ps = conexion.prepareStatement(buscarGenero)) {
                    ps.setString(1, nombreGenero);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        idGenero = rs.getInt("id_genero");
                    } else {
                        try (PreparedStatement psInsert = conexion.prepareStatement(insertarGenero)) {
                            psInsert.setString(1, nombreGenero);

                            ResultSet rsInsert = psInsert.executeQuery();
                            rsInsert.next();
                            idGenero = rsInsert.getInt("id_genero");
                        }
                    }
                }

                // Insertar película
                try (PreparedStatement ps = conexion.prepareStatement(insertarPelicula)) {
                    ps.setString(1, titulo);
                    ps.setInt(2, anio);
                    ps.setInt(3, duracion);
                    ps.setInt(4, idDirector);
                    ps.setString(5, sinopsis);

                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    idPelicula = rs.getInt("id_pelicula");
                }

                // Relacionar película con género
                try (PreparedStatement ps = conexion.prepareStatement(insertarRelacion)) {
                    ps.setInt(1, idPelicula);
                    ps.setInt(2, idGenero);
                    ps.executeUpdate();
                }

                // Insertar valoración
                try (PreparedStatement ps = conexion.prepareStatement(insertarValoracion)) {
                    ps.setInt(1, idPelicula);
                    ps.setString(2, usuarioValoracion);
                    ps.setDouble(3, puntuacion);
                    ps.setString(4, comentarioValoracion);
                    ps.executeUpdate();
                }

                conexion.commit();

                System.out.println("Película, director, género y valoración insertados correctamente.");

            } catch (SQLException e) {
                conexion.rollback();
                System.out.println("Error al insertar la película completa: " + e.getMessage());
            } finally {
                conexion.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

}
