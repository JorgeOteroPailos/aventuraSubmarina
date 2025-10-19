package gal.etse.ense.aventurasubmarina.Modelo;

import java.security.Timestamp;
import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Partida {

    private static final int maximoJugadores=6;

    private final BitSet coloresUsados=new BitSet(maximoJugadores);

    private final List<Jugador> jugadores=new ArrayList<>(maximoJugadores);

    private int nJugadores=0;

    private Tablero tablero=new Tablero();

    private Instant marcaTemporal;

    public String id;

    public Partida(){
        tablero=new Tablero();
        marcaTemporal= Instant.now();
    }


    public void anadirJugador(Usuario u) {
        int colorDisponible= coloresUsados.nextClearBit(0);
        jugadores.add(new Jugador(this, u, colorDisponible));
        coloresUsados.set(colorDisponible, true);
        nJugadores++;
        marcaTemporal=Instant.now();
    }

    public Instant getMarcaTemporal(){
        return marcaTemporal;
    }
}
