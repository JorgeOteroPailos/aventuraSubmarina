package gal.etse.ense.aventurasubmarina.Modelo;

public class Permiso {
    private String id;
    private String resource;
    private String action;

    public Permiso() {}

    public String getId() {
        return this.id;
    }

    public Permiso setId(String id) {
        this.id = id;
        return this;
    }

    public String getResource() {
        return resource;
    }

    public Permiso setResource(String resource) {
        this.resource = resource;
        return this;
    }

    public String getAction() {
        return action;
    }

    public Permiso setAction(String action) {
        this.action = action;
        return this;
    }
}
