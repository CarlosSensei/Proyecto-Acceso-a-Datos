package org.example.vista;

import org.example.dao.DirectorDAO;
import org.example.dao.GeneroDAO;
import org.example.dao.PeliculaDAO;
import org.example.dao.ValoracionDAO;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private final PeliculaDAO peliculaDAO = new PeliculaDAO();
    private final DirectorDAO directorDAO = new DirectorDAO();
    private final GeneroDAO generoDAO = new GeneroDAO();
    private final ValoracionDAO valoracionDAO = new ValoracionDAO();


    // Mostrar el menu principal
    public void mostrar() {
        int opcion = -1;

        do {
            System.out.println("\n===== CINEFRIKI MANAGER =====");
            System.out.println("1. Listar películas");
            System.out.println("2. Añadir película");
            System.out.println("3. Eliminar película");
            System.out.println("4. Modificar película");
            System.out.println("5. Listar directores");
            System.out.println("6. Listar géneros");
            System.out.println("7. Listar valoraciones");
            System.out.println("8. Añadir valoración");
            System.out.println("9. Búsqueda avanzada");
            System.out.println("10. Top películas valoradas");
            System.out.println("0. Salir");

            Integer opcionLeida = leerEntero("Seleccione una opción: ");

            if (opcionLeida == null) {
                continue;
            }

            opcion = opcionLeida;

            switch (opcion) {
                case 1 -> peliculaDAO.listar();
                case 2 -> insertarPelicula();
                case 3 -> eliminarPelicula();
                case 4 -> modificarPelicula();
                case 5 -> directorDAO.listar();
                case 6 -> generoDAO.listar();
                case 7 -> valoracionDAO.listarValoraciones();
                case 8 -> anadirValoracion();
                case 9 -> busquedaAvanzada();
                case 10 -> peliculaDAO.topPeliculasValoradas();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    // Insertar una pelicula nueva
    private void insertarPelicula() {
        System.out.print("Título: ");
        String titulo = scanner.nextLine();

        if (titulo.trim().isEmpty()) {
            System.out.println("Error: el título no puede estar vacío.");
            return;
        }

        Integer anio = leerEntero("Año: ");

        if (anio == null) {
            return;
        }

        if (anio < 1900 || anio > 2100) {
            System.out.println("Error: el año debe estar entre 1900 y 2100.");
            return;
        }

        Integer duracion = leerEntero("Duración en minutos: ");

        if (duracion == null) {
            return;
        }

        if (duracion <= 0) {
            System.out.println("Error: la duración debe ser mayor que 0.");
            return;
        }

        System.out.print("Nombre del director: ");
        String nombreDirector = scanner.nextLine();

        if (nombreDirector.trim().isEmpty()) {
            System.out.println("Error: el nombre del director no puede estar vacío.");
            return;
        }

        System.out.print("Nacionalidad del director: ");
        String nacionalidadDirector = scanner.nextLine();

        if (nacionalidadDirector.trim().isEmpty()) {
            nacionalidadDirector = "Desconocida";
        }

        System.out.print("Género: ");
        String nombreGenero = scanner.nextLine();

        if (nombreGenero.trim().isEmpty()) {
            System.out.println("Error: el género no puede estar vacío.");
            return;
        }

        System.out.print("Sinopsis: ");
        String sinopsis = scanner.nextLine();

        System.out.print("Usuario que realiza la valoración: ");
        String usuarioValoracion = scanner.nextLine();

        if (usuarioValoracion.trim().isEmpty()) {
            System.out.println("Error: el usuario de la valoración no puede estar vacío.");
            return;
        }

        Double puntuacion = leerDouble("Puntuación del 0 al 10: ");

        if (puntuacion == null) {
            return;
        }

        if (puntuacion < 0 || puntuacion > 10) {
            System.out.println("Error: la puntuación debe estar entre 0 y 10.");
            return;
        }

        System.out.print("Comentario de la valoración: ");
        String comentarioValoracion = scanner.nextLine();

        peliculaDAO.insertarPeliculaCompleta(
                titulo,
                anio,
                duracion,
                nombreDirector,
                nacionalidadDirector,
                nombreGenero,
                sinopsis,
                usuarioValoracion,
                puntuacion,
                comentarioValoracion
        );
    }

    // Eliminar una pelicula
    private void eliminarPelicula() {
        Integer id = leerEntero("ID de la película a eliminar: ");

        if (id == null) {
            return;
        }

        peliculaDAO.eliminar(id);
    }

    // modificar los datos de peliculas existentes
    private void modificarPelicula() {
        Integer id = leerEntero("ID de la película a modificar: ");

        if (id == null) {
            return;
        }

        if (!peliculaDAO.existePelicula(id)) {
            System.out.println("Error: no existe ninguna película con ese ID.");
            return;
        }

        System.out.print("Nuevo título: ");
        String titulo = scanner.nextLine();

        if (titulo.trim().isEmpty()) {
            System.out.println("Error: el título no puede estar vacío.");
            return;
        }

        Integer anio = leerEntero("Nuevo año: ");

        if (anio == null) {
            return;
        }

        if (anio < 1900 || anio > 2030) {
            System.out.println("Error: el año debe estar entre 1900 y 2030.");
            return;
        }

        Integer duracion = leerEntero("Nueva duración: ");

        if (duracion == null) {
            return;
        }

        if (duracion <= 0) {
            System.out.println("Error: la duración debe ser mayor que 0.");
            return;
        }

        Integer idDirector = leerEntero("Nuevo ID director: ");

        if (idDirector == null) {
            return;
        }

        if (!peliculaDAO.existeDirector(idDirector)) {
            System.out.println("Error: no existe ningún director con ese ID.");
            return;
        }

        System.out.print("Nueva sinopsis: ");
        String sinopsis = scanner.nextLine();

        peliculaDAO.modificar(id, titulo, anio, duracion, idDirector, sinopsis);
    }

    // Añadir una valoración
    private void anadirValoracion() {

        // primero listamos de nuevo las películas para elegir una
        System.out.println("\n===== PELÍCULAS DISPONIBLES =====");
        peliculaDAO.listar();

        // menu de Añadir Valoracion
        System.out.println("\n¿Cómo quieres seleccionar la película?");
        System.out.println("1. Por ID");
        System.out.println("2. Por nombre");
        System.out.println("0. Cancelar");

        Integer opcion = leerEntero("Seleccione una opción: ");

        if (opcion == null) {
            return;
        }

        if (opcion == 0) {
            System.out.println("Operación cancelada.");
            return;
        }

        Integer idPelicula = null;

        // 2 opciones posibles: id o nombre
        switch (opcion) {
            case 1 -> {
                idPelicula = leerEntero("Introduce el ID de la película: ");

                if (idPelicula == null) {
                    return;
                }

                if (!peliculaDAO.existePelicula(idPelicula)) {
                    System.out.println("Error: no existe ninguna película con ese ID.");
                    return;
                }
            }

            case 2 -> {
                System.out.print("Introduce parte o todo el nombre de la película: ");
                String titulo = scanner.nextLine();

                if (titulo.trim().isEmpty()) {
                    System.out.println("Error: el nombre no puede estar vacío.");
                    return;
                }

                idPelicula = valoracionDAO.buscarIdPeliculaPorNombre(titulo);

                if (idPelicula == null) {
                    return;
                }
            }

            default -> {
                System.out.println("Error: opción no válida.");
                return;
            }
        }

        // Usuario que hace la valoración
        System.out.print("Nombre del usuario: ");
        String usuario = scanner.nextLine();

        if (usuario.trim().isEmpty()) {
            System.out.println("Error: el nombre de usuario no puede estar vacío.");
            return;
        }

        // Puntuacion
        Double puntuacion = leerDouble("Puntuación del 0 al 10: ");

        if (puntuacion == null) {
            return;
        }

        if (puntuacion < 0 || puntuacion > 10) {
            System.out.println("Error: la puntuación debe estar entre 0 y 10.");
            return;
        }

        // Comentario de las valoraciones
        System.out.print("Comentario: ");
        String comentario = scanner.nextLine();

        if (comentario.trim().isEmpty()) {
            comentario = "Sin comentario";
        }

        valoracionDAO.insertarValoracion(idPelicula, usuario, puntuacion, comentario);
    }

    // Busqueda avanzada por genero, año y valoración.
    private void busquedaAvanzada() {
        System.out.print("Género (dejar vacío para todos): ");
        String genero = scanner.nextLine();

        Integer anioMin = leerEntero("Año mínimo (dejar vacío para no filtrar): ");
        Integer anioMax = leerEntero("Año máximo (dejar vacío para no filtrar): ");
        Double puntuacionMinima = leerDouble("Puntuación mínima (dejar vacío para no filtrar): ");

        if (anioMin != null && anioMax != null && anioMin > anioMax) {
            System.out.println("Error: el año mínimo no puede ser mayor que el año máximo.");
            return;
        }

        if (puntuacionMinima != null && (puntuacionMinima < 0 || puntuacionMinima > 10)) {
            System.out.println("Error: la puntuación debe estar entre 0 y 10.");
            return;
        }

        peliculaDAO.busquedaAvanzada(genero, anioMin, anioMax, puntuacionMinima);
    }

    // Entrada de int válidos
    private Integer leerEntero(String mensaje) {
        System.out.print(mensaje);
        String entrada = scanner.nextLine();

        if (entrada.trim().isEmpty()) {
            return null;
        }

        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Error: debes introducir un número válido.");
            return null;
        }
    }

    // Entrada de double válidos
    private Double leerDouble(String mensaje) {
        System.out.print(mensaje);
        String entrada = scanner.nextLine();

        if (entrada.trim().isEmpty()) {
            return null;
        }

        try {
            return Double.parseDouble(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Error: debes introducir un número decimal válido.");
            return null;
        }
    }

}
