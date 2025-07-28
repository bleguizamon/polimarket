package co.edu.poligran.polimarket.domain;

import static co.edu.poligran.polimarket.domain.EntregaTestSamples.*;
import static co.edu.poligran.polimarket.domain.InventarioTestSamples.*;
import static co.edu.poligran.polimarket.domain.MovimientoStockTestSamples.*;
import static co.edu.poligran.polimarket.domain.ProductoTestSamples.*;
import static co.edu.poligran.polimarket.domain.ProveedorTestSamples.*;
import static co.edu.poligran.polimarket.domain.TareaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poligran.polimarket.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Producto.class);
        Producto producto1 = getProductoSample1();
        Producto producto2 = new Producto();
        assertThat(producto1).isNotEqualTo(producto2);

        producto2.setId(producto1.getId());
        assertThat(producto1).isEqualTo(producto2);

        producto2 = getProductoSample2();
        assertThat(producto1).isNotEqualTo(producto2);
    }

    @Test
    void entregasTest() {
        Producto producto = getProductoRandomSampleGenerator();
        Entrega entregaBack = getEntregaRandomSampleGenerator();

        producto.addEntregas(entregaBack);
        assertThat(producto.getEntregases()).containsOnly(entregaBack);
        assertThat(entregaBack.getProducto()).isEqualTo(producto);

        producto.removeEntregas(entregaBack);
        assertThat(producto.getEntregases()).doesNotContain(entregaBack);
        assertThat(entregaBack.getProducto()).isNull();

        producto.entregases(new HashSet<>(Set.of(entregaBack)));
        assertThat(producto.getEntregases()).containsOnly(entregaBack);
        assertThat(entregaBack.getProducto()).isEqualTo(producto);

        producto.setEntregases(new HashSet<>());
        assertThat(producto.getEntregases()).doesNotContain(entregaBack);
        assertThat(entregaBack.getProducto()).isNull();
    }

    @Test
    void inventariosTest() {
        Producto producto = getProductoRandomSampleGenerator();
        Inventario inventarioBack = getInventarioRandomSampleGenerator();

        producto.addInventarios(inventarioBack);
        assertThat(producto.getInventarios()).containsOnly(inventarioBack);
        assertThat(inventarioBack.getProducto()).isEqualTo(producto);

        producto.removeInventarios(inventarioBack);
        assertThat(producto.getInventarios()).doesNotContain(inventarioBack);
        assertThat(inventarioBack.getProducto()).isNull();

        producto.inventarios(new HashSet<>(Set.of(inventarioBack)));
        assertThat(producto.getInventarios()).containsOnly(inventarioBack);
        assertThat(inventarioBack.getProducto()).isEqualTo(producto);

        producto.setInventarios(new HashSet<>());
        assertThat(producto.getInventarios()).doesNotContain(inventarioBack);
        assertThat(inventarioBack.getProducto()).isNull();
    }

    @Test
    void movimientosTest() {
        Producto producto = getProductoRandomSampleGenerator();
        MovimientoStock movimientoStockBack = getMovimientoStockRandomSampleGenerator();

        producto.addMovimientos(movimientoStockBack);
        assertThat(producto.getMovimientos()).containsOnly(movimientoStockBack);
        assertThat(movimientoStockBack.getProducto()).isEqualTo(producto);

        producto.removeMovimientos(movimientoStockBack);
        assertThat(producto.getMovimientos()).doesNotContain(movimientoStockBack);
        assertThat(movimientoStockBack.getProducto()).isNull();

        producto.movimientos(new HashSet<>(Set.of(movimientoStockBack)));
        assertThat(producto.getMovimientos()).containsOnly(movimientoStockBack);
        assertThat(movimientoStockBack.getProducto()).isEqualTo(producto);

        producto.setMovimientos(new HashSet<>());
        assertThat(producto.getMovimientos()).doesNotContain(movimientoStockBack);
        assertThat(movimientoStockBack.getProducto()).isNull();
    }

    @Test
    void proveedorTest() {
        Producto producto = getProductoRandomSampleGenerator();
        Proveedor proveedorBack = getProveedorRandomSampleGenerator();

        producto.setProveedor(proveedorBack);
        assertThat(producto.getProveedor()).isEqualTo(proveedorBack);

        producto.proveedor(null);
        assertThat(producto.getProveedor()).isNull();
    }

    @Test
    void tareaTest() {
        Producto producto = getProductoRandomSampleGenerator();
        Tarea tareaBack = getTareaRandomSampleGenerator();

        producto.setTarea(tareaBack);
        assertThat(producto.getTarea()).isEqualTo(tareaBack);

        producto.tarea(null);
        assertThat(producto.getTarea()).isNull();
    }
}
