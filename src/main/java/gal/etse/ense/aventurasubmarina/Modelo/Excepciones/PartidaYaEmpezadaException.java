package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

public class PartidaYaEmpezadaException extends Exception{
    private final String idPartida;

    public PartidaYaEmpezadaException(String idPartida){
        this.idPartida=idPartida;
    }

    public String getId(){
        return idPartida;
    }
}
