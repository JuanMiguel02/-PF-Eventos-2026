package lospolimorficos.boletopolis.models;

public class Cliente extends Usuario{
    private String numDocumento;

    public Cliente(String nombre, String apellido, String correo, String numTelefono, String contrasena, String numDocumento) {
        super(nombre, apellido, correo, numTelefono, contrasena);
        this.numDocumento = numDocumento;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }
}
