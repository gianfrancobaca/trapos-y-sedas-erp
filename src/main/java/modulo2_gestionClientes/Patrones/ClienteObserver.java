package modulo2_gestionClientes.Patrones;

public interface ClienteObserver {
    void actualizar(String evento, Object dato);
}