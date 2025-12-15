package gal.usc.aventurasubmarinacliente.modelo;

import java.time.Instant;
import java.util.BitSet;
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
        Instant marcaTemporal,

        // Juego
        Tablero tablero,
        List<Jugador> jugadores,
        Jugador jugadorInicial,

        // Colores
        List<Integer> coloresUsados

) {}
