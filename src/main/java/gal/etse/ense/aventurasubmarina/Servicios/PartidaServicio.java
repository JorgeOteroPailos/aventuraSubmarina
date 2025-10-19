package gal.etse.ense.aventurasubmarina.Servicios;

import gal.etse.ense.aventurasubmarina.Modelo.Partida;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PartidaServicio {

    private final Map<String, Partida> partidasActivas;
    public PartidaServicio() {
        partidasActivas= new HashMap<>();
    }

    public Partida crearPartida(Usuario dueno){
        Partida p =new Partida();
        p.id = crearIdPartida();
        partidasActivas.put(p.id,p);
        p.anadirJugador(dueno);
        return p;
    }

    private String crearIdPartida() {
        //TODO
        return "IDENTIFICADOR";
    }

    public Partida getPartida(String idPartida){
        return partidasActivas.get(idPartida);
    }


}
