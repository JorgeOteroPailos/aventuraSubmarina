package gal.etse.ense.aventurasubmarina.Controlador;


import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import gal.etse.ense.aventurasubmarina.Modelo.UsuarioDTO;
import gal.etse.ense.aventurasubmarina.Servicios.UsuarioServicio;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuarios")
@NullMarked
public class UsuariosControlador {
    // Aquí implementaremos nuestros servicios REST
    private final UsuarioServicio usuarioServicio;

    public UsuariosControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }



    // Habría patch o put, pero de momento los usuarios no tienen atributos
    // suficientes para que esta lógica tenga sentido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String id) {
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
    public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable String id){
        UsuarioDTO u=UsuarioDTO.from(usuarioServicio.getUsuario(id));
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> getUsuariosTodos(Pageable pageable){
        Page<UsuarioDTO> lista = usuarioServicio.getUsuariosTodos(pageable);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }


}
