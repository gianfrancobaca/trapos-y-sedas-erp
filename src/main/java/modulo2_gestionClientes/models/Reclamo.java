package modulo2_gestionClientes.models;

import java.util.Date;

public class Reclamo {
    private int idReclamo;
    private String motivo;
    private String estado;
    private Date fecha;
    private Cliente cliente;

    public Reclamo() {
    }

    public Reclamo(int idReclamo, String motivo, String estado, Date fecha, Cliente cliente) {
        this.idReclamo = idReclamo;
        this.motivo = motivo;
        this.estado = estado;
        this.fecha = fecha;
        this.cliente = cliente;
    }

    public int getIdReclamo() { return idReclamo; }
    public void setIdReclamo(int idReclamo) { this.idReclamo = idReclamo; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    @Override
    public String toString() {
        return "Reclamo{" +
                "idReclamo=" + idReclamo +
                ", motivo='" + motivo + '\'' +
                ", estado='" + estado + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}
