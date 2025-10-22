package gal.etse.ense.aventurasubmarina.Modelo;

import java.util.ArrayList;
import java.util.List;

public class Tablero {

    private int tamano=10;

    private int oxigeno=30;

    public final List<Casilla> casillas;

    public Tablero(int tamano){
        this.tamano=tamano;
        casillas=new ArrayList<>(tamano+10);
    }

    public Tablero(){
        casillas=new ArrayList<>(tamano+10);
    }
}
