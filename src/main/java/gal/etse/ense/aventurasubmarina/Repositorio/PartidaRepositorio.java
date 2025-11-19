package gal.etse.ense.aventurasubmarina.Repositorio;

import gal.etse.ense.aventurasubmarina.Modelo.Partida;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface PartidaRepositorio extends JpaRepository<Partida, String> {

}

