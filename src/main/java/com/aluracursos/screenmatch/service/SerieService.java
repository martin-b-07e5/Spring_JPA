package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.dto.EpisodioDTO;
import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.ISerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

  // dependency injection
  @Autowired
  private ISerieRepository ISerieRepository;

  //  Below functions are called from the controller

  public List<SerieDTO> convierteDatos(List<Serie> serie) {
    return serie
        .stream()
        .map(s -> new SerieDTO(
            s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getEvaluacion(), s.getPoster(), s.getGenero(), s.getActores(), s.getSinopsis()
        ))
        .collect(Collectors.toList());
  }

  public List<SerieDTO> getAllSeries() {
    return convierteDatos(ISerieRepository.findAll());
  }

  public List<SerieDTO> findTop5ByOrderByEvaluacionDesc() {
    return ISerieRepository.findTop5ByOrderByEvaluacionDesc();
  }

  public List<SerieDTO> getLanzamientosRecientes() {
    return convierteDatos(ISerieRepository.lanzamientosMasRecientes());
  }

  public SerieDTO getSerieById(Long id) {
    return ISerieRepository.findById(id)
        .map(s -> new SerieDTO(
            s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getEvaluacion(), s.getPoster(), s.getGenero(), s.getActores(), s.getSinopsis()
        ))
        .orElse(null);
  }

  public List<EpisodioDTO> getAllEpisodes(Long id) {
    Optional<Serie> serie = ISerieRepository.findById(id);
    if (serie.isPresent()) {
      Serie s = serie.get();
      return s.getEpisodios()
          .stream()
          .map(ep -> new EpisodioDTO(ep.getTemporada(), ep.getTitulo(), ep.getNumeroEpisodio())
          )
          .collect(Collectors.toList());
    } else {
      return null;
    }
  }

// esto funciona bien. Pero vamos a usar JPQL
  /*public List<EpisodioDTO> getTemporadaPorNumero(Long id, Integer temporada) {
    Optional<Serie> serie = ISerieRepository.findById(id);
    if (serie.isPresent()) {
      Serie s = serie.get();
      return s.getEpisodios()
          .stream()
          .filter(ep -> ep.getTemporada().equals(temporada))
          .map(ep -> new EpisodioDTO(ep.getTemporada(), ep.getTitulo(), ep.getNumeroEpisodio()))
          .collect(Collectors.toList());
    } else {
      return null;
    }
  }
}*/

  // usando JPQL
  public List<EpisodioDTO> getTemporadaPorNumero(Long id, Integer nroTemporada) {
    return ISerieRepository.obtenerTemporadaPorNumero(id, nroTemporada)
        .stream()
        .map(ep -> new EpisodioDTO(ep.getTemporada(), ep.getTitulo(), ep.getNumeroEpisodio()))
        .collect(Collectors.toList());
  }

  public List<SerieDTO> getSeriesPorCategoria(String nombreGenero) {
    Categoria categoria = Categoria.fromString(nombreGenero);
    return convierteDatos(ISerieRepository.findByGenero(categoria));
  }
}