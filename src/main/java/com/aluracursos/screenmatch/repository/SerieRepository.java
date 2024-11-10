package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
  Serie findByTitulo(String titulo);

  //  Serie findByTituloContainsIgnoreCase(String titulo);
  Optional<Serie> findByTituloContainsIgnoreCase(String titulo);

  //  5- Top 5 mejores series.
  List<Serie> findTop5ByOrderByEvaluacionDesc();


//  Serie findByPoster(String poster);
//  Serie findByGenero(String genero);
//  Serie findByActores(String actores);
//  Serie findBySinopsis(String sinopsis);
  // Aquí puedes añadir más métodos para buscar series según otros criterios.
  // Por ejemplo, findByTotalTemporadas, findByEvaluacion, etc.
}
