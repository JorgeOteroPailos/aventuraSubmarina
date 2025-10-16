package gal.etse.ense.aventurasubmarina.Modelo;

import gal.etse.ense.aventurasubmarina.Repositorio.UsuarioRepositorio;

//No debe confundirse con Usuario: un Jugador es la instancia concreta de un usuario en una partida, como su ficha
public class Jugador {
    private Usuario usuario;

    private Partida partida;

    private int color;

    public Jugador(Partida partida, Usuario usuario, int color){
        this.partida=partida;
        this.usuario=usuario;
        this.color=color;
    }
}
