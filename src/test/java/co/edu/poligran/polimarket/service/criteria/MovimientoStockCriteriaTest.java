package co.edu.poligran.polimarket.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MovimientoStockCriteriaTest {

    @Test
    void newMovimientoStockCriteriaHasAllFiltersNullTest() {
        var movimientoStockCriteria = new MovimientoStockCriteria();
        assertThat(movimientoStockCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void movimientoStockCriteriaFluentMethodsCreatesFiltersTest() {
        var movimientoStockCriteria = new MovimientoStockCriteria();

        setAllFilters(movimientoStockCriteria);

        assertThat(movimientoStockCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void movimientoStockCriteriaCopyCreatesNullFilterTest() {
        var movimientoStockCriteria = new MovimientoStockCriteria();
        var copy = movimientoStockCriteria.copy();

        assertThat(movimientoStockCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(movimientoStockCriteria)
        );
    }

    @Test
    void movimientoStockCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var movimientoStockCriteria = new MovimientoStockCriteria();
        setAllFilters(movimientoStockCriteria);

        var copy = movimientoStockCriteria.copy();

        assertThat(movimientoStockCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(movimientoStockCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var movimientoStockCriteria = new MovimientoStockCriteria();

        assertThat(movimientoStockCriteria).hasToString("MovimientoStockCriteria{}");
    }

    private static void setAllFilters(MovimientoStockCriteria movimientoStockCriteria) {
        movimientoStockCriteria.id();
        movimientoStockCriteria.fecha();
        movimientoStockCriteria.tipo();
        movimientoStockCriteria.cantidad();
        movimientoStockCriteria.referencia();
        movimientoStockCriteria.productoId();
        movimientoStockCriteria.inventarioId();
        movimientoStockCriteria.distinct();
    }

    private static Condition<MovimientoStockCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFecha()) &&
                condition.apply(criteria.getTipo()) &&
                condition.apply(criteria.getCantidad()) &&
                condition.apply(criteria.getReferencia()) &&
                condition.apply(criteria.getProductoId()) &&
                condition.apply(criteria.getInventarioId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MovimientoStockCriteria> copyFiltersAre(
        MovimientoStockCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFecha(), copy.getFecha()) &&
                condition.apply(criteria.getTipo(), copy.getTipo()) &&
                condition.apply(criteria.getCantidad(), copy.getCantidad()) &&
                condition.apply(criteria.getReferencia(), copy.getReferencia()) &&
                condition.apply(criteria.getProductoId(), copy.getProductoId()) &&
                condition.apply(criteria.getInventarioId(), copy.getInventarioId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
