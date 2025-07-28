package co.edu.poligran.polimarket.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.edu.poligran.polimarket.domain.Vendedor} entity. This class is used
 * in {@link co.edu.poligran.polimarket.web.rest.VendedorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vendedors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VendedorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private BooleanFilter autorizado;

    private LongFilter clientesId;

    private LongFilter tareasId;

    private LongFilter areaDeNegocioId;

    private Boolean distinct;

    public VendedorCriteria() {}

    public VendedorCriteria(VendedorCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.autorizado = other.optionalAutorizado().map(BooleanFilter::copy).orElse(null);
        this.clientesId = other.optionalClientesId().map(LongFilter::copy).orElse(null);
        this.tareasId = other.optionalTareasId().map(LongFilter::copy).orElse(null);
        this.areaDeNegocioId = other.optionalAreaDeNegocioId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public VendedorCriteria copy() {
        return new VendedorCriteria(this);
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

    public BooleanFilter getAutorizado() {
        return autorizado;
    }

    public Optional<BooleanFilter> optionalAutorizado() {
        return Optional.ofNullable(autorizado);
    }

    public BooleanFilter autorizado() {
        if (autorizado == null) {
            setAutorizado(new BooleanFilter());
        }
        return autorizado;
    }

    public void setAutorizado(BooleanFilter autorizado) {
        this.autorizado = autorizado;
    }

    public LongFilter getClientesId() {
        return clientesId;
    }

    public Optional<LongFilter> optionalClientesId() {
        return Optional.ofNullable(clientesId);
    }

    public LongFilter clientesId() {
        if (clientesId == null) {
            setClientesId(new LongFilter());
        }
        return clientesId;
    }

    public void setClientesId(LongFilter clientesId) {
        this.clientesId = clientesId;
    }

    public LongFilter getTareasId() {
        return tareasId;
    }

    public Optional<LongFilter> optionalTareasId() {
        return Optional.ofNullable(tareasId);
    }

    public LongFilter tareasId() {
        if (tareasId == null) {
            setTareasId(new LongFilter());
        }
        return tareasId;
    }

    public void setTareasId(LongFilter tareasId) {
        this.tareasId = tareasId;
    }

    public LongFilter getAreaDeNegocioId() {
        return areaDeNegocioId;
    }

    public Optional<LongFilter> optionalAreaDeNegocioId() {
        return Optional.ofNullable(areaDeNegocioId);
    }

    public LongFilter areaDeNegocioId() {
        if (areaDeNegocioId == null) {
            setAreaDeNegocioId(new LongFilter());
        }
        return areaDeNegocioId;
    }

    public void setAreaDeNegocioId(LongFilter areaDeNegocioId) {
        this.areaDeNegocioId = areaDeNegocioId;
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
        final VendedorCriteria that = (VendedorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(autorizado, that.autorizado) &&
            Objects.equals(clientesId, that.clientesId) &&
            Objects.equals(tareasId, that.tareasId) &&
            Objects.equals(areaDeNegocioId, that.areaDeNegocioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, autorizado, clientesId, tareasId, areaDeNegocioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VendedorCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalAutorizado().map(f -> "autorizado=" + f + ", ").orElse("") +
            optionalClientesId().map(f -> "clientesId=" + f + ", ").orElse("") +
            optionalTareasId().map(f -> "tareasId=" + f + ", ").orElse("") +
            optionalAreaDeNegocioId().map(f -> "areaDeNegocioId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
