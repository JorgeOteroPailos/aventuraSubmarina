package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

import gal.etse.ense.aventurasubmarina.Modelo.Usuario;

public class JugadorYaAnadidoException extends Throwable {
    private final String id;

    private final Usuario u;

    public String getId() {
        return id;
    }

    public Usuario getUsuario() {
        return u;
    }

    public JugadorYaAnadidoException(String idPartida, Usuario u){
        this.id=idPartida;
        this.u=u;
    }
}
