package co.edu.poligran.polimarket.domain;

import static co.edu.poligran.polimarket.domain.ProductoTestSamples.*;
import static co.edu.poligran.polimarket.domain.ProveedorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poligran.polimarket.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProveedorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proveedor.class);
        Proveedor proveedor1 = getProveedorSample1();
        Proveedor proveedor2 = new Proveedor();
        assertThat(proveedor1).isNotEqualTo(proveedor2);

        proveedor2.setId(proveedor1.getId());
        assertThat(proveedor1).isEqualTo(proveedor2);

        proveedor2 = getProveedorSample2();
        assertThat(proveedor1).isNotEqualTo(proveedor2);
    }

    @Test
    void productosTest() {
        Proveedor proveedor = getProveedorRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        proveedor.addProductos(productoBack);
        assertThat(proveedor.getProductos()).containsOnly(productoBack);
        assertThat(productoBack.getProveedor()).isEqualTo(proveedor);

        proveedor.removeProductos(productoBack);
        assertThat(proveedor.getProductos()).doesNotContain(productoBack);
        assertThat(productoBack.getProveedor()).isNull();

        proveedor.productos(new HashSet<>(Set.of(productoBack)));
        assertThat(proveedor.getProductos()).containsOnly(productoBack);
        assertThat(productoBack.getProveedor()).isEqualTo(proveedor);

        proveedor.setProductos(new HashSet<>());
        assertThat(proveedor.getProductos()).doesNotContain(productoBack);
        assertThat(productoBack.getProveedor()).isNull();
    }
}
