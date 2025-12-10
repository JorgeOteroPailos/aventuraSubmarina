package gal.etse.ense.aventurasubmarina.Repositorio;

import gal.etse.ense.aventurasubmarina.Modelo.Partida;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.mongodb.repository.MongoRepository;

@NullMarked
public interface PartidaRepositorio extends MongoRepository<Partida, String> {

}

