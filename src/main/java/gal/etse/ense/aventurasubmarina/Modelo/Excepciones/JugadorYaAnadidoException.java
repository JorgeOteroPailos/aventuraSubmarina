package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

import gal.etse.ense.aventurasubmarina.Modelo.Usuario;

public class JugadorYaAnadidoException extends Throwable {
    private final String idPartida;

    private final Usuario u;

    public String getIdPartida() {
        return idPartida;
    }

    public Usuario getUsuario() {
        return u;
    }

    public JugadorYaAnadidoException(String idPartida, Usuario u){
        this.idPartida=idPartida;
        this.u=u;
    }
}
