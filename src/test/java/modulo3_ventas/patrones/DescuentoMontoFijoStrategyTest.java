package modulo3_ventas.patrones;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DescuentoMontoFijoStrategyTest {

    @Test
    void descuentaElMontoFijoCuandoElPrecioLoCubre() {
        DescuentoStrategy estrategia = new DescuentoMontoFijoStrategy(20.0);
        assertEquals(20.0, estrategia.aplicar(100.0), 0.0001);
    }

    @Test
    void noDescuentaMasQueElMontoBase() {
        assertEquals(15.0, new DescuentoMontoFijoStrategy(20.0).aplicar(15.0), 0.0001);
    }

    @Test
    void cuandoElMontoFijoIgualaAlPrecioDescuentaElPrecioCompleto() {
        assertEquals(20.0, new DescuentoMontoFijoStrategy(20.0).aplicar(20.0), 0.0001);
    }

    @Test
    void montoFijoCeroNoDescuentaNada() {
        assertEquals(0.0, new DescuentoMontoFijoStrategy(0.0).aplicar(100.0), 0.0001);
    }

    @Test
    void descripcionMencionaElMonto() {
        assertTrue(new DescuentoMontoFijoStrategy(20.0).getDescripcion().startsWith("Descuento fijo de S/ 20"));
    }
}
