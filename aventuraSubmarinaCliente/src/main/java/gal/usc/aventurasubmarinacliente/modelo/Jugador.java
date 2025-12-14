package gal.usc.aventurasubmarinacliente.modelo;

import java.util.List;

public record Jugador(

        Usuario usuario,

        int posicion,
        int puntosGanados,

        boolean subiendo,
        boolean llegoAlSubmarino,

        List<List<Tesoro>> tesorosCargando,

        int color

) {}

