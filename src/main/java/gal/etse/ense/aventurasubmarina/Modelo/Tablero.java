package gal.etse.ense.aventurasubmarina.Modelo;

import java.util.ArrayList;
import java.util.List;

public class Tablero {

    private int tamano;

    private List<Casilla> casillas;

    public Tablero(int tamano){
        this.tamano=tamano;
        casillas=new ArrayList<>(tamano+10);
    }
}
