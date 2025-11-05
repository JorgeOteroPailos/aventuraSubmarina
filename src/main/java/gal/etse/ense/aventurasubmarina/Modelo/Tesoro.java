package gal.etse.ense.aventurasubmarina.Modelo;

public class Tesoro{
    private final int categoria;
    private final int valor;

    public int getCategoria() {
        return categoria;
    }

    public int getValor() {
        return valor;
    }

    public Tesoro (int categoria){
        this.categoria=categoria;
        valor=2*categoria; //TODO
    }

    protected Tesoro(){
        categoria=2;
        valor=2*categoria;
    }
}
