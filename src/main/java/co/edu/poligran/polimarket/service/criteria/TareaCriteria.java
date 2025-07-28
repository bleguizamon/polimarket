package co.edu.poligran.polimarket.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.edu.poligran.polimarket.domain.Tarea} entity. This class is used
 * in {@link co.edu.poligran.polimarket.web.rest.TareaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tareas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TareaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter descripcion;

    private StringFilter estado;

    private LongFilter productosId;

    private LongFilter entregasId;

    private LongFilter vendedorId;

    private Boolean distinct;

    public TareaCriteria() {}

    public TareaCriteria(TareaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
        this.estado = other.optionalEstado().map(StringFilter::copy).orElse(null);
        this.productosId = other.optionalProductosId().map(LongFilter::copy).orElse(null);
        this.entregasId = other.optionalEntregasId().map(LongFilter::copy).orElse(null);
        this.vendedorId = other.optionalVendedorId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TareaCriteria copy() {
        return new TareaCriteria(this);
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

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public Optional<StringFilter> optionalDescripcion() {
        return Optional.ofNullable(descripcion);
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            setDescripcion(new StringFilter());
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public StringFilter getEstado() {
        return estado;
    }

    public Optional<StringFilter> optionalEstado() {
        return Optional.ofNullable(estado);
    }

    public StringFilter estado() {
        if (estado == null) {
            setEstado(new StringFilter());
        }
        return estado;
    }

    public void setEstado(StringFilter estado) {
        this.estado = estado;
    }

    public LongFilter getProductosId() {
        return productosId;
    }

    public Optional<LongFilter> optionalProductosId() {
        return Optional.ofNullable(productosId);
    }

    public LongFilter productosId() {
        if (productosId == null) {
            setProductosId(new LongFilter());
        }
        return productosId;
    }

    public void setProductosId(LongFilter productosId) {
        this.productosId = productosId;
    }

    public LongFilter getEntregasId() {
        return entregasId;
    }

    public Optional<LongFilter> optionalEntregasId() {
        return Optional.ofNullable(entregasId);
    }

    public LongFilter entregasId() {
        if (entregasId == null) {
            setEntregasId(new LongFilter());
        }
        return entregasId;
    }

    public void setEntregasId(LongFilter entregasId) {
        this.entregasId = entregasId;
    }

    public LongFilter getVendedorId() {
        return vendedorId;
    }

    public Optional<LongFilter> optionalVendedorId() {
        return Optional.ofNullable(vendedorId);
    }

    public LongFilter vendedorId() {
        if (vendedorId == null) {
            setVendedorId(new LongFilter());
        }
        return vendedorId;
    }

    public void setVendedorId(LongFilter vendedorId) {
        this.vendedorId = vendedorId;
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
        final TareaCriteria that = (TareaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(productosId, that.productosId) &&
            Objects.equals(entregasId, that.entregasId) &&
            Objects.equals(vendedorId, that.vendedorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descripcion, estado, productosId, entregasId, vendedorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TareaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalEstado().map(f -> "estado=" + f + ", ").orElse("") +
            optionalProductosId().map(f -> "productosId=" + f + ", ").orElse("") +
            optionalEntregasId().map(f -> "entregasId=" + f + ", ").orElse("") +
            optionalVendedorId().map(f -> "vendedorId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
