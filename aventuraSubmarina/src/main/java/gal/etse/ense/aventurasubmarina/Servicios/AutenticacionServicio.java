package gal.etse.ense.aventurasubmarina.Servicios;



import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.TokenRefrescoInvalidoException;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.UsuarioNoEncontradoException;
import gal.etse.ense.aventurasubmarina.Modelo.TokenRefresco;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario;
import gal.etse.ense.aventurasubmarina.Modelo.UsuarioDTO;
import gal.etse.ense.aventurasubmarina.Utils.DebugPrint;
import gal.etse.ense.aventurasubmarina.Repositorio.RolesRepositorio;
import gal.etse.ense.aventurasubmarina.Repositorio.TokenRefrescoRepositorio;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import io.jsonwebtoken.Jwts;

import java.security.KeyPair;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

import gal.etse.ense.aventurasubmarina.Repositorio.UsuarioRepositorio;

@Service
public class AutenticacionServicio {

    private final AuthenticationManager autenticacionManager;
    private final KeyPair keyPair;
    private final UsuarioRepositorio usuarioRepositorio;
    private final RolesRepositorio rolesRepositorio;
    private final TokenRefrescoRepositorio refreshTokenRepositorio;

    @Value("${auth.jwt.ttl:PT30M}")
    private Duration tokenTTL;

    @Value("${auth.refresh.ttl:PT2H}")
    private Duration refreshTTL;

    @Autowired
    public AutenticacionServicio(
            AuthenticationManager autenticacionManager,
            KeyPair keyPair,
            UsuarioRepositorio usuarioRepositorio,
            RolesRepositorio roleRepositorio,
            TokenRefrescoRepositorio refreshTokenRepositorio
    ) {
        this.autenticacionManager = autenticacionManager;
        this.keyPair = keyPair;
        this.usuarioRepositorio = usuarioRepositorio;
        this.rolesRepositorio = roleRepositorio;
        this.refreshTokenRepositorio = refreshTokenRepositorio;
    }

    @Retryable(
            maxRetries = 3,
            delay = 300,
            jitter = 50,
            timeUnit = TimeUnit.MILLISECONDS,
            multiplier = 2
    )

    @ConcurrencyLimit(10)
    public UsuarioDTO login(UsuarioDTO usuario) throws AuthenticationException {

        DebugPrint.show("Entrando al login desde contraseña");
        DebugPrint.show("[AuthService::login-password] ENTRADA");
        DebugPrint.show("[AuthService::login-password] Usuario = " + usuario.username());


        Authentication auth = autenticacionManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(usuario.username(), usuario.password()));

        DebugPrint.show("[AuthService::login-password] Authentication OK");


        List<String> roles = auth.getAuthorities()
                .stream()
                .filter(authority -> authority instanceof SimpleGrantedAuthority)
                .map(GrantedAuthority::getAuthority)
                .toList();

        DebugPrint.show("[AuthService::login-password] Xerando JWT (ttl=" + tokenTTL + ")");

        String token = Jwts.builder()
                .subject(auth.getName())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(tokenTTL)))
                .notBefore(Date.from(Instant.now()))
                .claim("roles", roles)
                .signWith(keyPair.getPrivate())
                .compact();

        DebugPrint.show("[AuthService::login-password] JWT xerado");

        System.out.println(roles);
        return new UsuarioDTO(usuario.username(), token, new HashSet<>(roles));
    }

    public UsuarioDTO login(String refreshToken) throws TokenRefrescoInvalidoException {

        DebugPrint.show("Entrando a login desde token de refresco");



        TokenRefresco token = refreshTokenRepositorio.findByToken(refreshToken)
                .orElseThrow(() -> new TokenRefrescoInvalidoException(refreshToken));

        var usuario = usuarioRepositorio.findUsuarioByNombre(token.getUsuario())
                .orElseThrow(() -> new UsuarioNoEncontradoException(token.getUsuario()));

        List<String> roles = usuario.getRoles()
                .stream()
                .map(r -> "ROLE_" + r.getRolename())
                .toList();

        String jwt = Jwts.builder()
                .subject(usuario.getNombre())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(tokenTTL)))
                .notBefore(Date.from(Instant.now()))
                .claim("roles", roles)
                .signWith(keyPair.getPrivate())
                .compact();

        return new UsuarioDTO(
                usuario.getNombre(),
                jwt,
                new HashSet<>(roles)
        );
    }



    public String regenerateTokenRefresco(UsuarioDTO usuario) {

        DebugPrint.show("Entrando a regenerarTokenRefresco");
        DebugPrint.show("[AuthService::regen-refresh] ENTRADA");
        DebugPrint.show("[AuthService::regen-refresh] Usuario = " + usuario.username());


        UUID uuid = UUID.randomUUID();
        TokenRefresco refreshToken = new TokenRefresco(uuid.toString(), usuario.username(), refreshTTL.toSeconds());
        DebugPrint.show("[AuthService::regen-refresh] Borrando refresh tokens antigos");

        refreshTokenRepositorio.deleteAllByUsuario(usuario.username());
        DebugPrint.show("[AuthService::regen-refresh] Gardando novo refresh token");

        refreshTokenRepositorio.save(refreshToken);

        DebugPrint.show("[AuthService::regen-refresh] Refresh token gardado = " + refreshToken.getToken());


        return refreshToken.getToken();
    }

    public void invalidateTokens(String usuarioname) {
        refreshTokenRepositorio.deleteAllByUsuario(usuarioname);
    }

    public UsuarioDTO parseJWT(String token) throws JwtException {
        Claims claims = Jwts.parser()
                .verifyWith(keyPair.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String usuarioname = claims.getSubject();
        var usuario = usuarioRepositorio.findUsuarioByNombre(usuarioname);

        if (usuario.isPresent()) {
            return UsuarioDTO.from(usuario.get());
        } else {
            throw new UsuarioNoEncontradoException("No se ha encontrado un usuario con este nombre");
        }
    }

    public RoleHierarchy loadRoleHierarchy() {
        RoleHierarchyImpl.Builder builder = RoleHierarchyImpl.withRolePrefix("");

        rolesRepositorio.findAll().forEach(rol -> {
            if (!rol.getIncludes().isEmpty()) {
                builder.role("ROLE_"+rol.getRolename()).implies(
                        rol.getIncludes().stream().map(i -> "ROLE_"+i.getRolename()).toArray(String[]::new)
                );
            }
            if (!rol.getPermisos().isEmpty()) {
                builder.role("ROLE_"+rol.getRolename()).implies(
                        rol.getPermisos().stream().map(p -> p.getResource()+":"+p.getAction()).toArray(String[]::new)
                );
            }
        });

        return builder.build();
    }

}
