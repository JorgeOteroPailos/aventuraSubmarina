package gal.usc.aventurasubmarinacliente.modelo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Casilla(
        @JsonProperty("id") Integer id,
        @JsonProperty("treasures") Integer totalTesoros,
        @JsonProperty("players") List<String> nombresJugadores,
        @JsonProperty("tesoros") List<Tesoro> tesoros,
        @JsonProperty("jugadorPresente") Jugador jugadorPresenteDTO
) {

}
