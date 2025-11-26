package gal.etse.ense.aventurasubmarina.Controlador;

import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.*;
import gal.etse.ense.aventurasubmarina.Modelo.Jugador;
import gal.etse.ense.aventurasubmarina.Modelo.Partida;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import gal.etse.ense.aventurasubmarina.Servicios.PartidaServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/partidas")
public class PartidaControlador {

    private final PartidaServicio partidaServicio;

    public PartidaControlador(PartidaServicio partidaServicio) {
        this.partidaServicio = partidaServicio;
    }

    @PutMapping
    public ResponseEntity<Partida> crearPartida(@RequestBody Usuario usuario) throws JugadorYaAnadidoException {
        Partida partidaCreada = partidaServicio.crearPartida(usuario);
        return new ResponseEntity<>(partidaCreada, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/jugadores")
    public ResponseEntity<Partida> unirseAPartida(@PathVariable String id, @RequestBody Usuario usuario) throws PartidaNoEncontradaException, JugadorYaAnadidoException {
        Partida p = partidaServicio.anadirJugador(id, usuario);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partida> getEstadoPartida(
            @PathVariable String id,
            @RequestParam(required = false) Instant selloTemporal)
            throws PartidaNoEncontradaException {

        Partida p = partidaServicio.getPartida(id);
        if (selloTemporal != null && !p.getMarcaTemporal().equals(selloTemporal)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        return ResponseEntity.ok(p);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Partida> iniciarPartida(@PathVariable String id) throws PartidaNoEncontradaException, PartidaYaIniciadaException {
        Partida p = partidaServicio.iniciarPartida(id);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @DeleteMapping("/{idPartida}/jugadores/{idJugador}")
    public ResponseEntity<Void> abandonarPartida(
            @PathVariable String idPartida,
            @PathVariable String idJugador)
            throws PartidaNoEncontradaException, NoEstasEnLaPartidaException {

        partidaServicio.abandonarPartida(idPartida, idJugador);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{id}")
    public ResponseEntity<Partida> accion(@RequestBody Jugador j, @PathVariable String id, @RequestBody String accion, @RequestBody String accionSubirBajar) throws PartidaNoEncontradaException, NoEsTuTurnoException, AccionIlegalException, NoEstasEnLaPartidaException, SintaxisIncorrectaException {
        Partida p=partidaServicio.accion(id, accion, accionSubirBajar, j);

        return new ResponseEntity<>(p, HttpStatus.OK); //Hacer algo más arriba sobre si la partida acabó
    }
}
