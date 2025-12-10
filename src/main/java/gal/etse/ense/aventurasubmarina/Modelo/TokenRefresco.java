package gal.etse.ense.aventurasubmarina.Modelo;

import org.springframework.data.annotation.Id;


public class TokenRefresco {
    @Id
    private String token;
    private String usuario;
    private long ttl;

    public TokenRefresco() { }

    public TokenRefresco(String token, String user, long ttl) {
        this.token = token;
        this.usuario = user;
        this.ttl = ttl;
    }

    public String getToken() {
        return token;
    }

    public TokenRefresco setToken(String token) {
        this.token = token;
        return this;
    }

    public String getUsuario() {
        return usuario;
    }

    public TokenRefresco setUsuario(String usuario) {
        this.usuario = usuario;
        return this;
    }

    public long getTtl() {
        return ttl;
    }

    public TokenRefresco setTtl(long ttl) {
        this.ttl = ttl;
        return this;
    }
}
