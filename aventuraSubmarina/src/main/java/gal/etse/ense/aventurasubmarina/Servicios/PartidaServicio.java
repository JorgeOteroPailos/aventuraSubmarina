package gal.etse.ense.aventurasubmarina.Servicios;

import gal.etse.ense.aventurasubmarina.Modelo.*;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.*;
import gal.etse.ense.aventurasubmarina.Repositorio.PartidaRepositorio;
import gal.etse.ense.aventurasubmarina.Repositorio.PartidasActivasRepositorio;
import gal.etse.ense.aventurasubmarina.Repositorio.Usuario_PartidaRepositorio;
import gal.etse.ense.aventurasubmarina.Utils.DebugPrint;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

    private final Usuario_PartidaRepositorio usuarioPartidaRepo;


    public PartidaServicio(PartidasActivasRepositorio partidasActivasRepo, PartidaRepositorio partidasAcabadasRepo, Usuario_PartidaRepositorio usuarioPartidaRepo) {
        this.partidasActivasRepo=partidasActivasRepo;
        this.partidasAcabadasRepo=partidasAcabadasRepo;
        this.usuarioPartidaRepo=usuarioPartidaRepo;
    }

    public Partida crearPartida(Usuario dueno) throws JugadorYaAnadidoException{

        DebugPrint.show("Entrando a crear Partida en el servicio");

        Partida p =new Partida(crearIdPartida());

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

        Partida p = partidasActivasRepo.findById(idPartida)
                .orElseThrow(() -> new IllegalStateException("La partida no existe: " + idPartida));



        Instant now = Instant.now();
        int tiempo=now.getNano();
        PartidasAcabadas pa = PartidasAcabadas.from(p, tiempo);
        partidasAcabadasRepo.save(pa);

        for(Jugador j: p.getJugadores()){
            Usuario_Partida up=Usuario_Partida.from(j,p, tiempo);
            usuarioPartidaRepo.save(up);
        }

        borrarEn5Min(idPartida);
    }

    @Async
    public void borrarEn5Min(String idPartida) {
        try{
            Thread.sleep(15_000);
            partidasActivasRepo.deleteById(idPartida);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

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

        if(p.isPartidaAcabada()){
            almacenarPartida(p.getId());
        }
        return p;
    }



}
