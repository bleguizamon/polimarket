package co.edu.poligran.polimarket.service;

import co.edu.poligran.polimarket.domain.Cliente;
import co.edu.poligran.polimarket.repository.ClienteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poligran.polimarket.domain.Cliente}.
 */
@Service
@Transactional
public class ClienteService {

    private static final Logger LOG = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Save a cliente.
     *
     * @param cliente the entity to save.
     * @return the persisted entity.
     */
    public Cliente save(Cliente cliente) {
        LOG.debug("Request to save Cliente : {}", cliente);
        return clienteRepository.save(cliente);
    }

    /**
     * Update a cliente.
     *
     * @param cliente the entity to save.
     * @return the persisted entity.
     */
    public Cliente update(Cliente cliente) {
        LOG.debug("Request to update Cliente : {}", cliente);
        return clienteRepository.save(cliente);
    }

    /**
     * Partially update a cliente.
     *
     * @param cliente the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Cliente> partialUpdate(Cliente cliente) {
        LOG.debug("Request to partially update Cliente : {}", cliente);

        return clienteRepository
            .findById(cliente.getId())
            .map(existingCliente -> {
                if (cliente.getNombre() != null) {
                    existingCliente.setNombre(cliente.getNombre());
                }
                if (cliente.getContacto() != null) {
                    existingCliente.setContacto(cliente.getContacto());
                }

                return existingCliente;
            })
            .map(clienteRepository::save);
    }

    /**
     * Get all the clientes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Cliente> findAllWithEagerRelationships(Pageable pageable) {
        return clienteRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one cliente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> findOne(Long id) {
        LOG.debug("Request to get Cliente : {}", id);
        return clienteRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the cliente by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Cliente : {}", id);
        clienteRepository.deleteById(id);
    }
}
