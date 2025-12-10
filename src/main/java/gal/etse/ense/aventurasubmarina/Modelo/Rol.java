package gal.etse.ense.aventurasubmarina.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "roles")
@SuppressWarnings("unused")
public class Rol {

    @Id
    private String rolename;

    // IDs de roles incluidos (jerarquía)
    private Set<Rol> includes;

    // IDs de permisos
    private Set<Permiso> permisos;

    public Rol() { }

    public Rol(String rolename, Set<Rol> includes, Set<Permiso> permisos) {
        this.rolename = rolename;
        this.includes = includes;
        this.permisos = permisos;
    }

    public String getRolename() {
        return rolename;
    }

    public Rol setRolename(String rolename) {
        this.rolename = rolename;
        return this;
    }

    public Set<Rol> getIncludes() {
        return includes;
    }

    public Rol setIncludes(Set<Rol> includes) {
        this.includes = includes;
        return this;
    }

    public Set<Permiso> getPermisos() {
        return permisos;
    }

    public Rol setPermisos(Set<Permiso> permisos) {
        this.permisos = permisos;
        return this;
    }
}
