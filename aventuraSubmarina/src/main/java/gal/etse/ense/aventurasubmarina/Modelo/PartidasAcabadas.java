package gal.etse.ense.aventurasubmarina.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;

@Document("Partidas")
@CompoundIndex(
        name = "id_partida_tiempo_idx",
        def = "{'idPartida': 1, 'tiempoID': 1}",
        unique = true
)
public class PartidasAcabadas {

    private int numJugadores;
    private ArrayList<Jugador> ganadores;
    @Id
    private String id;
    private String idPartida;
    private String fecha;
    private int fechaID;

    public static PartidasAcabadas from(Partida partida, int fechaID){
        PartidasAcabadas partidasAcabadas = new PartidasAcabadas();
        partidasAcabadas.numJugadores=partida.jugadores.size();
        partidasAcabadas.ganadores=partida.ganadores;
        partidasAcabadas.idPartida=partida.getId();

        LocalDate hoy = LocalDate.now();
        partidasAcabadas.fecha= hoy.toString();

        partidasAcabadas.fechaID = fechaID;

        partidasAcabadas.id=partida.getId()+fechaID;

        return partidasAcabadas;
    }

    public int getNumJugadores() {
        return numJugadores;
    }

    public ArrayList<Jugador> getGanadores() {
        return ganadores;
    }

    public String getId() {
        return id;
    }

    public String getIdPartida() {
        return idPartida;
    }

    public String getFecha() {
        return fecha;
    }

    public int getFechaID() {
        return fechaID;
    }

}
