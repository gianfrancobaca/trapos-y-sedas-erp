package modulo3_ventas.repositories;

import modulo3_ventas.models.Descuento;
import java.time.LocalDate;
import java.util.List;

/**
 * Contrato del repositorio de descuentos y promociones.
 * RF: Gestión de descuentos y promociones en ventas.
 */
public interface DescuentoRepository {
    Descuento       buscarPorId(int id);
    List<Descuento> buscarTodos();
    List<Descuento> buscarActivos();
    List<Descuento> buscarVigentes(LocalDate fecha);
    void            guardar(Descuento descuento);
    void            actualizar(Descuento descuento);
    void            eliminar(int id);
}
