package gal.etse.ense.aventurasubmarina.Repositorio;

import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepositorio {
    public void guardarUsuario(Usuario u){
        //TODO
        System.out.println("Usuario "+u.getNombre()+" guardado (aún no pero llega bien al método");
    }

    public static boolean eliminarPorId(String id){
        return true;
    }
}
