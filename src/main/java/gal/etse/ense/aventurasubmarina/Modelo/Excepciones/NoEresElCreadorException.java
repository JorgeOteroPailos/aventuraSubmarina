package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

public class NoEresElCreadorException extends RuntimeException {
    public NoEresElCreadorException(String message) {
        super(message);
    }

    public NoEresElCreadorException(){
        super("Solo el creador de la partida puede iniciarla");
    }
}
