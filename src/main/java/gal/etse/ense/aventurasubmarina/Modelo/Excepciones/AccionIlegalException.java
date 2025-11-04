package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

public class AccionIlegalException extends Exception{
    private final String accionNoPermitida;

    private final String explicacion;

    public String getAccionNoPermitida() {
        return accionNoPermitida;
    }

    public String getExplicacion(){
        return explicacion;
    }

    public AccionIlegalException(String accionNoPermitida, String explicacion){
        this.accionNoPermitida=accionNoPermitida;
        this.explicacion=explicacion;
    }
}
