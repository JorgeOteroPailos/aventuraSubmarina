package gal.etse.ense.aventurasubmarina.Modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Tablero {

    private int tamano=40;

    public int oxigeno=30;

    public List<Casilla> casillas;

    public Tablero(int tamano){
        this.tamano=tamano;

        inicializarTablero();
    }

    public Tablero(){
        inicializarTablero();
    }

    private void inicializarTablero(){
        casillas=new ArrayList<>();
        casillas.add(new Casilla());

        List<Casilla> casillas1 = new ArrayList<>();
        List<Casilla> casillas2 = new ArrayList<>();
        List<Casilla> casillas3 = new ArrayList<>();
        List<Casilla> casillas4 = new ArrayList<>();

        for(int i=0;i<12;i++){
            casillas1.add(new Casilla());
            casillas2.add(new Casilla());
            casillas3.add(new Casilla());
            casillas4.add(new Casilla());
        }

        for(int i=0;i<7;i+=2)
            casillas1.get(i).tesoros.add(new Tesoro(1,i/2));
        for(int i=1;i<8;i+=2)
            casillas1.get(i).tesoros.add(new Tesoro(1,i/2));


        casillas2.getFirst().tesoros.add(new Tesoro(2,4));
        casillas2.get(1).tesoros.add(new Tesoro(2,4));
        casillas2.get(2).tesoros.add(new Tesoro(2,5));
        casillas2.get(3).tesoros.add(new Tesoro(2,5));
        casillas2.get(4).tesoros.add(new Tesoro(2,6));
        casillas2.get(5).tesoros.add(new Tesoro(2,6));
        casillas2.get(6).tesoros.add(new Tesoro(2,7));
        casillas2.get(7).tesoros.add(new Tesoro(2,7));

        casillas3.getFirst().tesoros.add(new Tesoro(2,8));
        casillas3.get(1).tesoros.add(new Tesoro(2,8));
        casillas3.get(2).tesoros.add(new Tesoro(2,9));
        casillas3.get(3).tesoros.add(new Tesoro(2,9));
        casillas3.get(4).tesoros.add(new Tesoro(2,10));
        casillas3.get(5).tesoros.add(new Tesoro(2,10));
        casillas3.get(6).tesoros.add(new Tesoro(2,11));
        casillas3.get(7).tesoros.add(new Tesoro(2,11));

        casillas4.getFirst().tesoros.add(new Tesoro(2,12));
        casillas4.get(1).tesoros.add(new Tesoro(2,12));
        casillas4.get(2).tesoros.add(new Tesoro(2,13));
        casillas4.get(3).tesoros.add(new Tesoro(2,13));
        casillas4.get(4).tesoros.add(new Tesoro(2,14));
        casillas4.get(5).tesoros.add(new Tesoro(2,14));
        casillas4.get(6).tesoros.add(new Tesoro(2,15));
        casillas4.get(7).tesoros.add(new Tesoro(2,15));

        Collections.shuffle(casillas1);
        Collections.shuffle(casillas2);
        Collections.shuffle(casillas3);
        Collections.shuffle(casillas4);

        casillas1.addAll(casillas2);
        casillas1.addAll(casillas3);
        casillas1.addAll(casillas4);

        casillas.addAll(casillas1);
    }
}
