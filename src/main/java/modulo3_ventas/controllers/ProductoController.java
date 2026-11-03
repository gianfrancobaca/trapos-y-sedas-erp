package modulo3_ventas.controllers;

import core.Controller;
import modulo3_ventas.models.Producto;
import modulo3_ventas.repositories.*;
import java.util.List;

/**
 * Controlador del catálogo de productos para el módulo de ventas.
 * RF: Gestión de catálogo de productos en ventas.
 */
public class ProductoController extends Controller {

    private final ProductoRepository productoRepo;

    public ProductoController() {
        this.productoRepo = new ProductoRepositorySQL();
    }

    @Override
    public void run() {
        // Vista se integrará en sprint siguiente
    }

    public List<Producto> obtenerProductos() {
        return productoRepo.buscarActivos();
    }

    public List<Producto> obtenerProductosConStock() {
        return productoRepo.buscarConStock();
    }

    public List<Producto> obtenerPorCategoria(Producto.CategoriaProducto categoria) {
        return productoRepo.buscarPorCategoria(categoria);
    }

    public Producto buscarPorCodigo(String codigo) {
        return productoRepo.buscarPorCodigo(codigo);
    }

    public Producto buscarPorId(int id) {
        return productoRepo.buscarPorId(id);
    }

    public void registrarProducto(Producto producto) {
        productoRepo.guardar(producto);
    }

    public void actualizarProducto(Producto producto) {
        productoRepo.actualizar(producto);
    }

    public void desactivarProducto(int id) {
        productoRepo.eliminar(id);
    }
}
