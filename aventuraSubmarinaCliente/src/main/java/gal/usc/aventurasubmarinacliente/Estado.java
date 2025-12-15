package gal.usc.aventurasubmarinacliente;

import gal.usc.aventurasubmarinacliente.modelo.Usuario;
import gal.usc.aventurasubmarinacliente.modelo.Partida;

public abstract class Estado{
    public static Usuario usuario;
    public final static String BASE_URL="http://localhost:8082";
    public static String token;
    public static Partida partida;
}
