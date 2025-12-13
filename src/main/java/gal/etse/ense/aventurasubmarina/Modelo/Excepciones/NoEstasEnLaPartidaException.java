package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import org.springframework.http.HttpStatus;

public class NoEstasEnLaPartidaException extends Exception{

    private final String idPartida;

    private final String nombreJugador;



    public String getIdPartida(){
        return idPartida;
    }

    public String getNombreJugador(){
        return nombreJugador;
    }

    public NoEstasEnLaPartidaException(String idPartida, String nombreJugador){
        this.idPartida=idPartida;
        this.nombreJugador=nombreJugador;
    }
}
