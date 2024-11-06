package com.aluracursos.screenmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos {
  private ObjectMapper objectMapper = new ObjectMapper();

  public class DeserializationException extends Exception {
    public DeserializationException(String message, Throwable cause) {
      super(message, cause);
    }
  }


  @Override
  public <T> T obtenerDatos(String json, Class<T> clase) {
    try {
      return objectMapper.readValue(json, clase);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }


}
