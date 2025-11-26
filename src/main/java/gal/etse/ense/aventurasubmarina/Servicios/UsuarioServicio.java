package gal.etse.ense.aventurasubmarina.Servicios;

import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.UsuarioExistenteException;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import gal.etse.ense.aventurasubmarina.Repositorio.UsuarioRepositorio;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {
    private final UsuarioRepositorio usuarioRepositorio;


    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public void crearUsuario(String nombre){
        Usuario u=new Usuario(nombre, "contrasena");

    }

    public Usuario guardarUsuario(Usuario u) throws UsuarioExistenteException {
        var dbUser = usuarioRepositorio.findUsuarioByNombre(u.getNombre());
        if (dbUser.isPresent()) {
            throw new UsuarioExistenteException(dbUser.get());
        }

        //Role userRole = roleRepository.findByRolename("USER");
        return usuarioRepositorio.save(u);
    }

    public boolean eliminarPorId(String id){

        var dbUser = usuarioRepositorio.findUsuarioByNombre(id);
        if (dbUser.isPresent()) {
            usuarioRepositorio.deleteById(id);
            return true;
        }
        return false;
    }
}
