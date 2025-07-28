package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.Entrega;
import co.edu.poligran.polimarket.repository.EntregaRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poligran.polimarket.domain.Entrega}.
 */
@Service
@Transactional
public class EntregaService {

    private static final Logger LOG = LoggerFactory.getLogger(EntregaService.class);

    private final EntregaRepository entregaRepository;

    public EntregaService(EntregaRepository entregaRepository) {
        this.entregaRepository = entregaRepository;
    }

    /**
     * Save a entrega.
     *
     * @param entrega the entity to save.
     * @return the persisted entity.
     */
    public Entrega save(Entrega entrega) {
        LOG.debug("Request to save Entrega : {}", entrega);
        return entregaRepository.save(entrega);
    }

    /**
     * Update a entrega.
     *
     * @param entrega the entity to save.
     * @return the persisted entity.
     */
    public Entrega update(Entrega entrega) {
        LOG.debug("Request to update Entrega : {}", entrega);
        return entregaRepository.save(entrega);
    }

    /**
     * Partially update a entrega.
     *
     * @param entrega the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Entrega> partialUpdate(Entrega entrega) {
        LOG.debug("Request to partially update Entrega : {}", entrega);

        return entregaRepository
            .findById(entrega.getId())
            .map(existingEntrega -> {
                if (entrega.getFecha() != null) {
                    existingEntrega.setFecha(entrega.getFecha());
                }
                if (entrega.getEstado() != null) {
                    existingEntrega.setEstado(entrega.getEstado());
                }

                return existingEntrega;
            })
            .map(entregaRepository::save);
    }

    /**
     * Get all the entregas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Entrega> findAllWithEagerRelationships(Pageable pageable) {
        return entregaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one entrega by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Entrega> findOne(Long id) {
        LOG.debug("Request to get Entrega : {}", id);
        return entregaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the entrega by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Entrega : {}", id);
        entregaRepository.deleteById(id);
    }
}
