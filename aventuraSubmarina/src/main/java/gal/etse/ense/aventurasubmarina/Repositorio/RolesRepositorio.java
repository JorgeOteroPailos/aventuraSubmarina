package gal.etse.ense.aventurasubmarina.Repositorio;

import gal.etse.ense.aventurasubmarina.Modelo.Rol;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.mongodb.repository.MongoRepository;


@NullMarked
public interface RolesRepositorio extends MongoRepository<Rol, String> {

}