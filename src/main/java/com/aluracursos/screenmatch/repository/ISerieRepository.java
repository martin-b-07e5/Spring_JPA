package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISerieRepository extends JpaRepository<Serie, Long> {
  Serie findByTitulo(String titulo);

  //  Serie findByTituloContainsIgnoreCase(String titulo);
  Optional<Serie> findByTituloContainsIgnoreCase(String titulo);

  //  5- Top 5 mejores series.
  List<SerieDTO> findTop5ByOrderByEvaluacionDesc();

  // top 5 episodes
  @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC LIMIT 5")
  List<Episodio> top5Episodios(Serie serie);

  //  06-Búsquedas_por_categorías (genre)
  List<Serie> findByGenero(Categoria genero); // le pasamos el 'enum'

  //  List<Serie> findByTotalTemporadasGreaterThanEqualAndEvaluacionGreaterThan(int totalTemporadas, double evaluacion);
  List<Serie> findByTotalTemporadasLessThanEqualAndEvaluacionGreaterThan(int totalTemporadas, double evaluacion);

  // IMPORTANTE los : hacen referencia al parametro que pasamos.
  // Para que distinga entre el atributo de la clase y el valor que pasamos
  @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.evaluacion > :evaluacion")
  List<Serie> seriesPorTemporadaYEvaluacionJPQL(int totalTemporadas, double evaluacion);

  // usamos jpql. lanzamientos de episodios mas recientes. hacer un join de Series con episodios. mostrando los mas
  // nuevos
  @Query("SELECT s FROM Serie s JOIN s.episodios e GROUP BY s ORDER BY MAX(e.fechaDeLanzamiento) DESC LIMIT 6 ")
  List<Serie> lanzamientosMasRecientes();

  //  Buscar series por id
//  Optional<T> findById(ID id);
//  Optional<Serie> findById(Long id);

//  http://localhost:8080/series/14/temporadas/todas
//Iterable<T> findAll();


  //  Buscar episodios por nombre
//  LIKE	Check if a value matches a pattern (case sensitive)
//  ILIKE	Check if a value matches a pattern (case insensitive)
  @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
  List<Episodio> episodesByNombrePostgres(String nombreEpisodio);

  @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE LOWER(e.titulo) LIKE LOWER(CONCAT('%', :nombreEpisodio, '%'))")
  List<Episodio> episodesByNombreMySql(String nombreEpisodio);

  @Query("SELECT ep FROM Serie s JOIN s.episodios ep WHERE s.id = :id AND ep.temporada = :nroTemporada ")
  List<Episodio> obtenerTemporadaPorNumero(Long id, Integer nroTemporada);


//  List<EpisodioDTO> getAllEpisodes(Long id);

  /*LOWER(e.titulo) convierte el título del episodio a minúsculas.

    LOWER(CONCAT('%', :nombreEpisodio, '%')) convierte el parámetro de búsqueda a minúsculas y añade comodines % antes y después, lo que permite buscar coincidencias parciales sin importar mayúsculas o minúsculas.

    LIKE en MySQL es sensible a mayúsculas y minúsculas solo en algunos casos, dependiendo de la collation de la base de datos.
    Este enfoque asegura insensibilidad al caso de manera explícita.
  */


//  Serie findByPoster(String poster);
//  Serie findByActores(String actores);
//  Serie findBySinopsis(String sinopsis);
  // Aquí puedes añadir más métodos para buscar series según otros criterios.
  // Por ejemplo, findByTotalTemporadas, findByEvaluacion, etc.
}
