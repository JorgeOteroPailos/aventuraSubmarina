package gal.etse.ense.aventurasubmarina.Repositorio;

import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@NullMarked
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {
    Optional<Usuario> findUsuarioByNombre(String name);

    Page<Usuario> findAll(Pageable pageRequest);
}