package gal.etse.ense.aventurasubmarina.Controlador;


import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.UsuarioExistenteException;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import gal.etse.ense.aventurasubmarina.Modelo.UsuarioDTO;
import gal.etse.ense.aventurasubmarina.Utils.DebugPrint;
import gal.etse.ense.aventurasubmarina.Servicios.AutenticacionServicio;
import gal.etse.ense.aventurasubmarina.Servicios.UsuarioServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Operation;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.time.Duration;

@NullMarked
@RestController
@RequestMapping("autenticacion")
public class AutenticacionControlador {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "RefreshToken";
    private final AutenticacionServicio autenticacion;
    private final UsuarioServicio usuarios;


    @Autowired
    public AutenticacionControlador(AutenticacionServicio autenticacion, UsuarioServicio usuarios) {
        this.autenticacion = autenticacion;
        this.usuarios = usuarios;
    }


    @Operation(
            summary = "Authenticate into the application",
            description = "Authenticates the usuario into the application and gets a JWT token in the Authorization header"
    )
    @SecurityRequirement(name = "")
    @PostMapping(
            path = "login",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )

    //@PreAuthorize("isAnonymous()")
    public ResponseEntity<Void> iniciarSesion(@RequestBody UsuarioDTO usuario) {

        DebugPrint.show("Entrando a iniciarSesion en el controlador");
        DebugPrint.show("[AuthController::login] ENTRADA");
        DebugPrint.show("[AuthController::login] Usuario = " + usuario.username());



        UsuarioDTO loggedUsuario = autenticacion.login(usuario);
        DebugPrint.show("[AuthController::login] JWT xerado");

        DebugPrint.show("[AuthController::login] Xerando refresh token");

        String refreshToken = autenticacion.regenerateTokenRefresco(usuario);
        DebugPrint.show("[AuthController::login] Refresh token creado = " + refreshToken);

        String refreshPath = "/autenticacion/refresh";
        DebugPrint.show("[AuthController::login] Cookie path = " + refreshPath);

        DebugPrint.show("ROLES DEL USUARIO: "+loggedUsuario.roles());


        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .secure(false)
                .httpOnly(true)
                .sameSite(Cookie.SameSite.STRICT.toString())
                .path(refreshPath)
                .maxAge(Duration.ofDays(7))
                .build();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getAuthorities().forEach(a -> System.out.println(a.getAuthority()));

        return ResponseEntity.noContent()
                .headers(headers -> headers.setBearerAuth(loggedUsuario.password()))
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();


    }

    @PostMapping("refresh")
    public ResponseEntity<Void> refresh(
            @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken
    ) {
        DebugPrint.show("Entrando e refresh en el controlador");
        DebugPrint.show("[AuthController::refresh] ENTRADA");
        DebugPrint.show("[AuthController::refresh] CookieValue refreshToken = " + refreshToken);

        if (refreshToken.isBlank()) {
            return ResponseEntity.status(403).build();
        }

        // OJO: este "usuario.password()" aquí es el JWT (sí, el nombre del campo es raro)
        UsuarioDTO usuario = autenticacion.login(refreshToken);

        return ResponseEntity.noContent()
                .headers(h -> h.setBearerAuth(usuario.password()))
                .build();
    }


    @PostMapping("logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        UsuarioDTO usuario = autenticacion.parseJWT(token.replaceFirst("^Bearer ", ""));
        autenticacion.invalidateTokens(usuario.username());
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, null).build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping(
            path = "register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public ResponseEntity<Usuario> register(@RequestBody UsuarioDTO usuario) throws UsuarioExistenteException {
        Usuario createdUsuario;

        createdUsuario = usuarios.crearUsuario(usuario);


        return ResponseEntity.created(MvcUriComponentsBuilder.fromMethodName(UsuariosControlador.class, "getUsuario", usuario.username()).build().toUri())
                .body(createdUsuario);
    }
}
