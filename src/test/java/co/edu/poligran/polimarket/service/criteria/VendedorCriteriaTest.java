package co.edu.poligran.polimarket.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class VendedorCriteriaTest {

    @Test
    void newVendedorCriteriaHasAllFiltersNullTest() {
        var vendedorCriteria = new VendedorCriteria();
        assertThat(vendedorCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void vendedorCriteriaFluentMethodsCreatesFiltersTest() {
        var vendedorCriteria = new VendedorCriteria();

        setAllFilters(vendedorCriteria);

        assertThat(vendedorCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void vendedorCriteriaCopyCreatesNullFilterTest() {
        var vendedorCriteria = new VendedorCriteria();
        var copy = vendedorCriteria.copy();

        assertThat(vendedorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(vendedorCriteria)
        );
    }

    @Test
    void vendedorCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var vendedorCriteria = new VendedorCriteria();
        setAllFilters(vendedorCriteria);

        var copy = vendedorCriteria.copy();

        assertThat(vendedorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(vendedorCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var vendedorCriteria = new VendedorCriteria();

        assertThat(vendedorCriteria).hasToString("VendedorCriteria{}");
    }

    private static void setAllFilters(VendedorCriteria vendedorCriteria) {
        vendedorCriteria.id();
        vendedorCriteria.nombre();
        vendedorCriteria.autorizado();
        vendedorCriteria.clientesId();
        vendedorCriteria.tareasId();
        vendedorCriteria.areaDeNegocioId();
        vendedorCriteria.distinct();
    }

    private static Condition<VendedorCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getAutorizado()) &&
                condition.apply(criteria.getClientesId()) &&
                condition.apply(criteria.getTareasId()) &&
                condition.apply(criteria.getAreaDeNegocioId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<VendedorCriteria> copyFiltersAre(VendedorCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getAutorizado(), copy.getAutorizado()) &&
                condition.apply(criteria.getClientesId(), copy.getClientesId()) &&
                condition.apply(criteria.getTareasId(), copy.getTareasId()) &&
                condition.apply(criteria.getAreaDeNegocioId(), copy.getAreaDeNegocioId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
