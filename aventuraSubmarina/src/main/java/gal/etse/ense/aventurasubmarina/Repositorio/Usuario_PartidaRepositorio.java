package gal.etse.ense.aventurasubmarina.Repositorio;

import gal.etse.ense.aventurasubmarina.Modelo.PartidasAcabadas;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario_Partida;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

@NullMarked
public interface Usuario_PartidaRepositorio extends MongoRepository<Usuario_Partida, String>{
    List<Usuario_Partida> findAllByIdUsuario(String idUsuario);
}
