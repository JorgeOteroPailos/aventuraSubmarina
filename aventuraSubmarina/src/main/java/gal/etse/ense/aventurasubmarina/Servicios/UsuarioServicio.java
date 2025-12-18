package gal.etse.ense.aventurasubmarina.Servicios;

import gal.etse.ense.aventurasubmarina.Modelo.*;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.UsuarioExistenteException;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.UsuarioNoEncontradoException;
import gal.etse.ense.aventurasubmarina.Repositorio.PartidaRepositorio;
import gal.etse.ense.aventurasubmarina.Repositorio.UsuarioRepositorio;
import gal.etse.ense.aventurasubmarina.Repositorio.Usuario_PartidaRepositorio;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@NullMarked
@Service
public class UsuarioServicio implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final Usuario_PartidaRepositorio usuarioPartidaRepositorio;
    private final PartidaRepositorio partidaRepositorio;

    private final PasswordEncoder passwordEncoder;

    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio,
                           Usuario_PartidaRepositorio usuarioPartidaRepositorio,
                           PartidaRepositorio partidaRepositorio,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioPartidaRepositorio = usuarioPartidaRepositorio;
        this.partidaRepositorio = partidaRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario crearUsuario(UsuarioDTO u) throws UsuarioExistenteException {

        var dbUser = usuarioRepositorio.findUsuarioByNombre(u.username());
        if (dbUser.isPresent()) {
            throw new UsuarioExistenteException(dbUser.get());
        }

        String password=(passwordEncoder.encode(u.password()));
        Usuario u2=new Usuario(u.username(),u.roles()!=null);
        u2.setContrasena(password);

        return usuarioRepositorio.save(u2);
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

    public List<PartidasAcabadas> getPartidasAcabadas(String id){

        ArrayList<PartidasAcabadas> partidas=new ArrayList<>();
        List<Usuario_Partida> lista=usuarioPartidaRepositorio.findAllByIdUsuario(id);
        System.out.println(lista + "Con id: "+id);
        System.out.println(lista.size());

        for(Usuario_Partida up: lista){
            Optional<PartidasAcabadas> partida=partidaRepositorio.findById(up.idPartida+up.tiempoID);
            System.out.println("Partida: "+partida + "con id: " + up.idPartida+up.tiempoID);
            partida.ifPresent(partidas::add);
        }
        System.out.println(partidas);
        return partidas;
    }

    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.findUsuarioByNombre(nombre)
                .orElseThrow(() -> new UsernameNotFoundException(nombre));

        System.out.println(usuario.getNombre() + " " + usuario.getContrasena() + " " + usuario.getRoles());

        Set<Rol> roles = usuario.getRoles();
        boolean admin=roles.stream()
                .anyMatch(rol -> "ADMIN".equals(rol.getRolename()));

        if(usuario.getRoles()==null||!admin){
            return org.springframework.security.core.userdetails.User
                    .withUsername(usuario.getNombre())
                    .password(usuario.getContrasena())
                    .roles("USER")
                    .build();
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getNombre())
                .password(usuario.getContrasena())
                .roles("USER", "ADMIN")
                .build();
    }




}
