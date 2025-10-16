package gal.etse.ense.aventurasubmarina.Modelo;

import org.h2.table.Table;

import java.util.List;

public class Partida {
    private List<Jugador> jugadores;

    private Tablero tablero;

    public String id;

    public Partida(){
        tablero=new Tablero(15);
    }

}
