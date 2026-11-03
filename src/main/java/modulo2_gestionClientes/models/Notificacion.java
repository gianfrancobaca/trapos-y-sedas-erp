package modulo2_gestionClientes.models;

import java.util.Date;

public class Notificacion {
    private int idNotificacion;
    private String mensaje;
    private String tipo;
    private Date fecha;
    private boolean leida;
    private Cliente cliente;

    public Notificacion() {}

    public Notificacion(int idNotificacion, String mensaje, String tipo, Date fecha, boolean leida, Cliente cliente) {
        this.idNotificacion = idNotificacion;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.fecha = fecha;
        this.leida = leida;
        this.cliente = cliente;
    }

    public int getIdNotificacion() { return idNotificacion; }
    public void setIdNotificacion(int idNotificacion) { this.idNotificacion = idNotificacion; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public boolean isLeida() { return leida; }
    public void setLeida(boolean leida) { this.leida = leida; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    @Override
    public String toString() {
        return "Notificacion{" +
                "idNotificacion=" + idNotificacion +
                ", mensaje='" + mensaje + '\'' +
                ", tipo='" + tipo + '\'' +
                ", fecha=" + fecha +
                ", leida=" + leida +
                '}';
    }
}


