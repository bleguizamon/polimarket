package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.*; // for static metamodels
import co.edu.poligran.polimarket.domain.Proveedor;
import co.edu.poligran.polimarket.repository.ProveedorRepository;
import co.edu.poligran.polimarket.service.criteria.ProveedorCriteria;
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
 * Service for executing complex queries for {@link Proveedor} entities in the database.
 * The main input is a {@link ProveedorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Proveedor} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProveedorQueryService extends QueryService<Proveedor> {

    private static final Logger LOG = LoggerFactory.getLogger(ProveedorQueryService.class);

    private final ProveedorRepository proveedorRepository;

    public ProveedorQueryService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    /**
     * Return a {@link Page} of {@link Proveedor} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Proveedor> findByCriteria(ProveedorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Proveedor> specification = createSpecification(criteria);
        return proveedorRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProveedorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Proveedor> specification = createSpecification(criteria);
        return proveedorRepository.count(specification);
    }

    /**
     * Function to convert {@link ProveedorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Proveedor> createSpecification(ProveedorCriteria criteria) {
        Specification<Proveedor> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Proveedor_.id),
                buildStringSpecification(criteria.getNombre(), Proveedor_.nombre),
                buildStringSpecification(criteria.getTelefono(), Proveedor_.telefono),
                buildSpecification(criteria.getProductosId(), root -> root.join(Proveedor_.productos, JoinType.LEFT).get(Producto_.id))
            );
        }
        return specification;
    }
}
