package co.edu.poligran.polimarket.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tarea.
 */
@Entity
@Table(name = "tarea")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tarea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estado")
    private String estado;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tarea")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "entregases", "inventarios", "movimientos", "proveedor", "tarea" }, allowSetters = true)
    private Set<Producto> productos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tarea")
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

    public Tarea id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Tarea descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return this.estado;
    }

    public Tarea estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Set<Producto> getProductos() {
        return this.productos;
    }

    public void setProductos(Set<Producto> productos) {
        if (this.productos != null) {
            this.productos.forEach(i -> i.setTarea(null));
        }
        if (productos != null) {
            productos.forEach(i -> i.setTarea(this));
        }
        this.productos = productos;
    }

    public Tarea productos(Set<Producto> productos) {
        this.setProductos(productos);
        return this;
    }

    public Tarea addProductos(Producto producto) {
        this.productos.add(producto);
        producto.setTarea(this);
        return this;
    }

    public Tarea removeProductos(Producto producto) {
        this.productos.remove(producto);
        producto.setTarea(null);
        return this;
    }

    public Set<Entrega> getEntregases() {
        return this.entregases;
    }

    public void setEntregases(Set<Entrega> entregas) {
        if (this.entregases != null) {
            this.entregases.forEach(i -> i.setTarea(null));
        }
        if (entregas != null) {
            entregas.forEach(i -> i.setTarea(this));
        }
        this.entregases = entregas;
    }

    public Tarea entregases(Set<Entrega> entregas) {
        this.setEntregases(entregas);
        return this;
    }

    public Tarea addEntregas(Entrega entrega) {
        this.entregases.add(entrega);
        entrega.setTarea(this);
        return this;
    }

    public Tarea removeEntregas(Entrega entrega) {
        this.entregases.remove(entrega);
        entrega.setTarea(null);
        return this;
    }

    public Vendedor getVendedor() {
        return this.vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Tarea vendedor(Vendedor vendedor) {
        this.setVendedor(vendedor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tarea)) {
            return false;
        }
        return getId() != null && getId().equals(((Tarea) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tarea{" +
            "id=" + getId() +
            ", descripcion='" + getDescripcion() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
