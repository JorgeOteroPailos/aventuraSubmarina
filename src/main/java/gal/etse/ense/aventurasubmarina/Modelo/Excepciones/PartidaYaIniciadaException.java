package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

public class PartidaYaIniciadaException extends Exception{
    private final String idPartida;

    public PartidaYaIniciadaException(String idPartida){
        this.idPartida=idPartida;
    }

    public String getId(){
        return idPartida;
    }
}
