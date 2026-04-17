package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Recinto;
import lospolimorficos.boletopolis.models.Usuario;

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

    public boolean actualizarRecinto(Recinto recintoActualizado) {
        for(int i = 0; i < recintos.size(); i++) {
            if(recintos.get(i).getIdRecinto().equals(recintoActualizado.getIdRecinto())) {

                recintos.set(i, recintoActualizado);
                return true;
            }
        }
        throw new IllegalArgumentException("Usuario no encontrado");
    }

    public ObservableList<Recinto> getRecintos() {
        return recintos;
    }

    public int contarRecintos(){
        return recintos.size();
    }
}
