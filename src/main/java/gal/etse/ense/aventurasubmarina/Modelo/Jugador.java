package gal.etse.ense.aventurasubmarina.Modelo;

import java.util.ArrayList;
import java.util.List;

//No debe confundirse con Usuario: un Jugador es la instancia concreta de un usuario en una partida, como su ficha
public class Jugador {
    private Usuario usuario;

    private Partida partida;

    public int posicion=0;

    public int puntosGanados=0;

    public boolean subiendo=false;

    public List<List<Tesoro>> tesorosCargando=new ArrayList<>();

    private int color;

    public Usuario getUsuario(){
        return usuario;
    }


    public int getColor() {
        return color;
    }

    public Jugador(Partida partida, Usuario usuario, int color){
        this.partida=partida;
        this.usuario=usuario;
        this.color=color;
    }

    public Jugador(){

    }


}
