package co.edu.poligran.polimarket.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductoCriteriaTest {

    @Test
    void newProductoCriteriaHasAllFiltersNullTest() {
        var productoCriteria = new ProductoCriteria();
        assertThat(productoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productoCriteriaFluentMethodsCreatesFiltersTest() {
        var productoCriteria = new ProductoCriteria();

        setAllFilters(productoCriteria);

        assertThat(productoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productoCriteriaCopyCreatesNullFilterTest() {
        var productoCriteria = new ProductoCriteria();
        var copy = productoCriteria.copy();

        assertThat(productoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productoCriteria)
        );
    }

    @Test
    void productoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productoCriteria = new ProductoCriteria();
        setAllFilters(productoCriteria);

        var copy = productoCriteria.copy();

        assertThat(productoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productoCriteria = new ProductoCriteria();

        assertThat(productoCriteria).hasToString("ProductoCriteria{}");
    }

    private static void setAllFilters(ProductoCriteria productoCriteria) {
        productoCriteria.id();
        productoCriteria.nombre();
        productoCriteria.cantidadDisponible();
        productoCriteria.entregasId();
        productoCriteria.inventariosId();
        productoCriteria.movimientosId();
        productoCriteria.proveedorId();
        productoCriteria.tareaId();
        productoCriteria.distinct();
    }

    private static Condition<ProductoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getCantidadDisponible()) &&
                condition.apply(criteria.getEntregasId()) &&
                condition.apply(criteria.getInventariosId()) &&
                condition.apply(criteria.getMovimientosId()) &&
                condition.apply(criteria.getProveedorId()) &&
                condition.apply(criteria.getTareaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductoCriteria> copyFiltersAre(ProductoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getCantidadDisponible(), copy.getCantidadDisponible()) &&
                condition.apply(criteria.getEntregasId(), copy.getEntregasId()) &&
                condition.apply(criteria.getInventariosId(), copy.getInventariosId()) &&
                condition.apply(criteria.getMovimientosId(), copy.getMovimientosId()) &&
                condition.apply(criteria.getProveedorId(), copy.getProveedorId()) &&
                condition.apply(criteria.getTareaId(), copy.getTareaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
