package modulo2_gestionClientes.models;

import java.util.Date;

public class Factura {
    private int idFactura;
    private Date fecha;
    private double total;
    private String estado;
    private Pedido pedido;
    private Cliente cliente;

    public Factura() {}

    public Factura(int idFactura, Date fecha, double total, String estado, Pedido pedido, Cliente cliente) {
        this.idFactura = idFactura;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
        this.pedido = pedido;
        this.cliente = cliente;
    }

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    @Override
    public String toString() {
        return "Factura{" +
                "idFactura=" + idFactura +
                ", fecha=" + fecha +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                '}';
    }
}


