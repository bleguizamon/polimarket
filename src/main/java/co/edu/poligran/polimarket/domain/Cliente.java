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
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "contacto")
    private String contacto;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tarea", "cliente", "producto" }, allowSetters = true)
    private Set<Entrega> entregases = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "clientes", "tareas", "areaDeNegocio" }, allowSetters = true)
    private Vendedor vendedor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cliente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Cliente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContacto() {
        return this.contacto;
    }

    public Cliente contacto(String contacto) {
        this.setContacto(contacto);
        return this;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public Set<Entrega> getEntregases() {
        return this.entregases;
    }

    public void setEntregases(Set<Entrega> entregas) {
        if (this.entregases != null) {
            this.entregases.forEach(i -> i.setCliente(null));
        }
        if (entregas != null) {
            entregas.forEach(i -> i.setCliente(this));
        }
        this.entregases = entregas;
    }

    public Cliente entregases(Set<Entrega> entregas) {
        this.setEntregases(entregas);
        return this;
    }

    public Cliente addEntregas(Entrega entrega) {
        this.entregases.add(entrega);
        entrega.setCliente(this);
        return this;
    }

    public Cliente removeEntregas(Entrega entrega) {
        this.entregases.remove(entrega);
        entrega.setCliente(null);
        return this;
    }

    public Vendedor getVendedor() {
        return this.vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Cliente vendedor(Vendedor vendedor) {
        this.setVendedor(vendedor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return getId() != null && getId().equals(((Cliente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", contacto='" + getContacto() + "'" +
            "}";
    }
}
