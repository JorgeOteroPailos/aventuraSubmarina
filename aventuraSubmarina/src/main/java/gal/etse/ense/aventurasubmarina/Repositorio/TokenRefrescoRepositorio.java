package gal.etse.ense.aventurasubmarina.Repositorio;

import gal.etse.ense.aventurasubmarina.Modelo.TokenRefresco;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@NullMarked
public interface TokenRefrescoRepositorio extends MongoRepository<gal.etse.ense.aventurasubmarina.Modelo.TokenRefresco, String> {

    Optional<TokenRefresco> findByToken(String token);
    void deleteAllByUsuario(String nombre);
}
