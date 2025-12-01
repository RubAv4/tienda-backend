/* src/main/java/com/tienda/tiendaelectronica/configuracion/SeguridadConfig.java */
package com.tienda.tiendaelectronica.configuracion;

import com.tienda.tiendaelectronica.repositorio.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SeguridadConfig {

    private final JwtFiltroAutenticacion jwtFiltroAutenticacion;
    private final JwtFiltroAutorizacion jwtFiltroAutorizacion;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(sess ->
                    sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // LOGIN / REGISTRO
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/usuarios/buscar").permitAll()

                // PANEL ADMIN / INVENTARIO (solo ADMIN o REPONEDOR)
                .requestMatchers("/api/admin/**")
                    .hasAnyAuthority("ROLE_ADMIN", "ROLE_REPONEDOR")

                // CARRITO y PEDIDOS -> cualquier usuario autenticado
                .requestMatchers("/api/carrito/**", "/api/pedidos/**")
                    .authenticated()

                // TODO LO DEMÁS: autenticado
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFiltroAutenticacion, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtFiltroAutorizacion, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepositorio usuarioRepositorio) {
        return username -> usuarioRepositorio.findByUsername(username)
            .map(usuario -> new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getRoles().stream()
                    // En BD: ROLE_ADMIN, ROLE_REPONEDOR, ROLE_USER
                    .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                    .toList()))
            .orElseThrow(() ->
                new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
