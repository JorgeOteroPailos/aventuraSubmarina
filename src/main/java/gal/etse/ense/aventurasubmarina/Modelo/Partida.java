package gal.etse.ense.aventurasubmarina.Modelo;

import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.*;
import org.springframework.data.annotation.Id;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Partida {

    private static final int maximoJugadores=6;

    private final BitSet coloresUsados=new BitSet(maximoJugadores);

    protected final List<Jugador> jugadores=new ArrayList<>(maximoJugadores);

    public int turno=0;

    private int contadorRondas=1;

    private boolean empezada=false;

    private boolean rondaAcabada=false;

    private boolean partidaAcabada=false;

    public Tablero tablero;

    private Instant marcaTemporal;

    private Jugador jugadorInicial;

    @Id
    private String id;

    private int dado1;
    private int dado2;

    public String getId(){
        return id;
    }

    public Partida(String id){
        tablero=new Tablero();
        marcaTemporal= Instant.now();
        this.id=id;
    }

    public Partida(){
        tablero=new Tablero();
        marcaTemporal= Instant.now();
        this.id="hola";
    }


    public synchronized void anadirJugador(Usuario u) throws JugadorYaAnadidoException {
        for (Jugador j : jugadores){
            if (j.getUsuario().equals(u)){
                throw new JugadorYaAnadidoException(id, u);
            }
        }
        int colorDisponible= coloresUsados.nextClearBit(0);
        jugadores.add(new Jugador(this, u, colorDisponible));
        coloresUsados.set(colorDisponible, true);
        marcaTemporal=Instant.now();
    }

    public Instant getMarcaTemporal(){
        return marcaTemporal;
    }

    public synchronized void iniciar() throws PartidaYaIniciadaException {
        if(empezada){
            throw new PartidaYaIniciadaException(this.id);
        }
        empezada=true;
        lanzarDados();
        moverse(jugadores.getFirst(),dado1+dado2);
    }

    private void lanzarDados() {
        this.dado1 = ThreadLocalRandom.current().nextInt(3) + 1;
        this.dado2 = ThreadLocalRandom.current().nextInt(3) + 1;
    }

    private void reducirOxigeno(Jugador j){
        tablero.oxigeno-=j.tesorosCargando.size();
    }

    @Override
    public String toString(){
        StringBuilder r= new StringBuilder();
        r.append("JUGADORES:\n");
        for(Jugador j : jugadores){
            r.append(j.getUsuario().getNombre()).append("\n");
        }
        r.append("TABLERO:\n");

        int i=0;
        for(Casilla c: tablero.casillas){

            r.append(i++);
            for(Jugador j : jugadores){
                if(j.posicion==i){
                    r.append(j.getUsuario().getNombre()).append(",");
                }

            }
            r.append("\n");
        }
        return r.toString();
    }

    public synchronized void accion(String accion, String accionSubirBajar, Jugador j) throws NoEsTuTurnoException, AccionIlegalException, NoEstasEnLaPartidaException, SintaxisIncorrectaException {
        int indiceJ=jugadores.indexOf(j);
        if(indiceJ==-1){
            throw new NoEstasEnLaPartidaException(this.id, j.getUsuario(), HttpStatus.FORBIDDEN);
        }
        if(indiceJ!=turno){
            throw new NoEsTuTurnoException(jugadores.get(turno));
        }
        if(j.llegoAlSubmarino){
            throw new AccionIlegalException("accion","Ya llegaste al submarino, espera a tus compañeros");
        }

        switch(accion){
            case "nada":
                //Literal no se hace nada
                break;
            case "coger":
                if(tablero.casillas.get(j.posicion).tesoros.isEmpty()){
                    //No hay tesoros
                    throw new AccionIlegalException("coger","No hay tesoros en esta casilla");
                }else{
                    j.tesorosCargando.add(tablero.casillas.get(j.posicion).tesoros); //Añades los tesoros como si fueran 1
                    tablero.casillas.get(j.posicion).tesoros.clear(); //Eliminas el tesoro de la casilla
                }
                break;
            case "dejar":
                if(tablero.casillas.get(j.posicion).tesoros.isEmpty()){
                    tablero.casillas.get(j.posicion).tesoros.add(j.tesorosCargando.getLast().getLast());
                }else{
                    throw new AccionIlegalException("dejar", "No puedes dejar un tesoro en una casilla no vacía");
                }
                break;
            default:
                throw new SintaxisIncorrectaException("Comando no reconocido: "+accion);
        }

        switch(accionSubirBajar){
            case "bajar":
                if(j.subiendo) throw new AccionIlegalException("bajar","No puedes decidir bajar si ya estás subiendo");
                break;
            case "subir":
                j.subiendo=true;
                break;
            default:
                throw new SintaxisIncorrectaException("Comando no reconocido: "+accion);
        }

        if(tablero.oxigeno<1){
            finalizarRonda();
        }

        Jugador jSiguiente;
        if (jugadorInicial!=null && rondaAcabada){
            jSiguiente=jugadorInicial;
        }else if(jugadores.getLast().getUsuario().getNombre().equals(j.getUsuario().getNombre())){
            jSiguiente=jugadores.getFirst();
        }
        else jSiguiente=jugadores.get(jugadores.indexOf(j)+1);

        reducirOxigeno(jSiguiente);

        //Sujeto a cambios:
        lanzarDados();
        moverse(jSiguiente,dado1+dado2-jSiguiente.tesorosCargando.size());

        turno++;
    }

    public void abandonarPartida(String idJugador){
        Jugador jugadorEliminar=null;
        for(Jugador j:jugadores){
            if(j.getUsuario().getNombre().equals(idJugador)){
                jugadorEliminar=j;
            }
        }
        if(jugadorEliminar!=null) jugadores.remove(jugadorEliminar);
    }

    public void finalizarRonda(){
        tablero.oxigeno=30;
        int posicionMasAlejada=0;

        for(Jugador j: jugadores) {
            if(j.posicion==0) {
                for (List<Tesoro> tesoros : j.tesorosCargando) {
                    for (Tesoro t : tesoros) {
                        j.puntosGanados += t.getValor();
                    }
                }
            }else{
                if(posicionMasAlejada<j.posicion) jugadorInicial=j;

                List<Tesoro> tesorosDejados= new ArrayList<>();
                List<Tesoro> tesorosDejados2= new ArrayList<>();

                if(j.tesorosCargando.size()>3){
                    for(int i=0;i<3;i++){
                        tesorosDejados.addAll(j.tesorosCargando.get(i));
                    }
                    for(int i=3;i<j.tesorosCargando.size();i++){
                        tesorosDejados2.addAll(j.tesorosCargando.get(i));
                    }
                }else{
                    for(List<Tesoro> t: j.tesorosCargando){
                        tesorosDejados.addAll(t);
                    }
                }
                if(!tesorosDejados.isEmpty()) {
                    tablero.casillas.add(new Casilla());
                    tablero.casillas.getLast().tesoros = tesorosDejados;
                }
                if(!tesorosDejados2.isEmpty()){
                    tablero.casillas.add(new Casilla());
                    tablero.casillas.getLast().tesoros = tesorosDejados2;
                }
            }

            j.posicion=0;
            j.subiendo=false;
            j.llegoAlSubmarino=false;
            j.tesorosCargando.clear();
        }

        tablero.casillas.removeIf(casilla -> casilla.tesoros.isEmpty()); //Debería funcionar jeje
        rondaAcabada=true;
        contadorRondas++;

        if(contadorRondas==3){
            acabarPartida(); //No hace mucho ahora mismo
        }
    }

    public void moverse(Jugador j, int tirada){
        if(tirada>0){
            if(j.subiendo){
                tirada=-tirada;
            }

            int posicionDeseada=j.posicion+tirada;

            while(tablero.casillas.get(posicionDeseada).jugadorPresente!=null){
                if(j.subiendo) posicionDeseada--;
                else posicionDeseada++;

                if(posicionDeseada<0||posicionDeseada>tablero.casillas.size()-1) break;
            }

            if(posicionDeseada>tablero.casillas.size()-1){
                posicionDeseada=j.posicion;
            }else if(posicionDeseada<0){
                posicionDeseada=0;
                j.llegoAlSubmarino=true;
            }

            tablero.casillas.get(j.posicion).jugadorPresente=null;
            j.posicion=posicionDeseada;
            tablero.casillas.get(j.posicion).jugadorPresente=j;

        }
    }

    public void acabarPartida(){
        //No se
        ArrayList<Jugador> ganadores=new ArrayList<>();
        int dineros=-1;

        for(Jugador j: jugadores){
            if(j.puntosGanados>dineros){
                ganadores.clear();
                ganadores.add(j);
            }else if(j.puntosGanados==dineros){
                ganadores.add(j);
            }
        }

        System.out.println("Los ganadores son" + ganadores);

        partidaAcabada=true;
    }
}
