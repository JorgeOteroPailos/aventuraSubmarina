package gal.usc.aventurasubmarinacliente.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

public record PartidasAcabadas(

        int numJugadores,
        ArrayList<Jugador> ganadores,
        String id,
        String idPartida,
        String fecha,
        int fechaID

) {}
