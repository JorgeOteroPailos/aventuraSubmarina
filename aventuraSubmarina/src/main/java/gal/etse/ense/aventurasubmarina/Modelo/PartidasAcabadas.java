package gal.etse.ense.aventurasubmarina.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;

@Document("Partidas")
public class PartidasAcabadas {

    private int numJugadores;
    private ArrayList<Jugador> ganadores;
    @Id
    private String id;
    private String fecha;
    private int fechaID;

    public static PartidasAcabadas from(Partida partida, int fechaID){
        PartidasAcabadas partidasAcabadas = new PartidasAcabadas();
        partidasAcabadas.numJugadores=partida.jugadores.size();
        partidasAcabadas.ganadores=partida.ganadores;
        partidasAcabadas.id=partida.getId();

        LocalDate hoy = LocalDate.now();
        partidasAcabadas.fecha= hoy.toString();

        partidasAcabadas.fechaID = fechaID;

        return partidasAcabadas;
    }
}
