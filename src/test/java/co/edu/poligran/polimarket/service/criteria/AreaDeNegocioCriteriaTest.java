package co.edu.poligran.polimarket.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AreaDeNegocioCriteriaTest {

    @Test
    void newAreaDeNegocioCriteriaHasAllFiltersNullTest() {
        var areaDeNegocioCriteria = new AreaDeNegocioCriteria();
        assertThat(areaDeNegocioCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void areaDeNegocioCriteriaFluentMethodsCreatesFiltersTest() {
        var areaDeNegocioCriteria = new AreaDeNegocioCriteria();

        setAllFilters(areaDeNegocioCriteria);

        assertThat(areaDeNegocioCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void areaDeNegocioCriteriaCopyCreatesNullFilterTest() {
        var areaDeNegocioCriteria = new AreaDeNegocioCriteria();
        var copy = areaDeNegocioCriteria.copy();

        assertThat(areaDeNegocioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(areaDeNegocioCriteria)
        );
    }

    @Test
    void areaDeNegocioCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var areaDeNegocioCriteria = new AreaDeNegocioCriteria();
        setAllFilters(areaDeNegocioCriteria);

        var copy = areaDeNegocioCriteria.copy();

        assertThat(areaDeNegocioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(areaDeNegocioCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var areaDeNegocioCriteria = new AreaDeNegocioCriteria();

        assertThat(areaDeNegocioCriteria).hasToString("AreaDeNegocioCriteria{}");
    }

    private static void setAllFilters(AreaDeNegocioCriteria areaDeNegocioCriteria) {
        areaDeNegocioCriteria.id();
        areaDeNegocioCriteria.nombre();
        areaDeNegocioCriteria.descripcion();
        areaDeNegocioCriteria.vendedoresId();
        areaDeNegocioCriteria.distinct();
    }

    private static Condition<AreaDeNegocioCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getVendedoresId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AreaDeNegocioCriteria> copyFiltersAre(
        AreaDeNegocioCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getVendedoresId(), copy.getVendedoresId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
