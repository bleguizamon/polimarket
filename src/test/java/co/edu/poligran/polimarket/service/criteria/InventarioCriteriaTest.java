package co.edu.poligran.polimarket.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InventarioCriteriaTest {

    @Test
    void newInventarioCriteriaHasAllFiltersNullTest() {
        var inventarioCriteria = new InventarioCriteria();
        assertThat(inventarioCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void inventarioCriteriaFluentMethodsCreatesFiltersTest() {
        var inventarioCriteria = new InventarioCriteria();

        setAllFilters(inventarioCriteria);

        assertThat(inventarioCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void inventarioCriteriaCopyCreatesNullFilterTest() {
        var inventarioCriteria = new InventarioCriteria();
        var copy = inventarioCriteria.copy();

        assertThat(inventarioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(inventarioCriteria)
        );
    }

    @Test
    void inventarioCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var inventarioCriteria = new InventarioCriteria();
        setAllFilters(inventarioCriteria);

        var copy = inventarioCriteria.copy();

        assertThat(inventarioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(inventarioCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var inventarioCriteria = new InventarioCriteria();

        assertThat(inventarioCriteria).hasToString("InventarioCriteria{}");
    }

    private static void setAllFilters(InventarioCriteria inventarioCriteria) {
        inventarioCriteria.id();
        inventarioCriteria.cantidad();
        inventarioCriteria.ubicacion();
        inventarioCriteria.movimientosId();
        inventarioCriteria.productoId();
        inventarioCriteria.distinct();
    }

    private static Condition<InventarioCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCantidad()) &&
                condition.apply(criteria.getUbicacion()) &&
                condition.apply(criteria.getMovimientosId()) &&
                condition.apply(criteria.getProductoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InventarioCriteria> copyFiltersAre(InventarioCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCantidad(), copy.getCantidad()) &&
                condition.apply(criteria.getUbicacion(), copy.getUbicacion()) &&
                condition.apply(criteria.getMovimientosId(), copy.getMovimientosId()) &&
                condition.apply(criteria.getProductoId(), copy.getProductoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
