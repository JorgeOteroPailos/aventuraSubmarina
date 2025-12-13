package gal.etse.ense.aventurasubmarina.Controlador;

import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExcepcionesControlador extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PartidaNoEncontradaException.class)
    public ErrorResponse handle(PartidaNoEncontradaException ex){
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        error.setDetail("La partida con identificador="+ex.getId()+" no se encuentra activa. ¿Quizás es el identificador erróneo?");
        error.setType(MvcUriComponentsBuilder.fromController(ExcepcionesControlador.class).pathSegment("error", "partida-no-encontrada").build().toUri());
        error.setTitle("Partida no encontrada");

        return ErrorResponse.builder(ex, error).build();
    }

    @ExceptionHandler(UsuarioExistenteException.class)
    public ErrorResponse handle(UsuarioExistenteException ex){
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        error.setDetail("Ya existe un usuario con identificador "+ex.getUser().getNombre() +". El identificador de usuario debe ser único, utiliza otro" );
        error.setType(MvcUriComponentsBuilder.fromController(ExcepcionesControlador.class).pathSegment("error", "usuario-ya-existente").build().toUri());
        error.setTitle("Usuario ya existente");

        return ErrorResponse.builder(ex, error).build();
    }

    @ExceptionHandler(JugadorNoEncontradoException.class)
    public ErrorResponse handle(JugadorNoEncontradoException ex){
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        error.setDetail("La el jugador con identificador="+ex.getIdJugador()+" no se encuentra en la partida "+ex.getIdPartida()+". ¿Quizás es el identificador erróneo?");
        error.setType(MvcUriComponentsBuilder.fromController(ExcepcionesControlador.class).pathSegment("error", "jugador-no-encontrada").build().toUri());
        error.setTitle("Jugador no encontrado");

        return ErrorResponse.builder(ex, error).build();
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ErrorResponse handle(UsuarioNoEncontradoException ex){
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        error.setDetail("La el jugador con nombre="+ex.getNombreUsuario()+" no se encuentra en la base de datos. ¿Quizás es el identificador erróneo?");
        error.setType(MvcUriComponentsBuilder.fromController(ExcepcionesControlador.class).pathSegment("error", "usuario-no-encontrada").build().toUri());
        error.setTitle("Usuario no encontrado");

        return ErrorResponse.builder(ex, error).build();
    }

    @ExceptionHandler(JugadorYaAnadidoException.class)
    public ErrorResponse handle(JugadorYaAnadidoException ex){
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        error.setDetail("El usuario con identificador="+ex.getUsuario().getNombre()+" ya está añadido a la partida con identificador="+ex.getIdPartida());
        error.setType(MvcUriComponentsBuilder.fromController(ExcepcionesControlador.class).pathSegment("error", "jugador-ya-anadido").build().toUri());
        error.setTitle("Jugador ya anadido");

        return ErrorResponse.builder(ex, error).build();
    }

    @ExceptionHandler(NoEstasEnLaPartidaException.class)
    public ErrorResponse handle(NoEstasEnLaPartidaException ex){
        ProblemDetail error = ProblemDetail.forStatus(ex.getCodigoError());
        error.setDetail("El usuario con identificador="+ex.getNombreJugador()+" no se encuentra en la partida con identificador="+ex.getIdPartida());
        error.setType(MvcUriComponentsBuilder.fromController(ExcepcionesControlador.class).pathSegment("error", "no-estas-en-la-partida").build().toUri());
        error.setTitle("No estás en la partida");
        return ErrorResponse.builder(ex, error).build();
    }

    @ExceptionHandler(PartidaYaIniciadaException.class)
    public ErrorResponse handle(PartidaYaIniciadaException ex){
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        error.setDetail("La partida con identificador="+ex.getId()+" ya ha sido iniciada.");
        error.setType(MvcUriComponentsBuilder.fromController(ExcepcionesControlador.class).pathSegment("error", "partida-ya-empezada").build().toUri());
        error.setTitle("Partida ya empezada");

        return ErrorResponse.builder(ex, error).build();
    }

    @ExceptionHandler(NoEsTuTurnoException.class)
    public ErrorResponse handle(NoEsTuTurnoException ex){
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.TOO_EARLY);
        error.setDetail("No es tu turno, sino el de "+ex.getJugadorConTurno()+".");
        error.setType(MvcUriComponentsBuilder.fromController(ExcepcionesControlador.class).pathSegment("error", "no-es-tu-turno").build().toUri());
        error.setTitle("No es tu turno");

        return ErrorResponse.builder(ex, error).build();
    }

}
