package gal.etse.ense.aventurasubmarina.Configuracion;

import gal.etse.ense.aventurasubmarina.Filtros.FiltroJWT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SeguridadConfiguracion {

    private final FiltroJWT filtroJWT;

    public SeguridadConfiguracion(FiltroJWT filtroJWT) {
        this.filtroJWT = filtroJWT;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        // Público
                        .requestMatchers("/autenticacion/login").permitAll()
                        .requestMatchers("/autenticacion/register").permitAll()
                        .requestMatchers("/autenticacion/refresh").permitAll()

                        .anyRequest().authenticated()
                )


                .addFilterBefore(filtroJWT, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)

                .httpBasic(AbstractHttpConfigurer::disable)

                .build();
    }
}
