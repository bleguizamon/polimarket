package co.edu.poligran.polimarket.domain;

import static co.edu.poligran.polimarket.domain.ClienteTestSamples.*;
import static co.edu.poligran.polimarket.domain.EntregaTestSamples.*;
import static co.edu.poligran.polimarket.domain.VendedorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poligran.polimarket.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cliente.class);
        Cliente cliente1 = getClienteSample1();
        Cliente cliente2 = new Cliente();
        assertThat(cliente1).isNotEqualTo(cliente2);

        cliente2.setId(cliente1.getId());
        assertThat(cliente1).isEqualTo(cliente2);

        cliente2 = getClienteSample2();
        assertThat(cliente1).isNotEqualTo(cliente2);
    }

    @Test
    void entregasTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        Entrega entregaBack = getEntregaRandomSampleGenerator();

        cliente.addEntregas(entregaBack);
        assertThat(cliente.getEntregases()).containsOnly(entregaBack);
        assertThat(entregaBack.getCliente()).isEqualTo(cliente);

        cliente.removeEntregas(entregaBack);
        assertThat(cliente.getEntregases()).doesNotContain(entregaBack);
        assertThat(entregaBack.getCliente()).isNull();

        cliente.entregases(new HashSet<>(Set.of(entregaBack)));
        assertThat(cliente.getEntregases()).containsOnly(entregaBack);
        assertThat(entregaBack.getCliente()).isEqualTo(cliente);

        cliente.setEntregases(new HashSet<>());
        assertThat(cliente.getEntregases()).doesNotContain(entregaBack);
        assertThat(entregaBack.getCliente()).isNull();
    }

    @Test
    void vendedorTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        Vendedor vendedorBack = getVendedorRandomSampleGenerator();

        cliente.setVendedor(vendedorBack);
        assertThat(cliente.getVendedor()).isEqualTo(vendedorBack);

        cliente.vendedor(null);
        assertThat(cliente.getVendedor()).isNull();
    }
}
