package co.edu.poligran.polimarket.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Inventario.
 */
@Entity
@Table(name = "inventario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Inventario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "ubicacion")
    private String ubicacion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inventario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "producto", "inventario" }, allowSetters = true)
    private Set<MovimientoStock> movimientos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "entregases", "inventarios", "movimientos", "proveedor", "tarea" }, allowSetters = true)
    private Producto producto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inventario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Inventario cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getUbicacion() {
        return this.ubicacion;
    }

    public Inventario ubicacion(String ubicacion) {
        this.setUbicacion(ubicacion);
        return this;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Set<MovimientoStock> getMovimientos() {
        return this.movimientos;
    }

    public void setMovimientos(Set<MovimientoStock> movimientoStocks) {
        if (this.movimientos != null) {
            this.movimientos.forEach(i -> i.setInventario(null));
        }
        if (movimientoStocks != null) {
            movimientoStocks.forEach(i -> i.setInventario(this));
        }
        this.movimientos = movimientoStocks;
    }

    public Inventario movimientos(Set<MovimientoStock> movimientoStocks) {
        this.setMovimientos(movimientoStocks);
        return this;
    }

    public Inventario addMovimientos(MovimientoStock movimientoStock) {
        this.movimientos.add(movimientoStock);
        movimientoStock.setInventario(this);
        return this;
    }

    public Inventario removeMovimientos(MovimientoStock movimientoStock) {
        this.movimientos.remove(movimientoStock);
        movimientoStock.setInventario(null);
        return this;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Inventario producto(Producto producto) {
        this.setProducto(producto);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inventario)) {
            return false;
        }
        return getId() != null && getId().equals(((Inventario) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inventario{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", ubicacion='" + getUbicacion() + "'" +
            "}";
    }
}
