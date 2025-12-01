package com.tienda.tiendaelectronica.configuracion;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFiltroAutenticacion extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // En este proyecto el login se maneja por el AuthControlador,
        // así que este filtro no realiza lógica de autenticación.
        // Solo continúa la cadena.
        filterChain.doFilter(request, response);
    }
}
