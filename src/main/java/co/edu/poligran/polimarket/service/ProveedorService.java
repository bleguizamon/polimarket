package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.Proveedor;
import co.edu.poligran.polimarket.repository.ProveedorRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poligran.polimarket.domain.Proveedor}.
 */
@Service
@Transactional
public class ProveedorService {

    private static final Logger LOG = LoggerFactory.getLogger(ProveedorService.class);

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    /**
     * Save a proveedor.
     *
     * @param proveedor the entity to save.
     * @return the persisted entity.
     */
    public Proveedor save(Proveedor proveedor) {
        LOG.debug("Request to save Proveedor : {}", proveedor);
        return proveedorRepository.save(proveedor);
    }

    /**
     * Update a proveedor.
     *
     * @param proveedor the entity to save.
     * @return the persisted entity.
     */
    public Proveedor update(Proveedor proveedor) {
        LOG.debug("Request to update Proveedor : {}", proveedor);
        return proveedorRepository.save(proveedor);
    }

    /**
     * Partially update a proveedor.
     *
     * @param proveedor the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Proveedor> partialUpdate(Proveedor proveedor) {
        LOG.debug("Request to partially update Proveedor : {}", proveedor);

        return proveedorRepository
            .findById(proveedor.getId())
            .map(existingProveedor -> {
                if (proveedor.getNombre() != null) {
                    existingProveedor.setNombre(proveedor.getNombre());
                }
                if (proveedor.getTelefono() != null) {
                    existingProveedor.setTelefono(proveedor.getTelefono());
                }

                return existingProveedor;
            })
            .map(proveedorRepository::save);
    }

    /**
     * Get one proveedor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Proveedor> findOne(Long id) {
        LOG.debug("Request to get Proveedor : {}", id);
        return proveedorRepository.findById(id);
    }

    /**
     * Delete the proveedor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Proveedor : {}", id);
        proveedorRepository.deleteById(id);
    }
}
