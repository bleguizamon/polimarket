package co.edu.poligran.polimarket.domain;

import static co.edu.poligran.polimarket.domain.AreaDeNegocioTestSamples.*;
import static co.edu.poligran.polimarket.domain.ClienteTestSamples.*;
import static co.edu.poligran.polimarket.domain.TareaTestSamples.*;
import static co.edu.poligran.polimarket.domain.VendedorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poligran.polimarket.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VendedorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vendedor.class);
        Vendedor vendedor1 = getVendedorSample1();
        Vendedor vendedor2 = new Vendedor();
        assertThat(vendedor1).isNotEqualTo(vendedor2);

        vendedor2.setId(vendedor1.getId());
        assertThat(vendedor1).isEqualTo(vendedor2);

        vendedor2 = getVendedorSample2();
        assertThat(vendedor1).isNotEqualTo(vendedor2);
    }

    @Test
    void clientesTest() {
        Vendedor vendedor = getVendedorRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        vendedor.addClientes(clienteBack);
        assertThat(vendedor.getClientes()).containsOnly(clienteBack);
        assertThat(clienteBack.getVendedor()).isEqualTo(vendedor);

        vendedor.removeClientes(clienteBack);
        assertThat(vendedor.getClientes()).doesNotContain(clienteBack);
        assertThat(clienteBack.getVendedor()).isNull();

        vendedor.clientes(new HashSet<>(Set.of(clienteBack)));
        assertThat(vendedor.getClientes()).containsOnly(clienteBack);
        assertThat(clienteBack.getVendedor()).isEqualTo(vendedor);

        vendedor.setClientes(new HashSet<>());
        assertThat(vendedor.getClientes()).doesNotContain(clienteBack);
        assertThat(clienteBack.getVendedor()).isNull();
    }

    @Test
    void tareasTest() {
        Vendedor vendedor = getVendedorRandomSampleGenerator();
        Tarea tareaBack = getTareaRandomSampleGenerator();

        vendedor.addTareas(tareaBack);
        assertThat(vendedor.getTareas()).containsOnly(tareaBack);
        assertThat(tareaBack.getVendedor()).isEqualTo(vendedor);

        vendedor.removeTareas(tareaBack);
        assertThat(vendedor.getTareas()).doesNotContain(tareaBack);
        assertThat(tareaBack.getVendedor()).isNull();

        vendedor.tareas(new HashSet<>(Set.of(tareaBack)));
        assertThat(vendedor.getTareas()).containsOnly(tareaBack);
        assertThat(tareaBack.getVendedor()).isEqualTo(vendedor);

        vendedor.setTareas(new HashSet<>());
        assertThat(vendedor.getTareas()).doesNotContain(tareaBack);
        assertThat(tareaBack.getVendedor()).isNull();
    }

    @Test
    void areaDeNegocioTest() {
        Vendedor vendedor = getVendedorRandomSampleGenerator();
        AreaDeNegocio areaDeNegocioBack = getAreaDeNegocioRandomSampleGenerator();

        vendedor.setAreaDeNegocio(areaDeNegocioBack);
        assertThat(vendedor.getAreaDeNegocio()).isEqualTo(areaDeNegocioBack);

        vendedor.areaDeNegocio(null);
        assertThat(vendedor.getAreaDeNegocio()).isNull();
    }
}
