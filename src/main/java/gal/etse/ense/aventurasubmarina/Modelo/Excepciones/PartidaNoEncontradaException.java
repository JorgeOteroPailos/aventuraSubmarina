package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

public class PartidaNoEncontradaException extends Exception {
    private final String id;

    public PartidaNoEncontradaException(String id){
        this.id=id;
    }

    public String getId() {
        return id;
    }
}
