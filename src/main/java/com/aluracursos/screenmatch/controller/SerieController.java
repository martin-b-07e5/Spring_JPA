package com.aluracursos.screenmatch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SerieController {

  @GetMapping("/series")
  public String mostrarMensaje() {
    return "este es mi primer msg de Series en mi API web - en http://localhost:8080/series";
  }

}
