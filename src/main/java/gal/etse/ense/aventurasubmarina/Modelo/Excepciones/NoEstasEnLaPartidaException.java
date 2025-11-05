package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import org.springframework.http.HttpStatus;

public class NoEstasEnLaPartidaException extends Exception{

    private final String idPartida;

    private final Usuario usuario;

    private final HttpStatus codigoError;

    public String getIdPartida(){
        return idPartida;
    }

    public HttpStatus getCodigoError(){return codigoError;}

    public Usuario getUsuario(){
        return usuario;
    }

    public NoEstasEnLaPartidaException(String idPartida, Usuario usuario, HttpStatus codigoError){
        this.idPartida=idPartida;
        this.usuario=usuario;
        this.codigoError=codigoError;
    }
}
