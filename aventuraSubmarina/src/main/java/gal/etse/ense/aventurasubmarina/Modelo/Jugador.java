package gal.etse.ense.aventurasubmarina.Modelo;

import java.util.ArrayList;
import java.util.List;

//No debe confundirse con Usuario: un Jugador es la instancia concreta de un usuario en una partida, como su ficha
public class Jugador {
    private UsuarioDTO usuario;



    public int posicion=0;

    public int puntosGanados=0;

    public boolean subiendo=false;

    public boolean llegoAlSubmarino=false;

    public List<List<Tesoro>> tesorosCargando=new ArrayList<>();

    private int color;

    public UsuarioDTO getUsuario(){
        return usuario;
    }


    public int getColor() {
        return color;
    }

    public Jugador(UsuarioDTO usuario, int color){
        this.usuario=usuario;
        this.color=color;
    }

    public Jugador(){

    }


}
