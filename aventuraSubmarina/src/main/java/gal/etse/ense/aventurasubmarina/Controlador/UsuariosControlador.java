package gal.etse.ense.aventurasubmarina.Controlador;


import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.SuplantacionException;
import gal.etse.ense.aventurasubmarina.Modelo.PartidasAcabadas;
import gal.etse.ense.aventurasubmarina.Modelo.UsuarioDTO;
import gal.etse.ense.aventurasubmarina.Servicios.AutenticacionServicio;
import gal.etse.ense.aventurasubmarina.Servicios.UsuarioServicio;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("usuarios")
@NullMarked
public class UsuariosControlador {
    // Aquí implementaremos nuestros servicios REST
    private final UsuarioServicio usuarioServicio;
    private final AutenticacionServicio autenticacion;

    public UsuariosControlador(UsuarioServicio usuarioServicio, AutenticacionServicio autenticacion) {
        this.usuarioServicio = usuarioServicio;
        this.autenticacion = autenticacion;
    }



    // Habría patch o put, pero de momento los usuarios no tienen atributos
    // suficientes para que esta lógica tenga sentido
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String id, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {

        UsuarioDTO usuario = autenticacion.parseJWT(token.replaceFirst("^Bearer ", ""));
        if(!Objects.equals(usuario.username(), id)){
            throw new SuplantacionException(id, usuario.username());
        }

        boolean eliminado = usuarioServicio.eliminarPorId(id);

        if (eliminado) {
            // Si se eliminó correctamente, devolvemos 204 No Content
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // Si no se encontró el usuario, devolvemos 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable String id, Authentication autenticacion){
        if(!id.equals(autenticacion.getName())){
            throw new SuplantacionException(id, autenticacion.getName());
        }

        UsuarioDTO u=UsuarioDTO.from(usuarioServicio.getUsuario(id));

        u.addLink("self", "/usuarios/" + id, "GET");
        u.addLink("partidas-acabadas", "/usuarios/" + id + "/partidasAcabadas", "GET");
        u.addLink("create-partida", "/partidas", "POST");
        u.addLink("delete", "/usuarios/" + id, "DELETE");

        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UsuarioDTO>> getUsuariosTodos(Pageable pageable){
        Page<UsuarioDTO> lista = usuarioServicio.getUsuariosTodos(pageable);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}/partidasAcabadas")
    public ResponseEntity<List<PartidasAcabadas>> getPartidasAcabadas(
            @PathVariable String id,
            Authentication authentication
    ) {

        System.out.println("ENTRÉ EN GET PARTIDAS ACABADAS");
        if (!id.equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<PartidasAcabadas> listaPartidas = usuarioServicio.getPartidasAcabadas(id);
        return ResponseEntity.ok(listaPartidas);
    }



}
