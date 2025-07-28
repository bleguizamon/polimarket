package co.edu.poligran.polimarket.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.edu.poligran.polimarket.domain.MovimientoStock} entity. This class is used
 * in {@link co.edu.poligran.polimarket.web.rest.MovimientoStockResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /movimiento-stocks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MovimientoStockCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter fecha;

    private StringFilter tipo;

    private IntegerFilter cantidad;

    private StringFilter referencia;

    private LongFilter productoId;

    private LongFilter inventarioId;

    private Boolean distinct;

    public MovimientoStockCriteria() {}

    public MovimientoStockCriteria(MovimientoStockCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fecha = other.optionalFecha().map(InstantFilter::copy).orElse(null);
        this.tipo = other.optionalTipo().map(StringFilter::copy).orElse(null);
        this.cantidad = other.optionalCantidad().map(IntegerFilter::copy).orElse(null);
        this.referencia = other.optionalReferencia().map(StringFilter::copy).orElse(null);
        this.productoId = other.optionalProductoId().map(LongFilter::copy).orElse(null);
        this.inventarioId = other.optionalInventarioId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MovimientoStockCriteria copy() {
        return new MovimientoStockCriteria(this);
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

    public InstantFilter getFecha() {
        return fecha;
    }

    public Optional<InstantFilter> optionalFecha() {
        return Optional.ofNullable(fecha);
    }

    public InstantFilter fecha() {
        if (fecha == null) {
            setFecha(new InstantFilter());
        }
        return fecha;
    }

    public void setFecha(InstantFilter fecha) {
        this.fecha = fecha;
    }

    public StringFilter getTipo() {
        return tipo;
    }

    public Optional<StringFilter> optionalTipo() {
        return Optional.ofNullable(tipo);
    }

    public StringFilter tipo() {
        if (tipo == null) {
            setTipo(new StringFilter());
        }
        return tipo;
    }

    public void setTipo(StringFilter tipo) {
        this.tipo = tipo;
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

    public StringFilter getReferencia() {
        return referencia;
    }

    public Optional<StringFilter> optionalReferencia() {
        return Optional.ofNullable(referencia);
    }

    public StringFilter referencia() {
        if (referencia == null) {
            setReferencia(new StringFilter());
        }
        return referencia;
    }

    public void setReferencia(StringFilter referencia) {
        this.referencia = referencia;
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

    public LongFilter getInventarioId() {
        return inventarioId;
    }

    public Optional<LongFilter> optionalInventarioId() {
        return Optional.ofNullable(inventarioId);
    }

    public LongFilter inventarioId() {
        if (inventarioId == null) {
            setInventarioId(new LongFilter());
        }
        return inventarioId;
    }

    public void setInventarioId(LongFilter inventarioId) {
        this.inventarioId = inventarioId;
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
        final MovimientoStockCriteria that = (MovimientoStockCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(referencia, that.referencia) &&
            Objects.equals(productoId, that.productoId) &&
            Objects.equals(inventarioId, that.inventarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fecha, tipo, cantidad, referencia, productoId, inventarioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovimientoStockCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFecha().map(f -> "fecha=" + f + ", ").orElse("") +
            optionalTipo().map(f -> "tipo=" + f + ", ").orElse("") +
            optionalCantidad().map(f -> "cantidad=" + f + ", ").orElse("") +
            optionalReferencia().map(f -> "referencia=" + f + ", ").orElse("") +
            optionalProductoId().map(f -> "productoId=" + f + ", ").orElse("") +
            optionalInventarioId().map(f -> "inventarioId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
