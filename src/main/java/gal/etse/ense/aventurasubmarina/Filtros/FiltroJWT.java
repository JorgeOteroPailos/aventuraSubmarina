package gal.etse.ense.aventurasubmarina.Filtros;


import gal.etse.ense.aventurasubmarina.Modelo.UsuarioDTO;
import gal.etse.ense.aventurasubmarina.Servicios.AutenticacionServicio;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FiltroJWT extends OncePerRequestFilter {
    private final AutenticacionServicio autenticacionServicio;

    @Autowired
    public FiltroJWT(AutenticacionServicio autenticacionServicio) {
        this.autenticacionServicio = autenticacionServicio;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException, JwtException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(token == null || !token.startsWith("Bearer ")){
            chain.doFilter(request, response);
            return;
        }

        UsuarioDTO user = autenticacionServicio.parseJWT(token.replaceFirst("^Bearer ", ""));

        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(
                user.username(),
                user.password(),
                user.roles().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role)).toList()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}

