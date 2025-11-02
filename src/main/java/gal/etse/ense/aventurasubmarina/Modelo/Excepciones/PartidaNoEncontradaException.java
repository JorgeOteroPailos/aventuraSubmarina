package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

public class PartidaNoEncontradaException extends Exception {
    private final String idPartida;

    public PartidaNoEncontradaException(String idPartida){
        this.idPartida=idPartida;
    }

    public String getId() {
        return idPartida;
    }
}
