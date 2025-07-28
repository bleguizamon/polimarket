package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.*; // for static metamodels
import co.edu.poligran.polimarket.domain.Vendedor;
import co.edu.poligran.polimarket.repository.VendedorRepository;
import co.edu.poligran.polimarket.service.criteria.VendedorCriteria;
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
 * Service for executing complex queries for {@link Vendedor} entities in the database.
 * The main input is a {@link VendedorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Vendedor} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VendedorQueryService extends QueryService<Vendedor> {

    private static final Logger LOG = LoggerFactory.getLogger(VendedorQueryService.class);

    private final VendedorRepository vendedorRepository;

    public VendedorQueryService(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    /**
     * Return a {@link Page} of {@link Vendedor} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Vendedor> findByCriteria(VendedorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vendedor> specification = createSpecification(criteria);
        return vendedorRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VendedorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Vendedor> specification = createSpecification(criteria);
        return vendedorRepository.count(specification);
    }

    /**
     * Function to convert {@link VendedorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vendedor> createSpecification(VendedorCriteria criteria) {
        Specification<Vendedor> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Vendedor_.id),
                buildStringSpecification(criteria.getNombre(), Vendedor_.nombre),
                buildSpecification(criteria.getAutorizado(), Vendedor_.autorizado),
                buildSpecification(criteria.getClientesId(), root -> root.join(Vendedor_.clientes, JoinType.LEFT).get(Cliente_.id)),
                buildSpecification(criteria.getTareasId(), root -> root.join(Vendedor_.tareas, JoinType.LEFT).get(Tarea_.id)),
                buildSpecification(criteria.getAreaDeNegocioId(), root ->
                    root.join(Vendedor_.areaDeNegocio, JoinType.LEFT).get(AreaDeNegocio_.id)
                )
            );
        }
        return specification;
    }
}
