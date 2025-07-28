package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.*; // for static metamodels
import co.edu.poligran.polimarket.domain.Tarea;
import co.edu.poligran.polimarket.repository.TareaRepository;
import co.edu.poligran.polimarket.service.criteria.TareaCriteria;
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
 * Service for executing complex queries for {@link Tarea} entities in the database.
 * The main input is a {@link TareaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Tarea} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TareaQueryService extends QueryService<Tarea> {

    private static final Logger LOG = LoggerFactory.getLogger(TareaQueryService.class);

    private final TareaRepository tareaRepository;

    public TareaQueryService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    /**
     * Return a {@link Page} of {@link Tarea} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Tarea> findByCriteria(TareaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tarea> specification = createSpecification(criteria);
        return tareaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TareaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Tarea> specification = createSpecification(criteria);
        return tareaRepository.count(specification);
    }

    /**
     * Function to convert {@link TareaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tarea> createSpecification(TareaCriteria criteria) {
        Specification<Tarea> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Tarea_.id),
                buildStringSpecification(criteria.getDescripcion(), Tarea_.descripcion),
                buildStringSpecification(criteria.getEstado(), Tarea_.estado),
                buildSpecification(criteria.getProductosId(), root -> root.join(Tarea_.productos, JoinType.LEFT).get(Producto_.id)),
                buildSpecification(criteria.getEntregasId(), root -> root.join(Tarea_.entregases, JoinType.LEFT).get(Entrega_.id)),
                buildSpecification(criteria.getVendedorId(), root -> root.join(Tarea_.vendedor, JoinType.LEFT).get(Vendedor_.id))
            );
        }
        return specification;
    }
}
