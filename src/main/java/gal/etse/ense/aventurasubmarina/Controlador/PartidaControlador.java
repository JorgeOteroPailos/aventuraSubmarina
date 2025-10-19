package gal.etse.ense.aventurasubmarina.Controlador;

import gal.etse.ense.aventurasubmarina.Modelo.Partida;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import gal.etse.ense.aventurasubmarina.Servicios.PartidaServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;

@RestController
@RequestMapping("/partidas")
public class PartidaControlador {

    private final PartidaServicio partidaServicio;

    public PartidaControlador(PartidaServicio partidaServicio) {
        this.partidaServicio = partidaServicio;
    }

    @PostMapping
    public ResponseEntity<Partida> crearPartida(@RequestBody Usuario usuario){
        Partida partidaCreada = partidaServicio.crearPartida(usuario);
        return new ResponseEntity<>(partidaCreada, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/jugadores")
    public ResponseEntity<Partida> unirseAPartida(@PathVariable String id, @RequestBody Usuario usuario){
        Partida p = partidaServicio.getPartida(id);
        if(p==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        p.anadirJugador(usuario);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partida> getEstadoPartida(
            @PathVariable String id,
            @RequestParam(required = false) Instant selloTemporal) {

        Partida p = partidaServicio.getPartida(id);
        if(p==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (selloTemporal != null && !p.getMarcaTemporal().equals(selloTemporal)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.ok(p);
    }

}
