package com.tienda.tiendaelectronica.configuracion;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFiltroAutorizacion extends OncePerRequestFilter {

    private final JwtUtilidad jwtUtilidad;
    private UserDetailsService userDetailsService;

    public JwtFiltroAutorizacion(JwtUtilidad jwtUtilidad) {
        this.jwtUtilidad = jwtUtilidad;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (userDetailsService == null) {
            userDetailsService = SpringContext.getBean(UserDetailsService.class);
        }

        final String encabezado = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (encabezado != null && encabezado.startsWith("Bearer ")) {
            jwt = encabezado.substring(7);

            try {
                username = jwtUtilidad.extraerUsername(jwt);
            } catch (Exception ignored) {}
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtilidad.esTokenValido(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
