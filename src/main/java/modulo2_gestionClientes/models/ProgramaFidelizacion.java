package modulo2_gestionClientes.models;

public class ProgramaFidelizacion {
    private int idPrograma;
    private String nombre;
    private int puntosAcumulados;
    private String nivel;
    private Cliente cliente;

    public ProgramaFidelizacion() {}

    public ProgramaFidelizacion(int idPrograma, String nombre, int puntosAcumulados, String nivel, Cliente cliente) {
        this.idPrograma = idPrograma;
        this.nombre = nombre;
        this.puntosAcumulados = puntosAcumulados;
        this.nivel = nivel;
        this.cliente = cliente;
    }

    public int getIdPrograma() { return idPrograma; }
    public void setIdPrograma(int idPrograma) { this.idPrograma = idPrograma; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getPuntosAcumulados() { return puntosAcumulados; }
    public void setPuntosAcumulados(int puntosAcumulados) { this.puntosAcumulados = puntosAcumulados; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    @Override
    public String toString() {
        return "ProgramaFidelizacion{" +
                "idPrograma=" + idPrograma +
                ", nombre='" + nombre + '\'' +
                ", puntosAcumulados=" + puntosAcumulados +
                ", nivel='" + nivel + '\'' +
                '}';
    }
}