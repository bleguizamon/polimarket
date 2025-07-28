package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.AreaDeNegocio;
import co.edu.poligran.polimarket.repository.AreaDeNegocioRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poligran.polimarket.domain.AreaDeNegocio}.
 */
@Service
@Transactional
public class AreaDeNegocioService {

    private static final Logger LOG = LoggerFactory.getLogger(AreaDeNegocioService.class);

    private final AreaDeNegocioRepository areaDeNegocioRepository;

    public AreaDeNegocioService(AreaDeNegocioRepository areaDeNegocioRepository) {
        this.areaDeNegocioRepository = areaDeNegocioRepository;
    }

    /**
     * Save a areaDeNegocio.
     *
     * @param areaDeNegocio the entity to save.
     * @return the persisted entity.
     */
    public AreaDeNegocio save(AreaDeNegocio areaDeNegocio) {
        LOG.debug("Request to save AreaDeNegocio : {}", areaDeNegocio);
        return areaDeNegocioRepository.save(areaDeNegocio);
    }

    /**
     * Update a areaDeNegocio.
     *
     * @param areaDeNegocio the entity to save.
     * @return the persisted entity.
     */
    public AreaDeNegocio update(AreaDeNegocio areaDeNegocio) {
        LOG.debug("Request to update AreaDeNegocio : {}", areaDeNegocio);
        return areaDeNegocioRepository.save(areaDeNegocio);
    }

    /**
     * Partially update a areaDeNegocio.
     *
     * @param areaDeNegocio the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AreaDeNegocio> partialUpdate(AreaDeNegocio areaDeNegocio) {
        LOG.debug("Request to partially update AreaDeNegocio : {}", areaDeNegocio);

        return areaDeNegocioRepository
            .findById(areaDeNegocio.getId())
            .map(existingAreaDeNegocio -> {
                if (areaDeNegocio.getNombre() != null) {
                    existingAreaDeNegocio.setNombre(areaDeNegocio.getNombre());
                }
                if (areaDeNegocio.getDescripcion() != null) {
                    existingAreaDeNegocio.setDescripcion(areaDeNegocio.getDescripcion());
                }

                return existingAreaDeNegocio;
            })
            .map(areaDeNegocioRepository::save);
    }

    /**
     * Get one areaDeNegocio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AreaDeNegocio> findOne(Long id) {
        LOG.debug("Request to get AreaDeNegocio : {}", id);
        return areaDeNegocioRepository.findById(id);
    }

    /**
     * Delete the areaDeNegocio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AreaDeNegocio : {}", id);
        areaDeNegocioRepository.deleteById(id);
    }
}
