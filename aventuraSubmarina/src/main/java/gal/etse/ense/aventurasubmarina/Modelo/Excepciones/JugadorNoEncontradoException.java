package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

public class JugadorNoEncontradoException extends Exception {
    private final String idPartida;

    private final String idJugador;

    public JugadorNoEncontradoException(String idPartida, String idJugador){
        this.idJugador=idJugador;
        this.idPartida=idPartida;
    }

    public String getIdPartida() {
        return idPartida;
    }

    public String getIdJugador(){
        return idJugador;
    }
}
