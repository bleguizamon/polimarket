package co.edu.poligran.polimarket.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.edu.poligran.polimarket.domain.Cliente} entity. This class is used
 * in {@link co.edu.poligran.polimarket.web.rest.ClienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter contacto;

    private LongFilter entregasId;

    private LongFilter vendedorId;

    private Boolean distinct;

    public ClienteCriteria() {}

    public ClienteCriteria(ClienteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.contacto = other.optionalContacto().map(StringFilter::copy).orElse(null);
        this.entregasId = other.optionalEntregasId().map(LongFilter::copy).orElse(null);
        this.vendedorId = other.optionalVendedorId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ClienteCriteria copy() {
        return new ClienteCriteria(this);
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

    public StringFilter getContacto() {
        return contacto;
    }

    public Optional<StringFilter> optionalContacto() {
        return Optional.ofNullable(contacto);
    }

    public StringFilter contacto() {
        if (contacto == null) {
            setContacto(new StringFilter());
        }
        return contacto;
    }

    public void setContacto(StringFilter contacto) {
        this.contacto = contacto;
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
        final ClienteCriteria that = (ClienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(contacto, that.contacto) &&
            Objects.equals(entregasId, that.entregasId) &&
            Objects.equals(vendedorId, that.vendedorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, contacto, entregasId, vendedorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalContacto().map(f -> "contacto=" + f + ", ").orElse("") +
            optionalEntregasId().map(f -> "entregasId=" + f + ", ").orElse("") +
            optionalVendedorId().map(f -> "vendedorId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
