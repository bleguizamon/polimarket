package co.edu.poligran.polimarket.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.edu.poligran.polimarket.domain.Producto} entity. This class is used
 * in {@link co.edu.poligran.polimarket.web.rest.ProductoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /productos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private IntegerFilter cantidadDisponible;

    private LongFilter entregasId;

    private LongFilter inventariosId;

    private LongFilter movimientosId;

    private LongFilter proveedorId;

    private LongFilter tareaId;

    private Boolean distinct;

    public ProductoCriteria() {}

    public ProductoCriteria(ProductoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.cantidadDisponible = other.optionalCantidadDisponible().map(IntegerFilter::copy).orElse(null);
        this.entregasId = other.optionalEntregasId().map(LongFilter::copy).orElse(null);
        this.inventariosId = other.optionalInventariosId().map(LongFilter::copy).orElse(null);
        this.movimientosId = other.optionalMovimientosId().map(LongFilter::copy).orElse(null);
        this.proveedorId = other.optionalProveedorId().map(LongFilter::copy).orElse(null);
        this.tareaId = other.optionalTareaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductoCriteria copy() {
        return new ProductoCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public Optional<StringFilter> optionalNombre() {
        return Optional.ofNullable(nombre);
    }

    public StringFilter nombre() {
        if (nombre == null) {
            setNombre(new StringFilter());
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public IntegerFilter getCantidadDisponible() {
        return cantidadDisponible;
    }

    public Optional<IntegerFilter> optionalCantidadDisponible() {
        return Optional.ofNullable(cantidadDisponible);
    }

    public IntegerFilter cantidadDisponible() {
        if (cantidadDisponible == null) {
            setCantidadDisponible(new IntegerFilter());
        }
        return cantidadDisponible;
    }

    public void setCantidadDisponible(IntegerFilter cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
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

    public LongFilter getInventariosId() {
        return inventariosId;
    }

    public Optional<LongFilter> optionalInventariosId() {
        return Optional.ofNullable(inventariosId);
    }

    public LongFilter inventariosId() {
        if (inventariosId == null) {
            setInventariosId(new LongFilter());
        }
        return inventariosId;
    }

    public void setInventariosId(LongFilter inventariosId) {
        this.inventariosId = inventariosId;
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

    public LongFilter getProveedorId() {
        return proveedorId;
    }

    public Optional<LongFilter> optionalProveedorId() {
        return Optional.ofNullable(proveedorId);
    }

    public LongFilter proveedorId() {
        if (proveedorId == null) {
            setProveedorId(new LongFilter());
        }
        return proveedorId;
    }

    public void setProveedorId(LongFilter proveedorId) {
        this.proveedorId = proveedorId;
    }

    public LongFilter getTareaId() {
        return tareaId;
    }

    public Optional<LongFilter> optionalTareaId() {
        return Optional.ofNullable(tareaId);
    }

    public LongFilter tareaId() {
        if (tareaId == null) {
            setTareaId(new LongFilter());
        }
        return tareaId;
    }

    public void setTareaId(LongFilter tareaId) {
        this.tareaId = tareaId;
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
        final ProductoCriteria that = (ProductoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(cantidadDisponible, that.cantidadDisponible) &&
            Objects.equals(entregasId, that.entregasId) &&
            Objects.equals(inventariosId, that.inventariosId) &&
            Objects.equals(movimientosId, that.movimientosId) &&
            Objects.equals(proveedorId, that.proveedorId) &&
            Objects.equals(tareaId, that.tareaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, cantidadDisponible, entregasId, inventariosId, movimientosId, proveedorId, tareaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalCantidadDisponible().map(f -> "cantidadDisponible=" + f + ", ").orElse("") +
            optionalEntregasId().map(f -> "entregasId=" + f + ", ").orElse("") +
            optionalInventariosId().map(f -> "inventariosId=" + f + ", ").orElse("") +
            optionalMovimientosId().map(f -> "movimientosId=" + f + ", ").orElse("") +
            optionalProveedorId().map(f -> "proveedorId=" + f + ", ").orElse("") +
            optionalTareaId().map(f -> "tareaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
