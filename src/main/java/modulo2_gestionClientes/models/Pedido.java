package modulo2_gestionClientes.models;

import java.util.Date;
import java.util.List;

public class Pedido {
    private int idPedido;
    private Date fecha;
    private String estado;
    private Cliente cliente;
    private List<DetallePedido> detalles;

    public Pedido() {}

    public Pedido(int idPedido, Date fecha, String estado, Cliente cliente, List<DetallePedido> detalles) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.estado = estado;
        this.cliente = cliente;
        this.detalles = detalles;
    }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }

    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", fecha=" + fecha +
                ", estado='" + estado + '\'' +
                ", cliente=" + (cliente != null ? cliente.getNombre() : "Sin cliente") +
                '}';
    }
}