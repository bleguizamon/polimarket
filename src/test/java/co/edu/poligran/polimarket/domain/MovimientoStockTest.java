package co.edu.poligran.polimarket.domain;

import static co.edu.poligran.polimarket.domain.InventarioTestSamples.*;
import static co.edu.poligran.polimarket.domain.MovimientoStockTestSamples.*;
import static co.edu.poligran.polimarket.domain.ProductoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poligran.polimarket.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MovimientoStockTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovimientoStock.class);
        MovimientoStock movimientoStock1 = getMovimientoStockSample1();
        MovimientoStock movimientoStock2 = new MovimientoStock();
        assertThat(movimientoStock1).isNotEqualTo(movimientoStock2);

        movimientoStock2.setId(movimientoStock1.getId());
        assertThat(movimientoStock1).isEqualTo(movimientoStock2);

        movimientoStock2 = getMovimientoStockSample2();
        assertThat(movimientoStock1).isNotEqualTo(movimientoStock2);
    }

    @Test
    void productoTest() {
        MovimientoStock movimientoStock = getMovimientoStockRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        movimientoStock.setProducto(productoBack);
        assertThat(movimientoStock.getProducto()).isEqualTo(productoBack);

        movimientoStock.producto(null);
        assertThat(movimientoStock.getProducto()).isNull();
    }

    @Test
    void inventarioTest() {
        MovimientoStock movimientoStock = getMovimientoStockRandomSampleGenerator();
        Inventario inventarioBack = getInventarioRandomSampleGenerator();

        movimientoStock.setInventario(inventarioBack);
        assertThat(movimientoStock.getInventario()).isEqualTo(inventarioBack);

        movimientoStock.inventario(null);
        assertThat(movimientoStock.getInventario()).isNull();
    }
}
