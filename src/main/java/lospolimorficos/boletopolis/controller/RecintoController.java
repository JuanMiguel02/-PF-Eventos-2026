package lospolimorficos.boletopolis.controller;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Recinto;
import lospolimorficos.boletopolis.repositorios.RecintoRepositorio;

public class RecintoController {
    private final RecintoRepositorio recintoRepositorio = RecintoRepositorio.getInstancia();

    public boolean registrarRecinto(Recinto recinto){
        return recintoRepositorio.registrarRecinto(recinto);
    }

    public boolean eliminarRecinto(Recinto recinto){
        return recintoRepositorio.eliminarRecinto(recinto);
    }

    public ObservableList<Recinto> getRecintos(){
        return recintoRepositorio.getRecintos();
    }
}
