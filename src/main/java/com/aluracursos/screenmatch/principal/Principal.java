package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
  private Scanner teclado = new Scanner(System.in);
  private ConsumoAPI consumoApi = new ConsumoAPI();
  private final String URL_BASE = "https://www.omdbapi.com/?t=";
  private final String API_KEY = "&apikey=79075aba";
  private ConvierteDatos conversor = new ConvierteDatos();
  private List<DatosSerie> datosSeriesList = new ArrayList<>();
  //  private List<Serie> serieList = new ArrayList<>(); // x mi
  private List<Serie> serieList; // por profesora
  private SerieRepository repository;

  // constructor
  public Principal(SerieRepository repository) {
    this.repository = repository;
  }

  // methods
  public void muestraElMenu() {

    int opcion = -1;
    while (opcion != 0) {
      var menu = """
          \n1- Buscar series.
          2- Buscar episodios.
          3- Mostrar series buscadas. (ordenadas por título)
          4- Buscar series por título.
          5- Top 5 mejores series.
          6- Buscar series por género.
          7- Filtrar series por TotalTemporadas <=x y Rating >y.y
          8- Idem 7 Using JPQL (The Java Persistence Query Language).
          9- Episodes By Nombre
          
          0 - Salir""";
      System.out.println(menu);

      try {
        String input = teclado.nextLine();
        opcion = Integer.parseInt(input); // por si ingresa una letra o carácter especial.

        switch (opcion) {
          case 1:
            buscarSerieWeb();
            break;
          case 2:
            buscarEpisodioPorSerie();
            break;
          case 3:
            mostrarSeriesBuscadas();
            break;
          case 4:
            buscarSeriesPorTitulo();
            break;
          case 5:
            mostrarTop5Series();
            break;
          case 6:
            buscarSeriesPorGenero();
            break;
          case 7:
            buscarSeriesPorTotalTemporadasYRating();
            break;
          case 8:
            buscarSeriesPorTotalTemporadasYRatingJPQL();
            break;
          case 9:
            buscarEpisodiosPorNombre();
            break;

          case 0:
            System.out.println("Cerrando la aplicación...");
            break;
          default:
            System.out.println("Ingrese un número del 0 al 6.");
        }
      } catch (NumberFormatException e) {
        System.out.println("OPCIÓN INVÁLIDA. POR FAVOR, INGRESE UN NÚMERO DEL 0 AL 6.");
      }

    } // end while

  } // end muestraElMenu


  // Methods
  private DatosSerie getDatosSerie() {
    System.out.println("Escribe el nombre de la serie que deseas buscar");
    var nombreSerie = teclado.nextLine();
    var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
    System.out.println("json: " + json);

    return conversor.obtenerDatos(json, DatosSerie.class); // return datos
  }

  // case 1
  private void buscarSerieWeb() {
    DatosSerie datos = getDatosSerie();
    if (datos.type().equals("series")) {
//      datosSeriesList.add(datos);
      Serie serie = new Serie(datos);
      repository.save(serie); // guardamos la serie en la base de datos
    } else {
      System.out.println("datos: No se encontró la serie");
      return;
    }
    System.out.println("datos: " + datos);
  }

  // case 2
  private void buscarEpisodioPorSerie() {
//    DatosSerie datosSerie = getDatosSerie();  // ahora trabajamos con la DB
    mostrarSeriesBuscadas(); // series en la DB
    System.out.println("Escribe el nombre de la serie de la cual quieres ver los episodios");
    var nombreSerie = teclado.nextLine();

    Optional<Serie> serie = serieList.stream()
        .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
        .findFirst();

    if (serie.isPresent()) {
      var serieEncontrada = serie.get();

      List<DatosTemporadas> temporadas = new ArrayList<>();

      int totalTemporadas = 0;
      try {
        totalTemporadas = serieEncontrada.getTotalTemporadas();  // convierte a Integer el
        // totalTemporadas
      } catch (NumberFormatException e) {
        System.out.println("***NO HAY INFORMACIÓN DE TEMPORADAS Y EPISODIOS DE LA SERIE: " + serieEncontrada.getTitulo());
        return; // Salir del método.
      }

      for (int i = 1; i <= totalTemporadas; i++) {
        var json =
            consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
        DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
        temporadas.add(datosTemporada);
      }
      temporadas.forEach(System.out::println); // Muestra las temporadas y episodios encontrados:

      // convertimos la lista de temporadas, en una lista de episodios
      List<Episodio> episodios = temporadas.stream()
          .flatMap(datosTemporada -> datosTemporada.episodios().stream()
                  .map(e -> new Episodio(datosTemporada.numero(), e))
//              .map(Episodio::new)
          ) // convertimos cada EpisodioDatos a Episodio
          .collect(Collectors.toList());
      // ya convertí la lista de episodios, a un tipo de datos episodio
      // guardo estos datos en la DB
      serieEncontrada.setEpisodios(episodios);
      repository.save(serieEncontrada);

      System.out.println("\nEpisodios:");
      episodios.forEach(System.out::println); // Muestra los episodios encontrados:
    }


  }

  // case 3
  private void mostrarSeriesBuscadas() {
    // Por mi
    /*datosSeriesList.stream()
        .sorted(Comparator.comparing(DatosSerie::titulo)) // ordenamos las series por título.
        .forEach(s -> System.out.println(
            "\nTítulo: " + s.titulo() +
                "\nSeasons: " + s.totalTemporadas() +
                "\nRating: " + s.evaluacion() +
                "\nGenero: " + s.genero() +
                "\nActores: " + s.actores()
        ));
    */

    // Por la profesora. Convertimos los DatosSerie a Serie
    /*serieList = datosSeriesList.stream()
//        .map(datosSerie -> new Serie(datosSerie)) // lambda
        .map(Serie::new) // method reference
        .collect(Collectors.toList());  // convertimos a list
    */

    serieList = repository.findAll(); // Mostramos las series en la BASE DE DATOS.

    serieList.stream()
        .sorted(Comparator.comparing(Serie::getTitulo))  // ordenamos las series por título.
        .forEach(System.out::println); // Podemos modificar toString para darle formato
  }

  private void buscarSeriesPorTitulo() {
    System.out.println("Escribe el nombre de la serie que desees buscar");
    var nombreSerie = teclado.nextLine();

    // creamos nuevamente una lista donde vamos a almacenar esas series
    Optional<Serie> serieBuscada = repository.findByTituloContainsIgnoreCase(nombreSerie);

    if (serieBuscada.isPresent()) {
      System.out.println("La serie buscada es: " + serieBuscada.get());
    } else {
      System.out.println("No se ha encontrado la serie con ese título.");
    }
  }

  private void mostrarTop5Series() {
    List<Serie> seriesTop5 = repository.findTop5ByOrderByEvaluacionDesc();

    if (seriesTop5.isEmpty()) {
      System.out.println("No hay series en la base de datos.");
    } else {
      System.out.println("\nTop 5 series por rating:");
//      seriesTop5.forEach(System.out::println); // print all
      // imprimimos solo el título y la evaluación
      seriesTop5.forEach(s -> System.out.println(
          "Título: " + s.getTitulo() +
              ", Temporadas: " + s.getTotalTemporadas() +
              ", Rating: " + s.getEvaluacion()
      ));
    }
  }

  // los categorías están en el enum en src/main/java/com/aluracursos/screenmatch/model/Categoria.java
  private void buscarSeriesPorGenero() {
    System.out.println("Escribe el genero de la serie que desees buscar");
    var generoSerie = teclado.nextLine();

    // transformamos generoSerie en un elemento del enum Categoria.
    // Categoria categoria = Categoria.fromString(generoSerie);
    List<Serie> seriesPorGenero = repository.findByGenero(Categoria.fromAString(generoSerie)); // desde la versión 1.15.0 de Spring Data JPA

    if (seriesPorGenero.isEmpty()) {
      System.out.println("No se ha encontrado la categoría '" + generoSerie + "'");
    } else {
      System.out.println("\nSeries con el género '" + generoSerie + "':");
      seriesPorGenero.forEach(System.out::println);
    }
  }

  private void buscarSeriesPorTotalTemporadasYRating() {
    int totalTemporadas = 0;
    double rating = 0;
    try {
      System.out.println("Filtrar series con cuántas temporadas");
      totalTemporadas = teclado.nextInt();
      teclado.nextLine(); // Limpia el buffer tras nextInt()

      System.out.println("¿Con rating a partir de cuanto?");
      rating = teclado.nextDouble();
      teclado.nextLine(); // Limpia el buffer tras nextInt()

      List<Serie> seriesPorTotalTemporadasYRating =
          repository.findByTotalTemporadasLessThanEqualAndEvaluacionGreaterThan(totalTemporadas, rating);

      if (seriesPorTotalTemporadasYRating.isEmpty()) {
        System.out.println("No se han encontrado series con ese criterio.");
      } else {
        System.out.println("\nSeries con un total de temporadas <=" + totalTemporadas + " y un rating >" + rating);
//        seriesPorTotalTemporadasYRating.forEach(System.out::println); // print all
        seriesPorTotalTemporadasYRating.forEach(s -> System.out.println(
            "id" + s.getId() +
                ", Título: " + s.getTitulo() +
                ", Temporadas: " + s.getTotalTemporadas() +
                ", Rating: " + s.getEvaluacion()
        ));
      }

    } catch (InputMismatchException e) {
      System.out.println("Error: Entrada inválida. Por favor, ingrese valores numéricos válidos.");
      System.out.println(e.getMessage());
      teclado.nextLine(); // Limpia el buffer tras entrada no válida
    } catch (NumberFormatException e) {
      System.out.println("Error al parsear un número: " + e.getMessage());
      teclado.nextLine(); // Limpia el buffer tras entrada no válida
    } catch (RuntimeException e) {
      System.out.println("Ocurrió un error inesperado. Por favor, inténtelo de nuevo.");
      e.printStackTrace(); // Opcional: imprime el stack trace para facilitar la depuración
    }

  }

  private void buscarSeriesPorTotalTemporadasYRatingJPQL() {
    int totalTemporadas = 0;
    double rating = 0;
    try {
      System.out.println("Filtrar series con cuántas temporadas");
      totalTemporadas = teclado.nextInt();
      teclado.nextLine(); // Limpia el buffer tras nextInt()

      System.out.println("¿Con rating a partir de cuanto?");
      rating = teclado.nextDouble();
      teclado.nextLine(); // Limpia el buffer tras nextInt()

      List<Serie> seriesPorTotalTemporadasYRating =
          repository.seriesPorTemporadaYEvaluacionJPQL(totalTemporadas, rating);

      if (seriesPorTotalTemporadasYRating.isEmpty()) {
        System.out.println("No se han encontrado series con ese criterio.");
      } else {
        System.out.println("\nSeries con un total de temporadas <=" + totalTemporadas + " y un rating >" + rating);
//        seriesPorTotalTemporadasYRating.forEach(System.out::println); // print all
        seriesPorTotalTemporadasYRating.forEach(s -> System.out.println(
            "id" + s.getId() +
                ", Título: " + s.getTitulo() +
                ", Temporadas: " + s.getTotalTemporadas() +
                ", Rating: " + s.getEvaluacion()
        ));
      }

    } catch (InputMismatchException e) {
      System.out.println("Error: Entrada inválida. Por favor, ingrese valores numéricos válidos.");
      System.out.println(e.getMessage());
      teclado.nextLine(); // Limpia el buffer tras entrada no válida
    } catch (NumberFormatException e) {
      System.out.println("Error al parsear un número: " + e.getMessage());
      teclado.nextLine(); // Limpia el buffer tras entrada no válida
    } catch (RuntimeException e) {
      System.out.println("Ocurrió un error inesperado. Por favor, inténtelo de nuevo.");
      e.printStackTrace(); // Opcional: imprime el stack trace para facilitar la depuración
    }

  } // end buscarSeriesPorTotalTemporadasYRatingNariveQuery()

  private void buscarEpisodiosPorNombre() {
    System.out.println("Escribe el nombre del episodio que deseas buscar");
    String nombreEpisodio = teclado.nextLine();

    List<Episodio> episodioList = repository.episodesByNombre(nombreEpisodio);

    if (episodioList.isEmpty()) {
      System.out.println("No se han encontrado episodios con ese nombre.");
    } else {
      System.out.println("\nEpisodios que contienen el nombre '" + nombreEpisodio + "':");
      episodioList.forEach(e -> {
        System.out.printf(
            "id: %d, Título: %s, Temporada: %d, Serie: %s %n",
            e.getId(), e.getTitulo(), e.getTemporada(), e.getSerie().getTitulo()

        );
      });
    }

  } // end buscarEpisodiosPorNombre


} // end Principal

