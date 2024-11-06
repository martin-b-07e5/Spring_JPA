package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosSerie(
    @JsonAlias("Title") String titulo,
    @JsonAlias("totalSeasons") String totalTemporadas,
    @JsonAlias("imdbRating") String evaluacion,
    @JsonAlias("Poster") String poster,
    @JsonAlias("Genre") String genero,
    @JsonAlias("Actors") String actores,
    @JsonAlias("Type") String type,
    @JsonAlias("Plot") String sinopsis) {
}
