package gal.etse.ense.aventurasubmarina.Servicios;

import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.*;
import gal.etse.ense.aventurasubmarina.Modelo.Partida;
import gal.etse.ense.aventurasubmarina.Modelo.PartidasAcabadas;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import gal.etse.ense.aventurasubmarina.Repositorio.PartidaRepositorio;
import gal.etse.ense.aventurasubmarina.Repositorio.PartidasActivasRepositorio;
import gal.etse.ense.aventurasubmarina.Utils.DebugPrint;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class PartidaServicio {

    public void abandonarPartida(String idPartida, String idJugador, Authentication autenticacion) throws PartidaNoEncontradaException, NoEstasEnLaPartidaException {
        Partida p = getPartida(idPartida);
        if(!Objects.equals(idJugador, autenticacion.getName())){
            throw new SuplantacionException(idJugador,autenticacion.getName());
        }
        p.abandonarPartida(idJugador);

        partidasActivasRepo.save(p);
    }

    private final PartidaRepositorio partidasAcabadasRepo;

    private final PartidasActivasRepositorio partidasActivasRepo;


    public PartidaServicio(PartidasActivasRepositorio partidasActivasRepo, PartidaRepositorio partidasAcabadasRepo) {
        this.partidasActivasRepo=partidasActivasRepo;
        this.partidasAcabadasRepo=partidasAcabadasRepo;
    }

    public Partida crearPartida(Usuario dueno) throws JugadorYaAnadidoException{

        DebugPrint.show("Entrando a crear Partida en el servicio");

        Partida p =new Partida(crearIdPartida(),this);

        p.anadirJugador(dueno);

        DebugPrint.show("Partida creada: " + p);

        partidasActivasRepo.save(p);

        DebugPrint.show("Partida sacada del redis: " + partidasActivasRepo.findById(p.getId()));





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
        }while(partidasActivasRepo.existsById(id));
        return id;
    }

    public Partida getPartida(String idPartida) throws PartidaNoEncontradaException{
        if(partidasActivasRepo.findById(idPartida).isPresent()){
            return partidasActivasRepo.findById(idPartida).get();
        }else{
            throw new PartidaNoEncontradaException(idPartida);
        }
    }

    public Partida iniciarPartida(String idPartida, Usuario usuario) throws PartidaNoEncontradaException, PartidaYaIniciadaException {
        Partida p=getPartida(idPartida);

        if(!Objects.equals(p.getJugadores().getFirst().getUsuario().username(), usuario.getNombre())){
            throw new NoEresElCreadorException();
        }

        p.iniciar();

        partidasActivasRepo.save(p);
        return p;
    }

    public void almacenarPartida(String idPartida) {

        Partida p=partidasActivasRepo.findById(idPartida).get();
        PartidasAcabadas pa= PartidasAcabadas.from(p);
        partidasAcabadasRepo.save(pa);
    }

    public Partida anadirJugador(String idPartida, Usuario u) throws PartidaNoEncontradaException, JugadorYaAnadidoException {

        Partida p = getPartida(idPartida);
        p.anadirJugador(u);

        partidasActivasRepo.save(p);

        return p;
    }

    public Partida accion(String id, String accion, String accion2, String jugador) throws PartidaNoEncontradaException, NoEsTuTurnoException, AccionIlegalException, NoEstasEnLaPartidaException, SintaxisIncorrectaException {
        Partida p=getPartida(id);
        p.accion(accion,accion2,jugador);
        partidasActivasRepo.save(p);
        return p;
    }



}
