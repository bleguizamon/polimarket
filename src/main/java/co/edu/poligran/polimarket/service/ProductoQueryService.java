package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.*; // for static metamodels
import co.edu.poligran.polimarket.domain.Producto;
import co.edu.poligran.polimarket.repository.ProductoRepository;
import co.edu.poligran.polimarket.service.criteria.ProductoCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Producto} entities in the database.
 * The main input is a {@link ProductoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Producto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductoQueryService extends QueryService<Producto> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductoQueryService.class);

    private final ProductoRepository productoRepository;

    public ProductoQueryService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Return a {@link Page} of {@link Producto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Producto> findByCriteria(ProductoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Producto> specification = createSpecification(criteria);
        return productoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Producto> specification = createSpecification(criteria);
        return productoRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Producto> createSpecification(ProductoCriteria criteria) {
        Specification<Producto> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Producto_.id),
                buildStringSpecification(criteria.getNombre(), Producto_.nombre),
                buildRangeSpecification(criteria.getCantidadDisponible(), Producto_.cantidadDisponible),
                buildSpecification(criteria.getEntregasId(), root -> root.join(Producto_.entregases, JoinType.LEFT).get(Entrega_.id)),
                buildSpecification(criteria.getInventariosId(), root -> root.join(Producto_.inventarios, JoinType.LEFT).get(Inventario_.id)
                ),
                buildSpecification(criteria.getMovimientosId(), root ->
                    root.join(Producto_.movimientos, JoinType.LEFT).get(MovimientoStock_.id)
                ),
                buildSpecification(criteria.getProveedorId(), root -> root.join(Producto_.proveedor, JoinType.LEFT).get(Proveedor_.id)),
                buildSpecification(criteria.getTareaId(), root -> root.join(Producto_.tarea, JoinType.LEFT).get(Tarea_.id))
            );
        }
        return specification;
    }
}
