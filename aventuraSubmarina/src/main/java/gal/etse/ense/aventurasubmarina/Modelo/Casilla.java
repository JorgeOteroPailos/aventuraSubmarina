package gal.etse.ense.aventurasubmarina.Modelo;

import java.util.ArrayList;
import java.util.List;

public class Casilla {

    public List<Tesoro> tesoros;
    public Jugador jugadorPresente=null;

    public Casilla(){
        tesoros=new ArrayList<>();
    }
}
