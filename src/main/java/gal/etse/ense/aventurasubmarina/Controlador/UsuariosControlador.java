package gal.etse.ense.aventurasubmarina.Controlador;


import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import gal.etse.ense.aventurasubmarina.Servicios.UsuarioServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuarios")
public class UsuariosControlador {
    // Aquí implementaremos nuestros servicios REST
    private final UsuarioServicio usuarioServicio;

    public UsuariosControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario){
        Usuario usuarioGuardado = usuarioServicio.guardarUsuario(usuario);
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
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
}
