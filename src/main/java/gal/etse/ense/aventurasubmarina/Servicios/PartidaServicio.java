package gal.etse.ense.aventurasubmarina.Servicios;

import gal.etse.ense.aventurasubmarina.Modelo.Partida;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PartidaServicio {

    private final Map partidasActivas;
    public PartidaServicio() {
        partidasActivas=new HashMap<String, Partida>();
    }

    public Partida crearPartida(Usuario dueno){
        Partida p =new Partida();
        partidasActivas.put(p,p.id);
        //p.anadirJugador(dueno); //TODO
        return p;
    }


}
