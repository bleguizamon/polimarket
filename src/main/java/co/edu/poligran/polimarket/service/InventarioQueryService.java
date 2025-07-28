package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.*; // for static metamodels
import co.edu.poligran.polimarket.domain.Inventario;
import co.edu.poligran.polimarket.repository.InventarioRepository;
import co.edu.poligran.polimarket.service.criteria.InventarioCriteria;
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
 * Service for executing complex queries for {@link Inventario} entities in the database.
 * The main input is a {@link InventarioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Inventario} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InventarioQueryService extends QueryService<Inventario> {

    private static final Logger LOG = LoggerFactory.getLogger(InventarioQueryService.class);

    private final InventarioRepository inventarioRepository;

    public InventarioQueryService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    /**
     * Return a {@link Page} of {@link Inventario} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Inventario> findByCriteria(InventarioCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Inventario> specification = createSpecification(criteria);
        return inventarioRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InventarioCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Inventario> specification = createSpecification(criteria);
        return inventarioRepository.count(specification);
    }

    /**
     * Function to convert {@link InventarioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Inventario> createSpecification(InventarioCriteria criteria) {
        Specification<Inventario> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Inventario_.id),
                buildRangeSpecification(criteria.getCantidad(), Inventario_.cantidad),
                buildStringSpecification(criteria.getUbicacion(), Inventario_.ubicacion),
                buildSpecification(criteria.getMovimientosId(), root ->
                    root.join(Inventario_.movimientos, JoinType.LEFT).get(MovimientoStock_.id)
                ),
                buildSpecification(criteria.getProductoId(), root -> root.join(Inventario_.producto, JoinType.LEFT).get(Producto_.id))
            );
        }
        return specification;
    }
}
