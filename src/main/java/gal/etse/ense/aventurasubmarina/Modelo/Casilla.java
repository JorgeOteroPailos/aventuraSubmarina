package gal.etse.ense.aventurasubmarina.Modelo;

import java.util.ArrayList;
import java.util.List;

public class Casilla {
    public List<Tesoro> tesoros;

    public Casilla(int indice){
        tesoros=new ArrayList<>(5);
        tesoros.add(new Tesoro((indice/8) +1));
    }
}
