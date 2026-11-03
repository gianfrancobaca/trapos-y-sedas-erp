package modulo2_gestionClientes.Patrones;

import modulo2_gestionClientes.models.CategoriaCliente;

public class CategoriaClienteFactory {

    public static CategoriaCliente crear(String tipo) {
        switch (tipo.toUpperCase()) {
            case "VIP":
                return new CategoriaCliente(0, "VIP", "Cliente preferencial con maximos beneficios", 20.0);
            case "REGULAR":
                return new CategoriaCliente(0, "Regular", "Cliente con compras frecuentes", 10.0);
            case "NUEVO":
                return new CategoriaCliente(0, "Nuevo", "Cliente recien registrado", 5.0);
            default:
                return new CategoriaCliente(0, "General", "Cliente sin categoria asignada", 0.0);
        }
    }
}
