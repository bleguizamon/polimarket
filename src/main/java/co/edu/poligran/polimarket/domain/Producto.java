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
 * A Producto.
 */
@Entity
@Table(name = "producto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "cantidad_disponible")
    private Integer cantidadDisponible;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "producto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tarea", "cliente", "producto" }, allowSetters = true)
    private Set<Entrega> entregases = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "producto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "movimientos", "producto" }, allowSetters = true)
    private Set<Inventario> inventarios = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "producto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "producto", "inventario" }, allowSetters = true)
    private Set<MovimientoStock> movimientos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productos" }, allowSetters = true)
    private Proveedor proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productos", "entregases", "vendedor" }, allowSetters = true)
    private Tarea tarea;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Producto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Producto nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidadDisponible() {
        return this.cantidadDisponible;
    }

    public Producto cantidadDisponible(Integer cantidadDisponible) {
        this.setCantidadDisponible(cantidadDisponible);
        return this;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public Set<Entrega> getEntregases() {
        return this.entregases;
    }

    public void setEntregases(Set<Entrega> entregas) {
        if (this.entregases != null) {
            this.entregases.forEach(i -> i.setProducto(null));
        }
        if (entregas != null) {
            entregas.forEach(i -> i.setProducto(this));
        }
        this.entregases = entregas;
    }

    public Producto entregases(Set<Entrega> entregas) {
        this.setEntregases(entregas);
        return this;
    }

    public Producto addEntregas(Entrega entrega) {
        this.entregases.add(entrega);
        entrega.setProducto(this);
        return this;
    }

    public Producto removeEntregas(Entrega entrega) {
        this.entregases.remove(entrega);
        entrega.setProducto(null);
        return this;
    }

    public Set<Inventario> getInventarios() {
        return this.inventarios;
    }

    public void setInventarios(Set<Inventario> inventarios) {
        if (this.inventarios != null) {
            this.inventarios.forEach(i -> i.setProducto(null));
        }
        if (inventarios != null) {
            inventarios.forEach(i -> i.setProducto(this));
        }
        this.inventarios = inventarios;
    }

    public Producto inventarios(Set<Inventario> inventarios) {
        this.setInventarios(inventarios);
        return this;
    }

    public Producto addInventarios(Inventario inventario) {
        this.inventarios.add(inventario);
        inventario.setProducto(this);
        return this;
    }

    public Producto removeInventarios(Inventario inventario) {
        this.inventarios.remove(inventario);
        inventario.setProducto(null);
        return this;
    }

    public Set<MovimientoStock> getMovimientos() {
        return this.movimientos;
    }

    public void setMovimientos(Set<MovimientoStock> movimientoStocks) {
        if (this.movimientos != null) {
            this.movimientos.forEach(i -> i.setProducto(null));
        }
        if (movimientoStocks != null) {
            movimientoStocks.forEach(i -> i.setProducto(this));
        }
        this.movimientos = movimientoStocks;
    }

    public Producto movimientos(Set<MovimientoStock> movimientoStocks) {
        this.setMovimientos(movimientoStocks);
        return this;
    }

    public Producto addMovimientos(MovimientoStock movimientoStock) {
        this.movimientos.add(movimientoStock);
        movimientoStock.setProducto(this);
        return this;
    }

    public Producto removeMovimientos(MovimientoStock movimientoStock) {
        this.movimientos.remove(movimientoStock);
        movimientoStock.setProducto(null);
        return this;
    }

    public Proveedor getProveedor() {
        return this.proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Producto proveedor(Proveedor proveedor) {
        this.setProveedor(proveedor);
        return this;
    }

    public Tarea getTarea() {
        return this.tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public Producto tarea(Tarea tarea) {
        this.setTarea(tarea);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producto)) {
            return false;
        }
        return getId() != null && getId().equals(((Producto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Producto{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", cantidadDisponible=" + getCantidadDisponible() +
            "}";
    }
}
