package com.aluracursos.screenmatch.model;

//import com.aluracursos.screenmatch.service.ConsultaChatGPT;
//import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.persistence.*;

import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto increment
  private Long id;
  @Column(name = "title", unique = true)
  private String titulo;
  private String totalTemporadas;
  private Double evaluacion; // String » Double
  private String poster;
  @Enumerated(EnumType.STRING)
  private Categoria genero; // String » Categoria
  private String actores;
  private String sinopsis;
  // una serie puede tener muchos episodios. Y un episodio pertenece a una sola serie.
//  @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL)
  @Transient
  private List<Episodio> episodioList; // No los mapeo todavía. Toda serie contiene una lista de episodes

  // default constructor
  public Serie() {
  }

  // *** Constructor que recibe DatosSerie ***
  public Serie(DatosSerie datosSerie) {
    this.titulo = datosSerie.titulo();
    this.totalTemporadas = datosSerie.totalTemporadas();
//    this.evaluacion = Double.valueOf(datosSerie.evaluacion());  // (este tb funciona)
//    this.evaluacion = OptionalDouble.of(Double.valueOf(datosSerie.evaluacion())).orElse(0);  // String » Double
    this.evaluacion = OptionalDouble.of(Double.parseDouble(datosSerie.evaluacion())).orElse(0);  // String » Double
    this.poster = datosSerie.poster();
    // [0] Trae el primero. Usa el Enum en Categoria .trim() no es necesario.
    this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim());
    this.actores = datosSerie.actores();
    this.sinopsis = datosSerie.sinopsis();
//    this.sinopsis = ConsultaChatGPT.obtenerTraduccion(datosSerie.sinopsis());
  }

  // getters and setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getTotalTemporadas() {
    return totalTemporadas;
  }

  public void setTotalTemporadas(String totalTemporadas) {
    this.totalTemporadas = totalTemporadas;
  }

  public Double getEvaluacion() {
    return evaluacion;
  }

  public void setEvaluacion(Double evaluacion) {
    this.evaluacion = evaluacion;
  }

  public String getPoster() {
    return poster;
  }

  public void setPoster(String poster) {
    this.poster = poster;
  }

  public Categoria getGenero() {
    return genero;
  }

  public void setGenero(Categoria genero) {
    this.genero = genero;
  }

  public String getActores() {
    return actores;
  }

  public void setActores(String actores) {
    this.actores = actores;
  }

  public String getSinopsis() {
    return sinopsis;
  }

  public void setSinopsis(String sinopsis) {
    this.sinopsis = sinopsis;
  }

  // toString
  @Override
  public String toString() {
    return
        "id='" + id + '\'' +
            ", titulo='" + titulo + '\'' +
            ", totalTemporadas='" + totalTemporadas + '\'' +
            ", evaluación=" + evaluacion +
            ", poster='" + poster + '\'' +
            ", genero=" + genero +
            ", actores='" + actores + '\'' +
            ", sinopsis='" + sinopsis + '\'';
  }

}
