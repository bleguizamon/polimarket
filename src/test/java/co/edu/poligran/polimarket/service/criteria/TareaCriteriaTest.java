package co.edu.poligran.polimarket.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TareaCriteriaTest {

    @Test
    void newTareaCriteriaHasAllFiltersNullTest() {
        var tareaCriteria = new TareaCriteria();
        assertThat(tareaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tareaCriteriaFluentMethodsCreatesFiltersTest() {
        var tareaCriteria = new TareaCriteria();

        setAllFilters(tareaCriteria);

        assertThat(tareaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tareaCriteriaCopyCreatesNullFilterTest() {
        var tareaCriteria = new TareaCriteria();
        var copy = tareaCriteria.copy();

        assertThat(tareaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tareaCriteria)
        );
    }

    @Test
    void tareaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tareaCriteria = new TareaCriteria();
        setAllFilters(tareaCriteria);

        var copy = tareaCriteria.copy();

        assertThat(tareaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tareaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tareaCriteria = new TareaCriteria();

        assertThat(tareaCriteria).hasToString("TareaCriteria{}");
    }

    private static void setAllFilters(TareaCriteria tareaCriteria) {
        tareaCriteria.id();
        tareaCriteria.descripcion();
        tareaCriteria.estado();
        tareaCriteria.productosId();
        tareaCriteria.entregasId();
        tareaCriteria.vendedorId();
        tareaCriteria.distinct();
    }

    private static Condition<TareaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getEstado()) &&
                condition.apply(criteria.getProductosId()) &&
                condition.apply(criteria.getEntregasId()) &&
                condition.apply(criteria.getVendedorId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TareaCriteria> copyFiltersAre(TareaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getEstado(), copy.getEstado()) &&
                condition.apply(criteria.getProductosId(), copy.getProductosId()) &&
                condition.apply(criteria.getEntregasId(), copy.getEntregasId()) &&
                condition.apply(criteria.getVendedorId(), copy.getVendedorId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
