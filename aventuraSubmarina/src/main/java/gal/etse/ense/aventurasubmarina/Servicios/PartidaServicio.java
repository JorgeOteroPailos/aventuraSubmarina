package gal.etse.ense.aventurasubmarina.Servicios;

import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.*;
import gal.etse.ense.aventurasubmarina.Modelo.Partida;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import gal.etse.ense.aventurasubmarina.Utils.DebugPrint;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class PartidaServicio {

    public void abandonarPartida(String idPartida, String idJugador, Authentication autenticacion) throws PartidaNoEncontradaException, NoEstasEnLaPartidaException {
        Partida p = getPartida(idPartida);
        if(!Objects.equals(idJugador, autenticacion.getName())){
            throw new SuplantacionException(idJugador,autenticacion.getName());
        }
        p.abandonarPartida(idJugador);
    }

    private static class PartidasActivas{
        private final Map<String, Partida> partidasActivas;

        public PartidasActivas(){
            partidasActivas=new ConcurrentHashMap<>();
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

        public boolean existeIdentificador(String identificador){
            return (partidasActivas.get(identificador)!=null);
        }
    }

    private final PartidasActivas partidasActivas;

    public PartidaServicio() {
        partidasActivas= new PartidasActivas();
    }

    public Partida crearPartida(Usuario dueno) throws JugadorYaAnadidoException{

        DebugPrint.show("Entrando a crear Partida en el servicio");

        Partida p =new Partida(crearIdPartida());
        partidasActivas.anadirPartida(p);
        p.anadirJugador(dueno);
        return p;
    }

    private String crearIdPartida() {
        String id;
        do {
            char letra1 = (char) ('A' + ThreadLocalRandom.current().nextInt(26));
            char letra2 = (char) ('A' + ThreadLocalRandom.current().nextInt(26));

            int numero1 = ThreadLocalRandom.current().nextInt(10);
            int numero2 = ThreadLocalRandom.current().nextInt(10);

            id = "" + letra1 + letra2 + numero1 + numero2;
        }while(partidasActivas.existeIdentificador(id));
        return id;
    }

    public Partida getPartida(String idPartida) throws PartidaNoEncontradaException{
        return partidasActivas.getPartida(idPartida);
    }

    public Partida iniciarPartida(String idPartida, Usuario usuario) throws PartidaNoEncontradaException, PartidaYaIniciadaException {
        Partida p=getPartida(idPartida);

        if(!Objects.equals(p.getJugadorInicial().getUsuario().getNombre(), usuario.getNombre())){
            throw new NoEresElCreadorException();
        }

        p.iniciar();
        return p;
    }

    public Partida anadirJugador(String idPartida, Usuario u) throws PartidaNoEncontradaException, JugadorYaAnadidoException {

        Partida p = getPartida(idPartida);
        p.anadirJugador(u);
        return p;
    }

    public Partida accion(String id, String accion, String accion2, String jugador) throws PartidaNoEncontradaException, NoEsTuTurnoException, AccionIlegalException, NoEstasEnLaPartidaException, SintaxisIncorrectaException {
        Partida p=getPartida(id);
        p.accion(accion,accion2,jugador);
        return p;
    }



}
