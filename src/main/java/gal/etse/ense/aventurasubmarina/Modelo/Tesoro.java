package gal.etse.ense.aventurasubmarina.Modelo;

public class Tesoro{
    private final int categoria;
    private int valor;

    public int getCategoria() {
        return categoria;
    }

    public int getValor() {
        return valor;
    }

    public Tesoro (int categoria){
        this.categoria=categoria;
    }

    public Tesoro(int categoria, int valor){
        this.categoria=categoria;
        this.valor=valor;
    }
}
