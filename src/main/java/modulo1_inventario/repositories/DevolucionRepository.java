package modulo1_inventario.repositories;

import modulo1_inventario.models.DetalleDevolucion;
import modulo1_inventario.models.Devolucion;
import java.util.List;

public interface DevolucionRepository {
    Devolucion            buscarPorId(int id);
    List<Devolucion>      buscarTodas();
    List<Devolucion>      buscarPorTipo(Devolucion.TipoDevolucion tipo);
    List<DetalleDevolucion> buscarDetalles(int devolucionId);
    void                  guardar(Devolucion devolucion);
    void                  guardarDetalle(DetalleDevolucion detalle);
    void                  actualizarEstado(int id, Devolucion.EstadoDevolucion estado);
}