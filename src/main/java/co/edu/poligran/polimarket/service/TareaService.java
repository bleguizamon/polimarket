package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.Tarea;
import co.edu.poligran.polimarket.repository.TareaRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poligran.polimarket.domain.Tarea}.
 */
@Service
@Transactional
public class TareaService {

    private static final Logger LOG = LoggerFactory.getLogger(TareaService.class);

    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    /**
     * Save a tarea.
     *
     * @param tarea the entity to save.
     * @return the persisted entity.
     */
    public Tarea save(Tarea tarea) {
        LOG.debug("Request to save Tarea : {}", tarea);
        return tareaRepository.save(tarea);
    }

    /**
     * Update a tarea.
     *
     * @param tarea the entity to save.
     * @return the persisted entity.
     */
    public Tarea update(Tarea tarea) {
        LOG.debug("Request to update Tarea : {}", tarea);
        return tareaRepository.save(tarea);
    }

    /**
     * Partially update a tarea.
     *
     * @param tarea the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Tarea> partialUpdate(Tarea tarea) {
        LOG.debug("Request to partially update Tarea : {}", tarea);

        return tareaRepository
            .findById(tarea.getId())
            .map(existingTarea -> {
                if (tarea.getDescripcion() != null) {
                    existingTarea.setDescripcion(tarea.getDescripcion());
                }
                if (tarea.getEstado() != null) {
                    existingTarea.setEstado(tarea.getEstado());
                }

                return existingTarea;
            })
            .map(tareaRepository::save);
    }

    /**
     * Get all the tareas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Tarea> findAllWithEagerRelationships(Pageable pageable) {
        return tareaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one tarea by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Tarea> findOne(Long id) {
        LOG.debug("Request to get Tarea : {}", id);
        return tareaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the tarea by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Tarea : {}", id);
        tareaRepository.deleteById(id);
    }
}
