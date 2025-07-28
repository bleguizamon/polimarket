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
 * A Vendedor.
 */
@Entity
@Table(name = "vendedor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vendedor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "autorizado")
    private Boolean autorizado;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vendedor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "entregases", "vendedor" }, allowSetters = true)
    private Set<Cliente> clientes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vendedor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "productos", "entregases", "vendedor" }, allowSetters = true)
    private Set<Tarea> tareas = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "vendedores" }, allowSetters = true)
    private AreaDeNegocio areaDeNegocio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vendedor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Vendedor nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getAutorizado() {
        return this.autorizado;
    }

    public Vendedor autorizado(Boolean autorizado) {
        this.setAutorizado(autorizado);
        return this;
    }

    public void setAutorizado(Boolean autorizado) {
        this.autorizado = autorizado;
    }

    public Set<Cliente> getClientes() {
        return this.clientes;
    }

    public void setClientes(Set<Cliente> clientes) {
        if (this.clientes != null) {
            this.clientes.forEach(i -> i.setVendedor(null));
        }
        if (clientes != null) {
            clientes.forEach(i -> i.setVendedor(this));
        }
        this.clientes = clientes;
    }

    public Vendedor clientes(Set<Cliente> clientes) {
        this.setClientes(clientes);
        return this;
    }

    public Vendedor addClientes(Cliente cliente) {
        this.clientes.add(cliente);
        cliente.setVendedor(this);
        return this;
    }

    public Vendedor removeClientes(Cliente cliente) {
        this.clientes.remove(cliente);
        cliente.setVendedor(null);
        return this;
    }

    public Set<Tarea> getTareas() {
        return this.tareas;
    }

    public void setTareas(Set<Tarea> tareas) {
        if (this.tareas != null) {
            this.tareas.forEach(i -> i.setVendedor(null));
        }
        if (tareas != null) {
            tareas.forEach(i -> i.setVendedor(this));
        }
        this.tareas = tareas;
    }

    public Vendedor tareas(Set<Tarea> tareas) {
        this.setTareas(tareas);
        return this;
    }

    public Vendedor addTareas(Tarea tarea) {
        this.tareas.add(tarea);
        tarea.setVendedor(this);
        return this;
    }

    public Vendedor removeTareas(Tarea tarea) {
        this.tareas.remove(tarea);
        tarea.setVendedor(null);
        return this;
    }

    public AreaDeNegocio getAreaDeNegocio() {
        return this.areaDeNegocio;
    }

    public void setAreaDeNegocio(AreaDeNegocio areaDeNegocio) {
        this.areaDeNegocio = areaDeNegocio;
    }

    public Vendedor areaDeNegocio(AreaDeNegocio areaDeNegocio) {
        this.setAreaDeNegocio(areaDeNegocio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vendedor)) {
            return false;
        }
        return getId() != null && getId().equals(((Vendedor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vendedor{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", autorizado='" + getAutorizado() + "'" +
            "}";
    }
}
