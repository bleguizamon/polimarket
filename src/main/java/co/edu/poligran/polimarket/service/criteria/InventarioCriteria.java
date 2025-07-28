package co.edu.poligran.polimarket.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.edu.poligran.polimarket.domain.Inventario} entity. This class is used
 * in {@link co.edu.poligran.polimarket.web.rest.InventarioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inventarios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventarioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter cantidad;

    private StringFilter ubicacion;

    private LongFilter movimientosId;

    private LongFilter productoId;

    private Boolean distinct;

    public InventarioCriteria() {}

    public InventarioCriteria(InventarioCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.cantidad = other.optionalCantidad().map(IntegerFilter::copy).orElse(null);
        this.ubicacion = other.optionalUbicacion().map(StringFilter::copy).orElse(null);
        this.movimientosId = other.optionalMovimientosId().map(LongFilter::copy).orElse(null);
        this.productoId = other.optionalProductoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InventarioCriteria copy() {
        return new InventarioCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getCantidad() {
        return cantidad;
    }

    public Optional<IntegerFilter> optionalCantidad() {
        return Optional.ofNullable(cantidad);
    }

    public IntegerFilter cantidad() {
        if (cantidad == null) {
            setCantidad(new IntegerFilter());
        }
        return cantidad;
    }

    public void setCantidad(IntegerFilter cantidad) {
        this.cantidad = cantidad;
    }

    public StringFilter getUbicacion() {
        return ubicacion;
    }

    public Optional<StringFilter> optionalUbicacion() {
        return Optional.ofNullable(ubicacion);
    }

    public StringFilter ubicacion() {
        if (ubicacion == null) {
            setUbicacion(new StringFilter());
        }
        return ubicacion;
    }

    public void setUbicacion(StringFilter ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LongFilter getMovimientosId() {
        return movimientosId;
    }

    public Optional<LongFilter> optionalMovimientosId() {
        return Optional.ofNullable(movimientosId);
    }

    public LongFilter movimientosId() {
        if (movimientosId == null) {
            setMovimientosId(new LongFilter());
        }
        return movimientosId;
    }

    public void setMovimientosId(LongFilter movimientosId) {
        this.movimientosId = movimientosId;
    }

    public LongFilter getProductoId() {
        return productoId;
    }

    public Optional<LongFilter> optionalProductoId() {
        return Optional.ofNullable(productoId);
    }

    public LongFilter productoId() {
        if (productoId == null) {
            setProductoId(new LongFilter());
        }
        return productoId;
    }

    public void setProductoId(LongFilter productoId) {
        this.productoId = productoId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InventarioCriteria that = (InventarioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(ubicacion, that.ubicacion) &&
            Objects.equals(movimientosId, that.movimientosId) &&
            Objects.equals(productoId, that.productoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cantidad, ubicacion, movimientosId, productoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventarioCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCantidad().map(f -> "cantidad=" + f + ", ").orElse("") +
            optionalUbicacion().map(f -> "ubicacion=" + f + ", ").orElse("") +
            optionalMovimientosId().map(f -> "movimientosId=" + f + ", ").orElse("") +
            optionalProductoId().map(f -> "productoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
