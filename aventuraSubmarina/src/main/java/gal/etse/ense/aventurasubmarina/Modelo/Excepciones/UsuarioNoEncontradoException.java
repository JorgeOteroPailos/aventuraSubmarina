package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

import gal.etse.ense.aventurasubmarina.Modelo.Usuario;

public class UsuarioNoEncontradoException extends RuntimeException {

    private final String nombreUsuario;

    public UsuarioNoEncontradoException(String nombre) {
        super("El usuario" + nombre + "ya existe!!");

        this.nombreUsuario=nombre;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }
}
