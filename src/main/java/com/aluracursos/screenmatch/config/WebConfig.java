package com.aluracursos.screenmatch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* Global CORS Configuration */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
//        .allowedOrigins("http://localhost:4200", "http://127.0.0.1:5500")
        .allowedOrigins("http://127.0.0.1:5500") // Allow specific origin
        .allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed HTTP methods (OPTIONS HEAD TRACE CONNECT)
        .allowedHeaders("*"); // Allow all headers
//        .allowedHeaders("Content-Type", "X-Requested-With", "Authorization")
//        .exposedHeaders("Authorization")
//        .allowCredentials(true);

  }

}
