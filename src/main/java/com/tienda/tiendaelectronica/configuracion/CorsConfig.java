package com.tienda.tiendaelectronica.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // === ORIGENES PERMITIDOS ===
        config.setAllowedOrigins(List.of(
                "http://localhost:4200",               // Angular local
                "https://*.vercel.app"              // cualquiera de Vercel
        ));

        // === MÉTODOS PERMITIDOS ===
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // === HEADERS PERMITIDOS ===
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Cache-Control",
                "X-Requested-With"
        ));

        // Permitir envío de cookies / headers Authorization
        config.setAllowCredentials(true);

        // Aplicar CORS a todos los endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
