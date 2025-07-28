package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.Producto;
import co.edu.poligran.polimarket.repository.ProductoRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poligran.polimarket.domain.Producto}.
 */
@Service
@Transactional
public class ProductoService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Save a producto.
     *
     * @param producto the entity to save.
     * @return the persisted entity.
     */
    public Producto save(Producto producto) {
        LOG.debug("Request to save Producto : {}", producto);
        return productoRepository.save(producto);
    }

    /**
     * Update a producto.
     *
     * @param producto the entity to save.
     * @return the persisted entity.
     */
    public Producto update(Producto producto) {
        LOG.debug("Request to update Producto : {}", producto);
        return productoRepository.save(producto);
    }

    /**
     * Partially update a producto.
     *
     * @param producto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Producto> partialUpdate(Producto producto) {
        LOG.debug("Request to partially update Producto : {}", producto);

        return productoRepository
            .findById(producto.getId())
            .map(existingProducto -> {
                if (producto.getNombre() != null) {
                    existingProducto.setNombre(producto.getNombre());
                }
                if (producto.getCantidadDisponible() != null) {
                    existingProducto.setCantidadDisponible(producto.getCantidadDisponible());
                }

                return existingProducto;
            })
            .map(productoRepository::save);
    }

    /**
     * Get all the productos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Producto> findAllWithEagerRelationships(Pageable pageable) {
        return productoRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one producto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Producto> findOne(Long id) {
        LOG.debug("Request to get Producto : {}", id);
        return productoRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the producto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Producto : {}", id);
        productoRepository.deleteById(id);
    }
}
