package gal.etse.ense.aventurasubmarina.Repositorio;

import gal.etse.ense.aventurasubmarina.Modelo.TokenRefresco;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@NullMarked
@Repository
public interface TokenRefrescoRepositorio extends CrudRepository<TokenRefresco, String> {

    Optional<TokenRefresco> findByToken(String token);
    void deleteAllByUsuario(String nombre);
}
