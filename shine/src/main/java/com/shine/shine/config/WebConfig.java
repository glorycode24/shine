package com.shine.shine.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
  /* 
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
          .allowedOrigins(
            "http://localhost:3000",  // for user frontend
            "http://localhost:3001"   // for admin frontend
          )
          .allowedMethods("GET", "POST", "PUT", "DELETE")
          .allowCredentials(true);
      }
    };
  }
    */
}
