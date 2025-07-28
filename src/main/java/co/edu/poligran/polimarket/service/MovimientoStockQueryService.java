package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.*; // for static metamodels
import co.edu.poligran.polimarket.domain.MovimientoStock;
import co.edu.poligran.polimarket.repository.MovimientoStockRepository;
import co.edu.poligran.polimarket.service.criteria.MovimientoStockCriteria;
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
 * Service for executing complex queries for {@link MovimientoStock} entities in the database.
 * The main input is a {@link MovimientoStockCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MovimientoStock} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MovimientoStockQueryService extends QueryService<MovimientoStock> {

    private static final Logger LOG = LoggerFactory.getLogger(MovimientoStockQueryService.class);

    private final MovimientoStockRepository movimientoStockRepository;

    public MovimientoStockQueryService(MovimientoStockRepository movimientoStockRepository) {
        this.movimientoStockRepository = movimientoStockRepository;
    }

    /**
     * Return a {@link Page} of {@link MovimientoStock} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MovimientoStock> findByCriteria(MovimientoStockCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MovimientoStock> specification = createSpecification(criteria);
        return movimientoStockRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MovimientoStockCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MovimientoStock> specification = createSpecification(criteria);
        return movimientoStockRepository.count(specification);
    }

    /**
     * Function to convert {@link MovimientoStockCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MovimientoStock> createSpecification(MovimientoStockCriteria criteria) {
        Specification<MovimientoStock> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MovimientoStock_.id),
                buildRangeSpecification(criteria.getFecha(), MovimientoStock_.fecha),
                buildStringSpecification(criteria.getTipo(), MovimientoStock_.tipo),
                buildRangeSpecification(criteria.getCantidad(), MovimientoStock_.cantidad),
                buildStringSpecification(criteria.getReferencia(), MovimientoStock_.referencia),
                buildSpecification(criteria.getProductoId(), root -> root.join(MovimientoStock_.producto, JoinType.LEFT).get(Producto_.id)),
                buildSpecification(criteria.getInventarioId(), root ->
                    root.join(MovimientoStock_.inventario, JoinType.LEFT).get(Inventario_.id)
                )
            );
        }
        return specification;
    }
}
