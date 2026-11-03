package modulo3_ventas.patrones;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DescuentoPorcentajeStrategyTest {

    @Test
    void aplicaElPorcentajeSobreElMontoBase() {
        DescuentoStrategy estrategia = new DescuentoPorcentajeStrategy(15);
        assertEquals(15.0, estrategia.aplicar(100.0), 0.0001);
    }

    @Test
    void ceroPorCientoNoDescuentaNada() {
        assertEquals(0.0, new DescuentoPorcentajeStrategy(0).aplicar(250.0), 0.0001);
    }

    @Test
    void cienPorCientoDescuentaTodoElMonto() {
        assertEquals(80.0, new DescuentoPorcentajeStrategy(100).aplicar(80.0), 0.0001);
    }

    @Test
    void montoBaseCeroDaDescuentoCero() {
        assertEquals(0.0, new DescuentoPorcentajeStrategy(30).aplicar(0.0), 0.0001);
    }

    @Test
    void aceptaPorcentajesConDecimales() {
        assertEquals(2.5, new DescuentoPorcentajeStrategy(12.5).aplicar(20.0), 0.0001);
    }

    @Test
    void descripcionMencionaElPorcentaje() {
        assertTrue(new DescuentoPorcentajeStrategy(15).getDescripcion().contains("15"));
    }
}
