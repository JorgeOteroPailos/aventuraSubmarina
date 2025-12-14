package gal.etse.ense.aventurasubmarina.Repositorio;

import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@NullMarked
public interface UsuarioRepositorio extends MongoRepository<Usuario, String> {
    Optional<Usuario> findUsuarioByNombre(String name);

    Page<Usuario> findAll(Pageable pageRequest);
}