package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
  Serie findByTitulo(String titulo);

  //  Serie findByTituloContainsIgnoreCase(String titulo);
  Optional<Serie> findByTituloContainsIgnoreCase(String titulo);

  //  5- Top 5 mejores series.
  List<Serie> findTop5ByOrderByEvaluacionDesc();

  //  06-Búsquedas_por_categorías (genre)
  List<Serie> findByGenero(Categoria genero); // le pasamos el 'enum'

  //  List<Serie> findByTotalTemporadasGreaterThanEqualAndEvaluacionGreaterThan(int totalTemporadas, double evaluacion);
  List<Serie> findByTotalTemporadasLessThanEqualAndEvaluacionGreaterThan(int totalTemporadas, double evaluacion);

  // IMPORTANTE los : hacen referencia al parametro que pasamos.
  // Para que distinga entre el atributo de la clase y el valor que pasamos
  @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.evaluacion > :evaluacion")
  List<Serie> seriesPorTemporadaYEvaluacionJPQL(int totalTemporadas, double evaluacion);

  //  Buscar episodios por nombre.
//  LIKE	Check if a value matches a pattern (case sensitive)
//  ILIKE	Check if a value matches a pattern (case insensitive)
  @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
  List<Episodio> episodesByNombre(String nombreEpisodio);
//  List<Episodio> findEpisodesByNombreContainsIgnoreCase(String nombreEpisodio);


//  Serie findByPoster(String poster);
//  Serie findByActores(String actores);
//  Serie findBySinopsis(String sinopsis);
  // Aquí puedes añadir más métodos para buscar series según otros criterios.
  // Por ejemplo, findByTotalTemporadas, findByEvaluacion, etc.
}
