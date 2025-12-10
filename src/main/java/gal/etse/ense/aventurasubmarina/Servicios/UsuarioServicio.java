package gal.etse.ense.aventurasubmarina.Servicios;

import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.UsuarioExistenteException;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.UsuarioNoEncontradoException;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import gal.etse.ense.aventurasubmarina.Modelo.UsuarioDTO;
import gal.etse.ense.aventurasubmarina.Repositorio.UsuarioRepositorio;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@NullMarked
@Service
public class UsuarioServicio implements UserDetailsService {
    private final UsuarioRepositorio usuarioRepositorio;


    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Usuario crearUsuario(Usuario u) throws UsuarioExistenteException {
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

    public Usuario getUsuario(String nombre) throws UsuarioNoEncontradoException{
        Optional<Usuario> usuario=usuarioRepositorio.findUsuarioByNombre(nombre);

        if(usuario.isPresent()){
            return usuario.get();
        }else{
            throw new UsuarioNoEncontradoException(nombre);
        }
    }

    public Page<UsuarioDTO> getUsuariosTodos(Pageable pageable) {
        return usuarioRepositorio.findAll(pageable)
                .map(UsuarioDTO::from);
    }

    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        return (UserDetails) usuarioRepositorio.findUsuarioByNombre(nombre).orElseThrow(() -> new UsernameNotFoundException(nombre));
    }

}
