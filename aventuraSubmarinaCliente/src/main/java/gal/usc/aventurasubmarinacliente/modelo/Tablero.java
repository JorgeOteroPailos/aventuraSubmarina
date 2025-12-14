package gal.usc.aventurasubmarinacliente.modelo;

import java.util.List;

public record Tablero(
        int tamano,
        int oxigeno,
        List<Casilla> casillas
) {}

