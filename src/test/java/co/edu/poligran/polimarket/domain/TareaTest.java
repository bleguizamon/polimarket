package co.edu.poligran.polimarket.domain;

import static co.edu.poligran.polimarket.domain.EntregaTestSamples.*;
import static co.edu.poligran.polimarket.domain.ProductoTestSamples.*;
import static co.edu.poligran.polimarket.domain.TareaTestSamples.*;
import static co.edu.poligran.polimarket.domain.VendedorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poligran.polimarket.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TareaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tarea.class);
        Tarea tarea1 = getTareaSample1();
        Tarea tarea2 = new Tarea();
        assertThat(tarea1).isNotEqualTo(tarea2);

        tarea2.setId(tarea1.getId());
        assertThat(tarea1).isEqualTo(tarea2);

        tarea2 = getTareaSample2();
        assertThat(tarea1).isNotEqualTo(tarea2);
    }

    @Test
    void productosTest() {
        Tarea tarea = getTareaRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        tarea.addProductos(productoBack);
        assertThat(tarea.getProductos()).containsOnly(productoBack);
        assertThat(productoBack.getTarea()).isEqualTo(tarea);

        tarea.removeProductos(productoBack);
        assertThat(tarea.getProductos()).doesNotContain(productoBack);
        assertThat(productoBack.getTarea()).isNull();

        tarea.productos(new HashSet<>(Set.of(productoBack)));
        assertThat(tarea.getProductos()).containsOnly(productoBack);
        assertThat(productoBack.getTarea()).isEqualTo(tarea);

        tarea.setProductos(new HashSet<>());
        assertThat(tarea.getProductos()).doesNotContain(productoBack);
        assertThat(productoBack.getTarea()).isNull();
    }

    @Test
    void entregasTest() {
        Tarea tarea = getTareaRandomSampleGenerator();
        Entrega entregaBack = getEntregaRandomSampleGenerator();

        tarea.addEntregas(entregaBack);
        assertThat(tarea.getEntregases()).containsOnly(entregaBack);
        assertThat(entregaBack.getTarea()).isEqualTo(tarea);

        tarea.removeEntregas(entregaBack);
        assertThat(tarea.getEntregases()).doesNotContain(entregaBack);
        assertThat(entregaBack.getTarea()).isNull();

        tarea.entregases(new HashSet<>(Set.of(entregaBack)));
        assertThat(tarea.getEntregases()).containsOnly(entregaBack);
        assertThat(entregaBack.getTarea()).isEqualTo(tarea);

        tarea.setEntregases(new HashSet<>());
        assertThat(tarea.getEntregases()).doesNotContain(entregaBack);
        assertThat(entregaBack.getTarea()).isNull();
    }

    @Test
    void vendedorTest() {
        Tarea tarea = getTareaRandomSampleGenerator();
        Vendedor vendedorBack = getVendedorRandomSampleGenerator();

        tarea.setVendedor(vendedorBack);
        assertThat(tarea.getVendedor()).isEqualTo(vendedorBack);

        tarea.vendedor(null);
        assertThat(tarea.getVendedor()).isNull();
    }
}
