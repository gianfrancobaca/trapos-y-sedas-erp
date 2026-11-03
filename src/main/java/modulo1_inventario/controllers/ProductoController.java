package modulo1_inventario.controllers;

import core.Controller;
import modulo1_inventario.models.Categoria;
import modulo1_inventario.models.Producto;
import modulo1_inventario.repositories.*;
import java.util.List;

public class ProductoController extends Controller {

    private final ProductoRepository  productoRepo;
    private final CategoriaRepository categoriaRepo;

    public ProductoController() {
        this.productoRepo  = new ProductoRepositorySQL();
        this.categoriaRepo = new CategoriaRepositorySQL();
    }

    @Override public void run() {}

    public List<Producto> obtenerActivos()                        { return productoRepo.buscarActivos(); }
    public List<Producto> obtenerTodos()                          { return productoRepo.buscarTodos(); }
    public List<Producto> obtenerPorCategoria(int categoriaId)    { return productoRepo.buscarPorCategoria(categoriaId); }
    public List<Producto> obtenerConStockBajo()                   { return productoRepo.buscarConStockBajo(); }
    public Producto       buscarPorId(int id)                     { return productoRepo.buscarPorId(id); }
    public Producto       buscarPorCodigo(String codigo)          { return productoRepo.buscarPorCodigo(codigo); }
    public List<Categoria> obtenerCategorias()                    { return categoriaRepo.buscarActivas(); }

    public void registrar(Producto producto) {
        if (productoRepo.buscarPorCodigo(producto.getCodigo()) != null)
            throw new IllegalArgumentException("Ya existe un producto con el código: " + producto.getCodigo());
        productoRepo.guardar(producto);
    }

    public void actualizar(Producto producto)  { productoRepo.actualizar(producto); }
    public void desactivar(int id)             { productoRepo.eliminar(id); }

    public void guardarCategoria(Categoria categoria)   { categoriaRepo.guardar(categoria); }
    public void actualizarCategoria(Categoria categoria){ categoriaRepo.actualizar(categoria); }
    public void eliminarCategoria(int id)               { categoriaRepo.eliminar(id); }
}