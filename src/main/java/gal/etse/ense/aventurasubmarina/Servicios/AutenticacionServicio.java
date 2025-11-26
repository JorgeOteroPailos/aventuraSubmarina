package gal.etse.ense.aventurasubmarina.Servicios;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;

import java.security.KeyPair;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

import gal.etse.ense.aventurasubmarina.Repositorio.UsuarioRepositorio;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario;

@Service
public class AutenticacionServicio {
    
/*
    private final AuthenticationManager autenticacionManager;
    private final KeyPair keyPair;
    private final UsuarioRepositorio usuarioRepositorio;
    private final RoleRepositorio roleRepositorio;
    private final RefreshTokenRepositorio refreshTokenRepositorio;

    @Value("${auth.jwt.ttl:PT15M}")
    private Duration tokenTTL;

    @Value("${auth.refresh.ttl:PT72H}")
    private Duration refreshTTL;

    @Autowired
    public AutenticacionServicio(
            AuthenticationManager autenticacionManager,
            KeyPair keyPair,
            UsuarioRepositorio usuarioRepositorio,
            RoleRepositorio roleRepositorio,
            RefreshTokenRepositorio refreshTokenRepositorio
    ) {
        this.autenticacionManager = autenticacionManager;
        this.keyPair = keyPair;
        this.usuarioRepositorio = usuarioRepositorio;
        this.roleRepositorio = roleRepositorio;
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
    public Usuario login(Usuario usuario) throws AuthenticationException {
        Authentication auth = autenticacionManager.authenticate(UsuarionamePasswordAuthenticationToken.unauthenticated(usuario.usuarioname(), usuario.password()));

        List<String> roles = auth.getAuthorities()
                .stream()
                .filter(authority -> authority instanceof SimpleGrantedAuthority)
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = Jwts.builder()
                .subject(auth.getName())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(tokenTTL)))
                .notBefore(Date.from(Instant.now()))
                .claim("roles", roles)
                .signWith(keyPair.getPrivate())
                .compact();

        return new Usuario(usuario.usuarioname(), token, new HashSet<>(roles));
    }

    public Usuario login(String refreshToken) throws InvalidRefreshTokenException {
        Optional<RefreshToken> token = refreshTokenRepositorio.findByToken(refreshToken);

        if (token.isPresent()) {
            var usuario = usuarioRepositorio.findByUsuarioname(token.get().getUsuario()).orElseThrow(() -> new UsuarionameNotFoundException(token.get().getUsuario()));

            return login(Usuario.from(usuario));
        }

        throw new InvalidRefreshTokenException(refreshToken);
    }

    public String regenerateRefreshToken(Usuario usuario) {
        UUID uuid = UUID.randomUUID();
        RefreshToken refreshToken = new RefreshToken(uuid.toString(), usuario.usuarioname(), refreshTTL.toSeconds());
        refreshTokenRepositorio.deleteAllByUsuario(usuario.usuarioname());
        refreshTokenRepositorio.save(refreshToken);

        return refreshToken.getToken();
    }

    public void invalidateTokens(String usuarioname) {
        refreshTokenRepositorio.deleteAllByUsuario(usuarioname);
    }

    public Usuario parseJWT(String token) throws JwtException {
        Claims claims = Jwts.parser()
                .verifyWith(keyPair.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String usuarioname = claims.getSubject();
        var usuario = usuarioRepositorio.findByUsuarioname(usuarioname);

        if (usuario.isPresent()) {
            return Usuario.from(usuario.get());
        } else {
            throw new UsuarionameNotFoundException("Usuarioname not found");
        }
    }

    public RoleHierarchy loadRoleHierarchy() {
        RoleHierarchyImpl.Builder builder = RoleHierarchyImpl.withRolePrefix("");

        roleRepositorio.findAll().forEach(role -> {
            if (!role.getIncludes().isEmpty()) {
                builder.role("ROLE_"+role.getRolename()).implies(
                        role.getIncludes().stream().map(i -> "ROLE_"+i.getRolename()).toArray(String[]::new)
                );
            }
            if (!role.getPermissions().isEmpty()) {
                builder.role("ROLE_"+role.getRolename()).implies(
                        role.getPermissions().stream().map(p -> p.getResource()+":"+p.getAction()).toArray(String[]::new)
                );
            }
        });

        return builder.build();
    }



 */
     
}
