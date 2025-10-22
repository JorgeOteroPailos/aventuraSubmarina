package gal.etse.ense.aventurasubmarina.Servicios;

import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.JugadorYaAnadidoException;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.PartidaNoEncontradaException;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.PartidaYaEmpezadaException;
import gal.etse.ense.aventurasubmarina.Modelo.Jugador;
import gal.etse.ense.aventurasubmarina.Modelo.Partida;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
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

    public Partida accion(String id, String accion) throws PartidaNoEncontradaException{
        Partida p=getPartida(id);
        Jugador j=p.getJugadores().get(p.turno);

        p.reducirOxigeno();

        switch(accion){
            case "coger":
                if(p.tablero.casillas.get(j.posicion).tesoros.isEmpty()){
                    //No hay tesoros
                }else{
                    j.tesorosCargando.add(p.tablero.casillas.get(j.posicion).tesoros); //Añades los tesoros como si fueran 1
                    p.tablero.casillas.get(j.posicion).tesoros.removeLast(); //Eliminas el tesoro de la casilla
                }
                break;
            case "dejar":
                if(p.tablero.casillas.get(j.posicion).tesoros.isEmpty()){
                    p.tablero.casillas.get(j.posicion).tesoros.add(j.tesorosCargando.getLast().getLast());
                }else{
                    //No puedes dejar si no está vacía
                }
                break;
            case "bajar":
                j.subiendo=false; // Es redundante
                break;
            case "subir":
                j.subiendo=true;
                break;

            default:
        }

        return p;
    }


}
