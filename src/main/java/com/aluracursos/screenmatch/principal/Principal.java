package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporadas;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
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
    var opcion = -1;
    while (opcion != 0) {
      var menu = """
          \n1- Buscar series.
          2- Buscar episodios.
          3- Mostrar series buscadas.
          4- Buscar series por título.
          5- Top 5 mejores series.
          
          0 - Salir""";
      System.out.println(menu);
      opcion = teclado.nextInt();
      teclado.nextLine();

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


        case 0:
          System.out.println("Cerrando la aplicación...");
          break;
        default:
          System.out.println("Opción inválida");
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
        totalTemporadas = Integer.parseInt(serieEncontrada.getTotalTemporadas());  // convierte a Integer el
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
          "\nTítulo: " + s.getTitulo() +
              ", Rating: " + s.getEvaluacion()
      ));
    }
  }

} // end Principal

