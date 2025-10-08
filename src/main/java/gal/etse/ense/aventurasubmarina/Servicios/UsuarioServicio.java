package gal.etse.ense.aventurasubmarina.Servicios;

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
        Usuario u=new Usuario(nombre);

    }

    public Usuario guardarUsuario(Usuario u){
        usuarioRepositorio.guardarUsuario(u);
        return u;
    }

    public static boolean eliminarPorId(String id){
        return UsuarioRepositorio.eliminarPorId(id);
    }
}
