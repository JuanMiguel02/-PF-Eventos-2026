package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Recinto;

public final class RecintoRepositorio {

    private final ObservableList<Recinto> recintos = FXCollections.observableArrayList();
    private static RecintoRepositorio instancia;

    private RecintoRepositorio() {}

    public static RecintoRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new RecintoRepositorio();
        }
        return instancia;
    }

    public boolean registrarRecinto(Recinto recinto) {
        return recintos.add(recinto);
    }

    public boolean eliminarRecinto(Recinto recinto) {
        return recintos.remove(recinto);
    }

    public ObservableList<Recinto> getRecintos() {
        return recintos;
    }
}
