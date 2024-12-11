package com.aluracursos.screenmatch.model;

public enum Categoria {
  ACTION("Action"),
  AVENTURA("Adventure"),
  COMEDIA("Comedy"),
  CRIMEN("Crime"),
  DRAMA("Drama");

  // variable
  private final String categoryOmdb;

  // Constructor
  Categoria(String categoryOmdb) {
    this.categoryOmdb = categoryOmdb;
  }


  /* 05-Preparando el ambiente: método utilizado en el enum categoría
        método específico para realizar la conversión
           entre la información de OMDB y la de nuestra aplicación. */
  public static Categoria fromString(String text) {
    for (Categoria categoria : Categoria.values()) {
      if (categoria.name().equalsIgnoreCase(text) || categoria.categoryOmdb.equalsIgnoreCase(text)) {
        return categoria;
      }
    }
    throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    //"Ninguna categoría encontrada
//    return Categoria.OTHER;
  }


}
