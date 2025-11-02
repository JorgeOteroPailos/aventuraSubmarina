package gal.etse.ense.aventurasubmarina.Servicios;

import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.JugadorYaAnadidoException;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.NoEsTuTurnoException;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.PartidaNoEncontradaException;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.PartidaYaEmpezadaException;
import gal.etse.ense.aventurasubmarina.Modelo.Jugador;
import gal.etse.ense.aventurasubmarina.Modelo.Partida;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class PartidaServicio {

    private static class PartidasActivas{
        private final Map<String, Partida> partidasActivas;

        public PartidasActivas(){
            partidasActivas=new HashMap<>();
        }

        public void anadirPartida(Partida p){
            partidasActivas.put(p.getId(), p);
        }

        public Partida getPartida(String idPartida) throws PartidaNoEncontradaException{
            Partida p=partidasActivas.get(idPartida);
            if(p==null){
                throw new PartidaNoEncontradaException(idPartida);
            }
            return p;
        }
    }

    private final PartidasActivas partidasActivas;

    public PartidaServicio() {
        partidasActivas= new PartidasActivas();
    }

    public Partida crearPartida(Usuario dueno) throws JugadorYaAnadidoException{
        Partida p =new Partida(crearIdPartida());
        //partidasActivas.put(p.id,p);
        p.anadirJugador(dueno);
        return p;
    }

    private String crearIdPartida() {
        //TODO
        return "IDENTIFICADOR";
    }

    public Partida getPartida(String idPartida) throws PartidaNoEncontradaException{
        return partidasActivas.getPartida(idPartida);
    }



    public Partida iniciarPartida(String idPartida) throws PartidaNoEncontradaException, PartidaYaEmpezadaException {
        Partida p=getPartida(idPartida);
        p.iniciar();
        return p;
    }

    public Partida anadirJugador(String idPartida, Usuario u) throws PartidaNoEncontradaException, JugadorYaAnadidoException {
        Partida p = getPartida(idPartida);
        p.anadirJugador(u);
        return p;
    }

    public Partida accion(String id, String accion, Jugador j) throws PartidaNoEncontradaException, NoEsTuTurnoException {
        Partida p=getPartida(id);
        p.accion(accion, j);
        return p;
    }


}
