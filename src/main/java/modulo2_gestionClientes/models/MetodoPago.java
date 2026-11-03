package modulo2_gestionClientes.models;

public class MetodoPago {
    private int idMetodoPago;
    private String tipo;
    private String descripcion;
    private Cliente cliente;

    public MetodoPago() {}

    public MetodoPago(int idMetodoPago, String tipo, String descripcion, Cliente cliente) {
        this.idMetodoPago = idMetodoPago;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.cliente = cliente;
    }

    public int getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(int idMetodoPago) { this.idMetodoPago = idMetodoPago; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    @Override
    public String toString() {
        return "MetodoPago{" +
                "idMetodoPago=" + idMetodoPago +
                ", tipo='" + tipo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}