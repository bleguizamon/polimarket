package co.edu.poligran.polimarket.domain;

import static co.edu.poligran.polimarket.domain.InventarioTestSamples.*;
import static co.edu.poligran.polimarket.domain.MovimientoStockTestSamples.*;
import static co.edu.poligran.polimarket.domain.ProductoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poligran.polimarket.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class InventarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inventario.class);
        Inventario inventario1 = getInventarioSample1();
        Inventario inventario2 = new Inventario();
        assertThat(inventario1).isNotEqualTo(inventario2);

        inventario2.setId(inventario1.getId());
        assertThat(inventario1).isEqualTo(inventario2);

        inventario2 = getInventarioSample2();
        assertThat(inventario1).isNotEqualTo(inventario2);
    }

    @Test
    void movimientosTest() {
        Inventario inventario = getInventarioRandomSampleGenerator();
        MovimientoStock movimientoStockBack = getMovimientoStockRandomSampleGenerator();

        inventario.addMovimientos(movimientoStockBack);
        assertThat(inventario.getMovimientos()).containsOnly(movimientoStockBack);
        assertThat(movimientoStockBack.getInventario()).isEqualTo(inventario);

        inventario.removeMovimientos(movimientoStockBack);
        assertThat(inventario.getMovimientos()).doesNotContain(movimientoStockBack);
        assertThat(movimientoStockBack.getInventario()).isNull();

        inventario.movimientos(new HashSet<>(Set.of(movimientoStockBack)));
        assertThat(inventario.getMovimientos()).containsOnly(movimientoStockBack);
        assertThat(movimientoStockBack.getInventario()).isEqualTo(inventario);

        inventario.setMovimientos(new HashSet<>());
        assertThat(inventario.getMovimientos()).doesNotContain(movimientoStockBack);
        assertThat(movimientoStockBack.getInventario()).isNull();
    }

    @Test
    void productoTest() {
        Inventario inventario = getInventarioRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        inventario.setProducto(productoBack);
        assertThat(inventario.getProducto()).isEqualTo(productoBack);

        inventario.producto(null);
        assertThat(inventario.getProducto()).isNull();
    }
}
