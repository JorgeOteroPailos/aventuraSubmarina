package gal.etse.ense.aventurasubmarina.Modelo;

import java.util.ArrayList;
import java.util.List;

public class Tablero {

    private int tamano=10;

    public int oxigeno=30;

    public final List<Casilla> casillas;

    public Tablero(int tamano){
        this.tamano=tamano;
        casillas=new ArrayList<>(tamano+10);

        for(int i=0;i<tamano;i++){
            casillas.add(new Casilla(i)); //TODO: Hay que cambiarlo para que el tesoro sea aleatorio dentro de unas posibilidades
        }
    }

    public Tablero(){
        casillas=new ArrayList<>(tamano+10);

        for(int i=0;i<tamano;i++){
            casillas.add(new Casilla(i)); //TODO: Hay que cambiarlo para que el tesoro sea aleatorio dentro de unas posibilidades
        }
    }
}
