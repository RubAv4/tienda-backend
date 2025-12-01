/* src/main/java/com/tienda/tiendaelectronica/configuracion/CorsConfig.java */
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

        // 🌐 Orígenes permitidos
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:4200",    // desarrollo Angular
                "https://*.vercel.app"      // cualquier dominio de Vercel
        ));

        // Métodos permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Cabeceras permitidas
        config.setAllowedHeaders(List.of("*"));

        // Cabeceras que el front puede leer
        config.setExposedHeaders(List.of("Authorization"));

        // Para permitir cookies / auth, etc.
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
