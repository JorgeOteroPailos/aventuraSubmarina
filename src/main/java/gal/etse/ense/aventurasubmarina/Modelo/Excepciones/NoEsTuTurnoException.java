package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

import gal.etse.ense.aventurasubmarina.Modelo.Jugador;
public class NoEsTuTurnoException extends Throwable {
    private final Jugador jugadorConTurno;

    public Jugador getJugadorConTurno() {
        return jugadorConTurno;
    }

    public NoEsTuTurnoException(Jugador jugadorConTurno){
        this.jugadorConTurno=jugadorConTurno;
    }


}
