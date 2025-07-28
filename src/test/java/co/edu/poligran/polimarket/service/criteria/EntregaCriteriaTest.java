package co.edu.poligran.polimarket.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EntregaCriteriaTest {

    @Test
    void newEntregaCriteriaHasAllFiltersNullTest() {
        var entregaCriteria = new EntregaCriteria();
        assertThat(entregaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void entregaCriteriaFluentMethodsCreatesFiltersTest() {
        var entregaCriteria = new EntregaCriteria();

        setAllFilters(entregaCriteria);

        assertThat(entregaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void entregaCriteriaCopyCreatesNullFilterTest() {
        var entregaCriteria = new EntregaCriteria();
        var copy = entregaCriteria.copy();

        assertThat(entregaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(entregaCriteria)
        );
    }

    @Test
    void entregaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var entregaCriteria = new EntregaCriteria();
        setAllFilters(entregaCriteria);

        var copy = entregaCriteria.copy();

        assertThat(entregaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(entregaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var entregaCriteria = new EntregaCriteria();

        assertThat(entregaCriteria).hasToString("EntregaCriteria{}");
    }

    private static void setAllFilters(EntregaCriteria entregaCriteria) {
        entregaCriteria.id();
        entregaCriteria.fecha();
        entregaCriteria.estado();
        entregaCriteria.tareaId();
        entregaCriteria.clienteId();
        entregaCriteria.productoId();
        entregaCriteria.distinct();
    }

    private static Condition<EntregaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFecha()) &&
                condition.apply(criteria.getEstado()) &&
                condition.apply(criteria.getTareaId()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getProductoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EntregaCriteria> copyFiltersAre(EntregaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFecha(), copy.getFecha()) &&
                condition.apply(criteria.getEstado(), copy.getEstado()) &&
                condition.apply(criteria.getTareaId(), copy.getTareaId()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getProductoId(), copy.getProductoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
