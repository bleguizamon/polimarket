package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.Vendedor;
import co.edu.poligran.polimarket.repository.VendedorRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poligran.polimarket.domain.Vendedor}.
 */
@Service
@Transactional
public class VendedorService {

    private static final Logger LOG = LoggerFactory.getLogger(VendedorService.class);

    private final VendedorRepository vendedorRepository;

    public VendedorService(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    /**
     * Save a vendedor.
     *
     * @param vendedor the entity to save.
     * @return the persisted entity.
     */
    public Vendedor save(Vendedor vendedor) {
        LOG.debug("Request to save Vendedor : {}", vendedor);
        return vendedorRepository.save(vendedor);
    }

    /**
     * Update a vendedor.
     *
     * @param vendedor the entity to save.
     * @return the persisted entity.
     */
    public Vendedor update(Vendedor vendedor) {
        LOG.debug("Request to update Vendedor : {}", vendedor);
        return vendedorRepository.save(vendedor);
    }

    /**
     * Partially update a vendedor.
     *
     * @param vendedor the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Vendedor> partialUpdate(Vendedor vendedor) {
        LOG.debug("Request to partially update Vendedor : {}", vendedor);

        return vendedorRepository
            .findById(vendedor.getId())
            .map(existingVendedor -> {
                if (vendedor.getNombre() != null) {
                    existingVendedor.setNombre(vendedor.getNombre());
                }
                if (vendedor.getAutorizado() != null) {
                    existingVendedor.setAutorizado(vendedor.getAutorizado());
                }

                return existingVendedor;
            })
            .map(vendedorRepository::save);
    }

    /**
     * Get all the vendedors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Vendedor> findAllWithEagerRelationships(Pageable pageable) {
        return vendedorRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one vendedor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Vendedor> findOne(Long id) {
        LOG.debug("Request to get Vendedor : {}", id);
        return vendedorRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the vendedor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Vendedor : {}", id);
        vendedorRepository.deleteById(id);
    }
}
