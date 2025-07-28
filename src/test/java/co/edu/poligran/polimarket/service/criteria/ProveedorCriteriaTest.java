package co.edu.poligran.polimarket.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProveedorCriteriaTest {

    @Test
    void newProveedorCriteriaHasAllFiltersNullTest() {
        var proveedorCriteria = new ProveedorCriteria();
        assertThat(proveedorCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void proveedorCriteriaFluentMethodsCreatesFiltersTest() {
        var proveedorCriteria = new ProveedorCriteria();

        setAllFilters(proveedorCriteria);

        assertThat(proveedorCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void proveedorCriteriaCopyCreatesNullFilterTest() {
        var proveedorCriteria = new ProveedorCriteria();
        var copy = proveedorCriteria.copy();

        assertThat(proveedorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(proveedorCriteria)
        );
    }

    @Test
    void proveedorCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var proveedorCriteria = new ProveedorCriteria();
        setAllFilters(proveedorCriteria);

        var copy = proveedorCriteria.copy();

        assertThat(proveedorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(proveedorCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var proveedorCriteria = new ProveedorCriteria();

        assertThat(proveedorCriteria).hasToString("ProveedorCriteria{}");
    }

    private static void setAllFilters(ProveedorCriteria proveedorCriteria) {
        proveedorCriteria.id();
        proveedorCriteria.nombre();
        proveedorCriteria.telefono();
        proveedorCriteria.productosId();
        proveedorCriteria.distinct();
    }

    private static Condition<ProveedorCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getTelefono()) &&
                condition.apply(criteria.getProductosId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProveedorCriteria> copyFiltersAre(ProveedorCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getTelefono(), copy.getTelefono()) &&
                condition.apply(criteria.getProductosId(), copy.getProductosId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
