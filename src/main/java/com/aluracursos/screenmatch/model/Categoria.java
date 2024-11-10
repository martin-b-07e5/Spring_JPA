package com.aluracursos.screenmatch.model;

public enum Categoria {
  ACTION("Action"),
  ADVENTURE("Adventure"),
  COMEDY("Comedy"),
  CRIME("Crime"),
  DOCUMENTARY("Documentary"),
  DRAMA("Drama"),
  FANTASY("Fantasy"),
  HORROR("Horror"),
  MUSIC("Music"),
  OTHER("Other"),
  ROMANCE("Romance"),
  SCI_FI("Sci_fi"),
  THRILLER("Thriller");

  // variable
  private final String categoryOmdb;

  // Constructor
  Categoria(String categoryOmdb) {
    this.categoryOmdb = categoryOmdb;
  }

  // Getter
  public String getCategoryOmdb() {
    return categoryOmdb;
  }

  /* 05-Preparando el ambiente: método utilizado en el enum categoría
      método específico para realizar la conversión
         entre la información de OMDB y la de nuestra aplicación. */
  public static Categoria fromString(String text) {
    for (Categoria categoria : Categoria.values()) {
      if (categoria.categoryOmdb.equalsIgnoreCase(text)) {
        return categoria;
      }
    }
    //"Ninguna categoría encontrada
    return Categoria.OTHER;
  }

}
