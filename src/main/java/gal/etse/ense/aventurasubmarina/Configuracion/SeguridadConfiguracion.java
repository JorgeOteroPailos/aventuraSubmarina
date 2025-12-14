package gal.etse.ense.aventurasubmarina.Configuracion;

import gal.etse.ense.aventurasubmarina.Filtros.FiltroJWT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SeguridadConfiguracion {

    private final FiltroJWT filtroJWT;

    public SeguridadConfiguracion(FiltroJWT filtroJWT) {
        this.filtroJWT = filtroJWT;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Configuración CORS CORREGIDA (usa tu bean)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ← CAMBIO AQUÍ

                // 2. Deshabilitar CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // 3. Configurar política de sesiones como STATELESS
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. Configurar autorización de endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/autenticacion/login").permitAll()
                        .requestMatchers("/autenticacion/register").permitAll()
                        .requestMatchers("/autenticacion/refresh").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )

                // 5. Agregar filtro JWT
                .addFilterBefore(filtroJWT, UsernamePasswordAuthenticationFilter.class)

                // 6. Deshabilitar autenticación básica HTTP
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // ORIGEN específico (no usar "*" con allowCredentials)
        config.setAllowedOrigins(List.of("http://localhost:5173"));

        // MÉTODOS permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // HEADERS permitidos (¡NO usar "*" con allowCredentials!)
        config.setAllowedHeaders(List.of(
                "Authorization",       // ← CLAVE para enviar token
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With"
        ));

        // HEADERS expuestos en respuesta (¡para que el frontend vea Authorization!)
        config.setExposedHeaders(List.of(
                "Authorization",       // ← CLAVE para recibir token
                "Content-Disposition"
        ));

        // Permitir cookies/credentials
        config.setAllowCredentials(true);

        // Tiempo cache preflight (OPTIONS)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}