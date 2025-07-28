package co.edu.poligran.polimarket.domain;

import static co.edu.poligran.polimarket.domain.ClienteTestSamples.*;
import static co.edu.poligran.polimarket.domain.EntregaTestSamples.*;
import static co.edu.poligran.polimarket.domain.ProductoTestSamples.*;
import static co.edu.poligran.polimarket.domain.TareaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poligran.polimarket.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EntregaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Entrega.class);
        Entrega entrega1 = getEntregaSample1();
        Entrega entrega2 = new Entrega();
        assertThat(entrega1).isNotEqualTo(entrega2);

        entrega2.setId(entrega1.getId());
        assertThat(entrega1).isEqualTo(entrega2);

        entrega2 = getEntregaSample2();
        assertThat(entrega1).isNotEqualTo(entrega2);
    }

    @Test
    void tareaTest() {
        Entrega entrega = getEntregaRandomSampleGenerator();
        Tarea tareaBack = getTareaRandomSampleGenerator();

        entrega.setTarea(tareaBack);
        assertThat(entrega.getTarea()).isEqualTo(tareaBack);

        entrega.tarea(null);
        assertThat(entrega.getTarea()).isNull();
    }

    @Test
    void clienteTest() {
        Entrega entrega = getEntregaRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        entrega.setCliente(clienteBack);
        assertThat(entrega.getCliente()).isEqualTo(clienteBack);

        entrega.cliente(null);
        assertThat(entrega.getCliente()).isNull();
    }

    @Test
    void productoTest() {
        Entrega entrega = getEntregaRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        entrega.setProducto(productoBack);
        assertThat(entrega.getProducto()).isEqualTo(productoBack);

        entrega.producto(null);
        assertThat(entrega.getProducto()).isNull();
    }
}
