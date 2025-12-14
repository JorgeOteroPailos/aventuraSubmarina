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
        casillas=new ArrayList<>(tamano+10);

        List<Casilla> casillas1 = new ArrayList<>(12);
        List<Casilla> casillas2 = new ArrayList<>(12);
        List<Casilla> casillas3 = new ArrayList<>(12);
        List<Casilla> casillas4 = new ArrayList<>(12);

        for(int i=0;i<12;i++){
            casillas1.add(new Casilla());
            casillas2.add(new Casilla());
            casillas3.add(new Casilla());
            casillas4.add(new Casilla());
        }

        for(Casilla casilla:casillas1){
            for(int i=0;i<4;i++){
                casilla.tesoros.add(new Tesoro(1,i));
                casilla.tesoros.add(new Tesoro(1,i));
            }
        }for(Casilla casilla:casillas2){
            for(int i=4;i<8;i++){
                casilla.tesoros.add(new Tesoro(2,i));
                casilla.tesoros.add(new Tesoro(2,i));
            }
        }for(Casilla casilla:casillas3){
            for(int i=8;i<12;i++){
                casilla.tesoros.add(new Tesoro(3,i));
                casilla.tesoros.add(new Tesoro(3,i));
            }
        }for(Casilla casilla:casillas4){
            for(int i=12;i<16;i++){
                casilla.tesoros.add(new Tesoro(4,i));
                casilla.tesoros.add(new Tesoro(4,i));
            }
        }

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
