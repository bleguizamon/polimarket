package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.*; // for static metamodels
import co.edu.poligran.polimarket.domain.AreaDeNegocio;
import co.edu.poligran.polimarket.repository.AreaDeNegocioRepository;
import co.edu.poligran.polimarket.service.criteria.AreaDeNegocioCriteria;
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
 * Service for executing complex queries for {@link AreaDeNegocio} entities in the database.
 * The main input is a {@link AreaDeNegocioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AreaDeNegocio} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AreaDeNegocioQueryService extends QueryService<AreaDeNegocio> {

    private static final Logger LOG = LoggerFactory.getLogger(AreaDeNegocioQueryService.class);

    private final AreaDeNegocioRepository areaDeNegocioRepository;

    public AreaDeNegocioQueryService(AreaDeNegocioRepository areaDeNegocioRepository) {
        this.areaDeNegocioRepository = areaDeNegocioRepository;
    }

    /**
     * Return a {@link Page} of {@link AreaDeNegocio} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AreaDeNegocio> findByCriteria(AreaDeNegocioCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AreaDeNegocio> specification = createSpecification(criteria);
        return areaDeNegocioRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AreaDeNegocioCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AreaDeNegocio> specification = createSpecification(criteria);
        return areaDeNegocioRepository.count(specification);
    }

    /**
     * Function to convert {@link AreaDeNegocioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AreaDeNegocio> createSpecification(AreaDeNegocioCriteria criteria) {
        Specification<AreaDeNegocio> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AreaDeNegocio_.id),
                buildStringSpecification(criteria.getNombre(), AreaDeNegocio_.nombre),
                buildStringSpecification(criteria.getDescripcion(), AreaDeNegocio_.descripcion),
                buildSpecification(criteria.getVendedoresId(), root -> root.join(AreaDeNegocio_.vendedores, JoinType.LEFT).get(Vendedor_.id)
                )
            );
        }
        return specification;
    }
}
