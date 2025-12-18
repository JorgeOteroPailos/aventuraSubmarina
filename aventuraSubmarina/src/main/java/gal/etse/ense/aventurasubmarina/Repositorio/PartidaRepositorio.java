package gal.etse.ense.aventurasubmarina.Repositorio;

import gal.etse.ense.aventurasubmarina.Modelo.PartidasAcabadas;
import jakarta.servlet.http.Part;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@NullMarked
public interface PartidaRepositorio extends MongoRepository<PartidasAcabadas, String> {
    Optional<PartidasAcabadas> findById(String id);
}

