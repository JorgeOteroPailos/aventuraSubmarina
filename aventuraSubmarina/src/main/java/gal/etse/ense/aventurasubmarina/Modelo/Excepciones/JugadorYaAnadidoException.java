package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

import gal.etse.ense.aventurasubmarina.Modelo.Usuario;

public class JugadorYaAnadidoException extends Exception{

    private final String idPartida;

    private final Usuario usuario;

    public String getIdPartida(){
        return idPartida;
    }

    public Usuario getUsuario(){
        return usuario;
    }

    public JugadorYaAnadidoException(String idPartida, Usuario usuario){
        this.idPartida=idPartida;
        this.usuario=usuario;
    }
}
