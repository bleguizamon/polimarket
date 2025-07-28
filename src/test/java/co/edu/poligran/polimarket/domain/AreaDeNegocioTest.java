package co.edu.poligran.polimarket.domain;

import static co.edu.poligran.polimarket.domain.AreaDeNegocioTestSamples.*;
import static co.edu.poligran.polimarket.domain.VendedorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poligran.polimarket.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AreaDeNegocioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AreaDeNegocio.class);
        AreaDeNegocio areaDeNegocio1 = getAreaDeNegocioSample1();
        AreaDeNegocio areaDeNegocio2 = new AreaDeNegocio();
        assertThat(areaDeNegocio1).isNotEqualTo(areaDeNegocio2);

        areaDeNegocio2.setId(areaDeNegocio1.getId());
        assertThat(areaDeNegocio1).isEqualTo(areaDeNegocio2);

        areaDeNegocio2 = getAreaDeNegocioSample2();
        assertThat(areaDeNegocio1).isNotEqualTo(areaDeNegocio2);
    }

    @Test
    void vendedoresTest() {
        AreaDeNegocio areaDeNegocio = getAreaDeNegocioRandomSampleGenerator();
        Vendedor vendedorBack = getVendedorRandomSampleGenerator();

        areaDeNegocio.addVendedores(vendedorBack);
        assertThat(areaDeNegocio.getVendedores()).containsOnly(vendedorBack);
        assertThat(vendedorBack.getAreaDeNegocio()).isEqualTo(areaDeNegocio);

        areaDeNegocio.removeVendedores(vendedorBack);
        assertThat(areaDeNegocio.getVendedores()).doesNotContain(vendedorBack);
        assertThat(vendedorBack.getAreaDeNegocio()).isNull();

        areaDeNegocio.vendedores(new HashSet<>(Set.of(vendedorBack)));
        assertThat(areaDeNegocio.getVendedores()).containsOnly(vendedorBack);
        assertThat(vendedorBack.getAreaDeNegocio()).isEqualTo(areaDeNegocio);

        areaDeNegocio.setVendedores(new HashSet<>());
        assertThat(areaDeNegocio.getVendedores()).doesNotContain(vendedorBack);
        assertThat(vendedorBack.getAreaDeNegocio()).isNull();
    }
}
