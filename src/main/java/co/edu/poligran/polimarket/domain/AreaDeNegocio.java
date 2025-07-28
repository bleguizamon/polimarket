package co.edu.poligran.polimarket.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AreaDeNegocio.
 */
@Entity
@Table(name = "area_de_negocio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AreaDeNegocio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "areaDeNegocio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "clientes", "tareas", "areaDeNegocio" }, allowSetters = true)
    private Set<Vendedor> vendedores = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AreaDeNegocio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public AreaDeNegocio nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public AreaDeNegocio descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Vendedor> getVendedores() {
        return this.vendedores;
    }

    public void setVendedores(Set<Vendedor> vendedors) {
        if (this.vendedores != null) {
            this.vendedores.forEach(i -> i.setAreaDeNegocio(null));
        }
        if (vendedors != null) {
            vendedors.forEach(i -> i.setAreaDeNegocio(this));
        }
        this.vendedores = vendedors;
    }

    public AreaDeNegocio vendedores(Set<Vendedor> vendedors) {
        this.setVendedores(vendedors);
        return this;
    }

    public AreaDeNegocio addVendedores(Vendedor vendedor) {
        this.vendedores.add(vendedor);
        vendedor.setAreaDeNegocio(this);
        return this;
    }

    public AreaDeNegocio removeVendedores(Vendedor vendedor) {
        this.vendedores.remove(vendedor);
        vendedor.setAreaDeNegocio(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AreaDeNegocio)) {
            return false;
        }
        return getId() != null && getId().equals(((AreaDeNegocio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AreaDeNegocio{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
