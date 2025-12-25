package gal.usc.aventurasubmarinacliente.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

public record Partida(

        // Identificación
        String id,

        // Estado general
        boolean empezada,
        boolean rondaAcabada,
        boolean partidaAcabada,

        // Turnos y rondas
        int turno,
        int contadorRondas,

        // Dados
        int dado1,
        int dado2,

        // Tiempo
        int marcaTemporal,

        // Juego
        Tablero tablero,
        List<Jugador> jugadores,
        Jugador jugadorInicial,

        ArrayList<Jugador> ganadores,

        // Colores
        List<Integer> coloresUsados

) {}
