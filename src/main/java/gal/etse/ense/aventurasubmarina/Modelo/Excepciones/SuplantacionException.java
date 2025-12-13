package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

public class SuplantacionException extends RuntimeException{
    public final String nombreUsuarioToken;
    public final String nombreUsuarioSuplantado;


    public SuplantacionException(String nombreUsuarioSuplantado, String nombreUsuarioToken) {
        super("Fallo con la autenticación, eres realmente "+nombreUsuarioSuplantado+" o eres "+nombreUsuarioToken+" ?");

        this.nombreUsuarioSuplantado=nombreUsuarioSuplantado;
        this.nombreUsuarioToken=nombreUsuarioToken;
    }


}
