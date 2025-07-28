package co.edu.poligran.polimarket.web.rest;

import co.edu.poligran.polimarket.domain.Vendedor;
import co.edu.poligran.polimarket.repository.VendedorRepository;
import co.edu.poligran.polimarket.service.VendedorQueryService;
import co.edu.poligran.polimarket.service.VendedorService;
import co.edu.poligran.polimarket.service.criteria.VendedorCriteria;
import co.edu.poligran.polimarket.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.edu.poligran.polimarket.domain.Vendedor}.
 */
@RestController
@RequestMapping("/api/vendedors")
public class VendedorResource {

    private static final Logger LOG = LoggerFactory.getLogger(VendedorResource.class);

    private static final String ENTITY_NAME = "vendedor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VendedorService vendedorService;

    private final VendedorRepository vendedorRepository;

    private final VendedorQueryService vendedorQueryService;

    public VendedorResource(
        VendedorService vendedorService,
        VendedorRepository vendedorRepository,
        VendedorQueryService vendedorQueryService
    ) {
        this.vendedorService = vendedorService;
        this.vendedorRepository = vendedorRepository;
        this.vendedorQueryService = vendedorQueryService;
    }

    /**
     * {@code POST  /vendedors} : Create a new vendedor.
     *
     * @param vendedor the vendedor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vendedor, or with status {@code 400 (Bad Request)} if the vendedor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Vendedor> createVendedor(@Valid @RequestBody Vendedor vendedor) throws URISyntaxException {
        LOG.debug("REST request to save Vendedor : {}", vendedor);
        if (vendedor.getId() != null) {
            throw new BadRequestAlertException("A new vendedor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vendedor = vendedorService.save(vendedor);
        return ResponseEntity.created(new URI("/api/vendedors/" + vendedor.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vendedor.getId().toString()))
            .body(vendedor);
    }

    /**
     * {@code PUT  /vendedors/:id} : Updates an existing vendedor.
     *
     * @param id the id of the vendedor to save.
     * @param vendedor the vendedor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vendedor,
     * or with status {@code 400 (Bad Request)} if the vendedor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vendedor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Vendedor> updateVendedor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Vendedor vendedor
    ) throws URISyntaxException {
        LOG.debug("REST request to update Vendedor : {}, {}", id, vendedor);
        if (vendedor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vendedor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vendedorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vendedor = vendedorService.update(vendedor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vendedor.getId().toString()))
            .body(vendedor);
    }

    /**
     * {@code PATCH  /vendedors/:id} : Partial updates given fields of an existing vendedor, field will ignore if it is null
     *
     * @param id the id of the vendedor to save.
     * @param vendedor the vendedor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vendedor,
     * or with status {@code 400 (Bad Request)} if the vendedor is not valid,
     * or with status {@code 404 (Not Found)} if the vendedor is not found,
     * or with status {@code 500 (Internal Server Error)} if the vendedor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Vendedor> partialUpdateVendedor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Vendedor vendedor
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Vendedor partially : {}, {}", id, vendedor);
        if (vendedor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vendedor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vendedorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vendedor> result = vendedorService.partialUpdate(vendedor);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vendedor.getId().toString())
        );
    }

    /**
     * {@code GET  /vendedors} : get all the vendedors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vendedors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Vendedor>> getAllVendedors(
        VendedorCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Vendedors by criteria: {}", criteria);

        Page<Vendedor> page = vendedorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vendedors/count} : count all the vendedors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countVendedors(VendedorCriteria criteria) {
        LOG.debug("REST request to count Vendedors by criteria: {}", criteria);
        return ResponseEntity.ok().body(vendedorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vendedors/:id} : get the "id" vendedor.
     *
     * @param id the id of the vendedor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vendedor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vendedor> getVendedor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Vendedor : {}", id);
        Optional<Vendedor> vendedor = vendedorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vendedor);
    }

    /**
     * {@code DELETE  /vendedors/:id} : delete the "id" vendedor.
     *
     * @param id the id of the vendedor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendedor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Vendedor : {}", id);
        vendedorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
