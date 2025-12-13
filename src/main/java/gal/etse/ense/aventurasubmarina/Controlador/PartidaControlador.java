package gal.etse.ense.aventurasubmarina.Controlador;

import gal.etse.ense.aventurasubmarina.Modelo.DTO.AccionDTO;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.*;
import gal.etse.ense.aventurasubmarina.Modelo.Partida;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import gal.etse.ense.aventurasubmarina.Servicios.PartidaServicio;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@NullMarked
@RestController
@RequestMapping("/partidas")
public class PartidaControlador {

    private final PartidaServicio partidaServicio;

    public PartidaControlador(PartidaServicio partidaServicio) {
        this.partidaServicio = partidaServicio;
    }

    @PostMapping
    public ResponseEntity<Partida> crearPartida(Authentication autenticacion) throws JugadorYaAnadidoException {

        Partida partidaCreada = partidaServicio.crearPartida(new Usuario(autenticacion.getName()));
        return new ResponseEntity<>(partidaCreada, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/jugadores")
    public ResponseEntity<Partida> unirseAPartida(@PathVariable String id, Authentication autenticacion) throws PartidaNoEncontradaException, JugadorYaAnadidoException {
        Partida p = partidaServicio.anadirJugador(id, new Usuario(autenticacion.getName()));
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partida> getEstadoPartida(
            @PathVariable String id,
            @RequestParam(required = false) Instant selloTemporal)
            throws PartidaNoEncontradaException {

        System.out.println("Entrando a getEstadoPartida");

        Partida p = partidaServicio.getPartida(id);
        if (!p.getMarcaTemporal().equals(selloTemporal)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        return ResponseEntity.ok(p);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Partida> iniciarPartida(@PathVariable String id, Authentication autenticacion) throws PartidaNoEncontradaException, PartidaYaIniciadaException {

        System.out.println("Entrando a iniciarPartida");

        Partida p = partidaServicio.iniciarPartida(id, new Usuario(autenticacion.getName()));
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @DeleteMapping("/{idPartida}/jugadores/{idJugador}")
    public ResponseEntity<Void> abandonarPartida(
            @PathVariable String idPartida,
            @PathVariable String idJugador,
            Authentication autenticacion)
            throws PartidaNoEncontradaException, NoEstasEnLaPartidaException {

        System.out.println("Entrando a abandonarPartida");

        partidaServicio.abandonarPartida(idPartida, idJugador, autenticacion);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Partida> accion(@RequestBody AccionDTO accionDTO, @PathVariable String id, Authentication autenticacion) throws PartidaNoEncontradaException, NoEsTuTurnoException, AccionIlegalException, NoEstasEnLaPartidaException, SintaxisIncorrectaException {

        System.out.println("Entrando a accion");

        Partida p=partidaServicio.accion(id, accionDTO.accion(), accionDTO.accionSubirBajar(), autenticacion.getName());

        return new ResponseEntity<>(p, HttpStatus.OK); //Hacer algo más arriba sobre si la partida acabó
    }
}
