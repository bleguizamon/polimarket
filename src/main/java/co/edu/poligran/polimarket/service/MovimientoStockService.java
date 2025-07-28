package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.MovimientoStock;
import co.edu.poligran.polimarket.repository.MovimientoStockRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poligran.polimarket.domain.MovimientoStock}.
 */
@Service
@Transactional
public class MovimientoStockService {

    private static final Logger LOG = LoggerFactory.getLogger(MovimientoStockService.class);

    private final MovimientoStockRepository movimientoStockRepository;

    public MovimientoStockService(MovimientoStockRepository movimientoStockRepository) {
        this.movimientoStockRepository = movimientoStockRepository;
    }

    /**
     * Save a movimientoStock.
     *
     * @param movimientoStock the entity to save.
     * @return the persisted entity.
     */
    public MovimientoStock save(MovimientoStock movimientoStock) {
        LOG.debug("Request to save MovimientoStock : {}", movimientoStock);
        return movimientoStockRepository.save(movimientoStock);
    }

    /**
     * Update a movimientoStock.
     *
     * @param movimientoStock the entity to save.
     * @return the persisted entity.
     */
    public MovimientoStock update(MovimientoStock movimientoStock) {
        LOG.debug("Request to update MovimientoStock : {}", movimientoStock);
        return movimientoStockRepository.save(movimientoStock);
    }

    /**
     * Partially update a movimientoStock.
     *
     * @param movimientoStock the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MovimientoStock> partialUpdate(MovimientoStock movimientoStock) {
        LOG.debug("Request to partially update MovimientoStock : {}", movimientoStock);

        return movimientoStockRepository
            .findById(movimientoStock.getId())
            .map(existingMovimientoStock -> {
                if (movimientoStock.getFecha() != null) {
                    existingMovimientoStock.setFecha(movimientoStock.getFecha());
                }
                if (movimientoStock.getTipo() != null) {
                    existingMovimientoStock.setTipo(movimientoStock.getTipo());
                }
                if (movimientoStock.getCantidad() != null) {
                    existingMovimientoStock.setCantidad(movimientoStock.getCantidad());
                }
                if (movimientoStock.getReferencia() != null) {
                    existingMovimientoStock.setReferencia(movimientoStock.getReferencia());
                }

                return existingMovimientoStock;
            })
            .map(movimientoStockRepository::save);
    }

    /**
     * Get all the movimientoStocks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MovimientoStock> findAllWithEagerRelationships(Pageable pageable) {
        return movimientoStockRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one movimientoStock by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MovimientoStock> findOne(Long id) {
        LOG.debug("Request to get MovimientoStock : {}", id);
        return movimientoStockRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the movimientoStock by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MovimientoStock : {}", id);
        movimientoStockRepository.deleteById(id);
    }
}
