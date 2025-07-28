package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.Inventario;
import co.edu.poligran.polimarket.repository.InventarioRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poligran.polimarket.domain.Inventario}.
 */
@Service
@Transactional
public class InventarioService {

    private static final Logger LOG = LoggerFactory.getLogger(InventarioService.class);

    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    /**
     * Save a inventario.
     *
     * @param inventario the entity to save.
     * @return the persisted entity.
     */
    public Inventario save(Inventario inventario) {
        LOG.debug("Request to save Inventario : {}", inventario);
        return inventarioRepository.save(inventario);
    }

    /**
     * Update a inventario.
     *
     * @param inventario the entity to save.
     * @return the persisted entity.
     */
    public Inventario update(Inventario inventario) {
        LOG.debug("Request to update Inventario : {}", inventario);
        return inventarioRepository.save(inventario);
    }

    /**
     * Partially update a inventario.
     *
     * @param inventario the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Inventario> partialUpdate(Inventario inventario) {
        LOG.debug("Request to partially update Inventario : {}", inventario);

        return inventarioRepository
            .findById(inventario.getId())
            .map(existingInventario -> {
                if (inventario.getCantidad() != null) {
                    existingInventario.setCantidad(inventario.getCantidad());
                }
                if (inventario.getUbicacion() != null) {
                    existingInventario.setUbicacion(inventario.getUbicacion());
                }

                return existingInventario;
            })
            .map(inventarioRepository::save);
    }

    /**
     * Get all the inventarios with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Inventario> findAllWithEagerRelationships(Pageable pageable) {
        return inventarioRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one inventario by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Inventario> findOne(Long id) {
        LOG.debug("Request to get Inventario : {}", id);
        return inventarioRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the inventario by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Inventario : {}", id);
        inventarioRepository.deleteById(id);
    }
}
