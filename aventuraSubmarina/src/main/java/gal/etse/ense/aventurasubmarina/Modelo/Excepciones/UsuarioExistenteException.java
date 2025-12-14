package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

import gal.etse.ense.aventurasubmarina.Modelo.Usuario;

public class UsuarioExistenteException extends RuntimeException {
    private final Usuario user;

    public UsuarioExistenteException(Usuario user) {
        super("El usuario" + user.getNombre() + "ya existe!!");

        this.user = user;
    }

    public Usuario getUser() {
        return user;
    }
}
