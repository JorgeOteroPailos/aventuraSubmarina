package gal.etse.ense.aventurasubmarina.Controlador;


import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.TokenRefrescoInvalidoException;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.UsuarioExistenteException;
import gal.etse.ense.aventurasubmarina.Modelo.Rol;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@NullMarked
@RestController
@RequestMapping("autenticacion")
public class AutenticacionControlador {



    private static final String REFRESH_TOKEN_COOKIE_NAME = "__Secure-RefreshToken";
    private final AutenticacionServicio autenticacion;
    private final UsuarioServicio usuarios;
    private final StringRedisTemplate redis;


    @Autowired
    public AutenticacionControlador(AutenticacionServicio autenticacion, UsuarioServicio usuarios, StringRedisTemplate redis) {
        this.autenticacion = autenticacion;
        this.usuarios = usuarios;
        this.redis=redis;
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> iniciarSesion(@RequestBody UsuarioDTO usuario) {

        DebugPrint.show("Entrando a iniciarSesion en el controlador");

        redis.opsForValue().set("aventura","");

        UsuarioDTO loggedUsuario = autenticacion.login(usuario);
        String refreshToken = autenticacion.regenerateTokenRefresco(usuario);
        String refreshPath = MvcUriComponentsBuilder.fromMethodName(AutenticacionControlador.class, "refresh", "").build().toUri().getPath();

        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .secure(true)
                .httpOnly(true)
                .sameSite(Cookie.SameSite.STRICT.toString())
                .path(refreshPath)
                .maxAge(Duration.ofDays(7))
                .build();

        return ResponseEntity.noContent()
                .headers(headers -> headers.setBearerAuth(loggedUsuario.password()))
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("refresh")
    public ResponseEntity<Void> refresh(@CookieValue(name = REFRESH_TOKEN_COOKIE_NAME) String refreshToken) throws TokenRefrescoInvalidoException {
        UsuarioDTO usuario = autenticacion.login(refreshToken);

        return iniciarSesion(usuario);
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
