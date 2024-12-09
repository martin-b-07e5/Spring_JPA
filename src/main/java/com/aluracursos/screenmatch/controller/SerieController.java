package com.aluracursos.screenmatch.controller;

import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SerieController {

  // dependency injection
  @Autowired
  private SerieService serieService;

  @GetMapping("/inicio")
  public String inicio() {
    return "Bienvenido a Screenmatch!";
  }

  @GetMapping("/series")
  public List<SerieDTO> getAllSeries() {
    return serieService.getAllSeries();
  }

  @GetMapping("/series/top5")
  public List<SerieDTO> getTop5Series() {
    return serieService.findTop5ByOrderByEvaluacionDesc();
  }


}
