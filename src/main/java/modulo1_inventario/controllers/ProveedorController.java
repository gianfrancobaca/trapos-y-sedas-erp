package modulo1_inventario.controllers;

import core.Controller;
import modulo1_inventario.models.Proveedor;
import modulo1_inventario.repositories.ProveedorRepository;
import modulo1_inventario.repositories.ProveedorRepositorySQL;
import java.util.List;

public class ProveedorController extends Controller {

    private final ProveedorRepository proveedorRepo;

    public ProveedorController() {
        this.proveedorRepo = new ProveedorRepositorySQL();
    }

    @Override public void run() {}

    public List<Proveedor> obtenerActivos()                    { return proveedorRepo.buscarActivos(); }
    public List<Proveedor> obtenerTodos()                      { return proveedorRepo.buscarTodos(); }
    public Proveedor       buscarPorId(int id)                 { return proveedorRepo.buscarPorId(id); }
    public Proveedor       buscarPorNumeroDocumento(String nd)  { return proveedorRepo.buscarPorNumeroDocumento(nd); }
    public void            registrar(Proveedor p)              { proveedorRepo.guardar(p); }
    public void            actualizar(Proveedor p)             { proveedorRepo.actualizar(p); }
    public void            desactivar(int id)                  { proveedorRepo.eliminar(id); }
}