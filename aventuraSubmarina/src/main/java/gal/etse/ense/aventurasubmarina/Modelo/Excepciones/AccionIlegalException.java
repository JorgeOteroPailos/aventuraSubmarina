package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

public class AccionIlegalException extends Exception{
    private final String accionNoPermitida;

    public String getAccionNoPermitida() {
        return accionNoPermitida;
    }

    public AccionIlegalException(String accionNoPermitida, String explicacion){
        super(explicacion);
        this.accionNoPermitida=accionNoPermitida;
    }
}
