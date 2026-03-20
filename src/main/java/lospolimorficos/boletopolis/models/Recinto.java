package lospolimorficos.boletopolis.models;

import java.util.UUID;

public class Recinto {

    private UUID idRecinto;
    private String nombre;
    private String direccion;
    private Ciudad ciudad;

    public Recinto(String direccion, String nombre, Ciudad ciudad) {
        this.idRecinto = UUID.randomUUID();
        this.direccion = direccion;
        this.nombre = nombre;
        this.ciudad = ciudad;
    }
}
