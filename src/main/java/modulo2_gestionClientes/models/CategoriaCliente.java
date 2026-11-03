package modulo2_gestionClientes.models;

public class CategoriaCliente {
    private int idCategoria;
    private String nombre;
    private String descripcion;
    private double descuento;

    public CategoriaCliente() {}

    public CategoriaCliente(int idCategoria, String nombre, String descripcion, double descuento) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descuento = descuento;
    }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    @Override
    public String toString() {
        return "CategoriaCliente{" +
                "idCategoria=" + idCategoria +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", descuento=" + descuento +
                '}';
    }
}