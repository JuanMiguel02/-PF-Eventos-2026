package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Compra;

public final class CompraRepositorio {

    private final ObservableList<Compra> compras = FXCollections.observableArrayList();
    private static CompraRepositorio instancia;

    private CompraRepositorio() {}

    public static CompraRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new CompraRepositorio();
        }
        return instancia;
    }

    public ObservableList<Compra> getCompras() {
        return compras;
    }

    public boolean registrarCompra(Compra compra) {
        return compras.add(compra);
    }

    public int contarCompras(){
        return compras.size();
    }
}
