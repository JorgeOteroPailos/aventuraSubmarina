package gal.etse.ense.aventurasubmarina.Repositorio;

import gal.etse.ense.aventurasubmarina.Modelo.PartidasAcabadas;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario_Partida;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario_PartidaDTO;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.mongodb.repository.MongoRepository;

@NullMarked
public interface Usuario_PartidaRepositorio extends MongoRepository<Usuario_Partida, Usuario_PartidaDTO>{
}
