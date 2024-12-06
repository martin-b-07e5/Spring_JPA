package com.aluracursos.screenmatch.controller;

import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {

  @Autowired
  private SerieRepository repository;


  @GetMapping("/series")
  public List<SerieDTO> getAllSeries() {
    return repository.findAll().stream()
        .map(serie -> new SerieDTO(
                serie.getTitulo(), serie.getTotalTemporadas(), serie.getEvaluacion(),
                serie.getPoster(), serie.getGenero(), serie.getActores(),
                serie.getSinopsis()
            )
        )
        .collect(Collectors.toList());

  }


}
