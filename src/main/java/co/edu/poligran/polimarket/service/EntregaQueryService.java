package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.*; // for static metamodels
import co.edu.poligran.polimarket.domain.Entrega;
import co.edu.poligran.polimarket.repository.EntregaRepository;
import co.edu.poligran.polimarket.service.criteria.EntregaCriteria;
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
 * Service for executing complex queries for {@link Entrega} entities in the database.
 * The main input is a {@link EntregaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Entrega} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EntregaQueryService extends QueryService<Entrega> {

    private static final Logger LOG = LoggerFactory.getLogger(EntregaQueryService.class);

    private final EntregaRepository entregaRepository;

    public EntregaQueryService(EntregaRepository entregaRepository) {
        this.entregaRepository = entregaRepository;
    }

    /**
     * Return a {@link Page} of {@link Entrega} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Entrega> findByCriteria(EntregaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Entrega> specification = createSpecification(criteria);
        return entregaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EntregaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Entrega> specification = createSpecification(criteria);
        return entregaRepository.count(specification);
    }

    /**
     * Function to convert {@link EntregaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Entrega> createSpecification(EntregaCriteria criteria) {
        Specification<Entrega> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Entrega_.id),
                buildRangeSpecification(criteria.getFecha(), Entrega_.fecha),
                buildStringSpecification(criteria.getEstado(), Entrega_.estado),
                buildSpecification(criteria.getTareaId(), root -> root.join(Entrega_.tarea, JoinType.LEFT).get(Tarea_.id)),
                buildSpecification(criteria.getClienteId(), root -> root.join(Entrega_.cliente, JoinType.LEFT).get(Cliente_.id)),
                buildSpecification(criteria.getProductoId(), root -> root.join(Entrega_.producto, JoinType.LEFT).get(Producto_.id))
            );
        }
        return specification;
    }
}
