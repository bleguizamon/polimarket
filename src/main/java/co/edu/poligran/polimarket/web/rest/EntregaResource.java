package co.edu.poligran.polimarket.web.rest;

import co.edu.poligran.polimarket.domain.Entrega;
import co.edu.poligran.polimarket.repository.EntregaRepository;
import co.edu.poligran.polimarket.service.EntregaQueryService;
import co.edu.poligran.polimarket.service.EntregaService;
import co.edu.poligran.polimarket.service.criteria.EntregaCriteria;
import co.edu.poligran.polimarket.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link co.edu.poligran.polimarket.domain.Entrega}.
 */
@RestController
@RequestMapping("/api/entregas")
public class EntregaResource {

    private static final Logger LOG = LoggerFactory.getLogger(EntregaResource.class);

    private static final String ENTITY_NAME = "entrega";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntregaService entregaService;

    private final EntregaRepository entregaRepository;

    private final EntregaQueryService entregaQueryService;

    public EntregaResource(EntregaService entregaService, EntregaRepository entregaRepository, EntregaQueryService entregaQueryService) {
        this.entregaService = entregaService;
        this.entregaRepository = entregaRepository;
        this.entregaQueryService = entregaQueryService;
    }

    /**
     * {@code POST  /entregas} : Create a new entrega.
     *
     * @param entrega the entrega to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entrega, or with status {@code 400 (Bad Request)} if the entrega has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Entrega> createEntrega(@RequestBody Entrega entrega) throws URISyntaxException {
        LOG.debug("REST request to save Entrega : {}", entrega);
        if (entrega.getId() != null) {
            throw new BadRequestAlertException("A new entrega cannot already have an ID", ENTITY_NAME, "idexists");
        }
        entrega = entregaService.save(entrega);
        return ResponseEntity.created(new URI("/api/entregas/" + entrega.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, entrega.getId().toString()))
            .body(entrega);
    }

    /**
     * {@code PUT  /entregas/:id} : Updates an existing entrega.
     *
     * @param id the id of the entrega to save.
     * @param entrega the entrega to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entrega,
     * or with status {@code 400 (Bad Request)} if the entrega is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entrega couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Entrega> updateEntrega(@PathVariable(value = "id", required = false) final Long id, @RequestBody Entrega entrega)
        throws URISyntaxException {
        LOG.debug("REST request to update Entrega : {}, {}", id, entrega);
        if (entrega.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entrega.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entregaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        entrega = entregaService.update(entrega);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entrega.getId().toString()))
            .body(entrega);
    }

    /**
     * {@code PATCH  /entregas/:id} : Partial updates given fields of an existing entrega, field will ignore if it is null
     *
     * @param id the id of the entrega to save.
     * @param entrega the entrega to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entrega,
     * or with status {@code 400 (Bad Request)} if the entrega is not valid,
     * or with status {@code 404 (Not Found)} if the entrega is not found,
     * or with status {@code 500 (Internal Server Error)} if the entrega couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Entrega> partialUpdateEntrega(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Entrega entrega
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Entrega partially : {}, {}", id, entrega);
        if (entrega.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entrega.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entregaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Entrega> result = entregaService.partialUpdate(entrega);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entrega.getId().toString())
        );
    }

    /**
     * {@code GET  /entregas} : get all the entregas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entregas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Entrega>> getAllEntregas(
        EntregaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Entregas by criteria: {}", criteria);

        Page<Entrega> page = entregaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /entregas/count} : count all the entregas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEntregas(EntregaCriteria criteria) {
        LOG.debug("REST request to count Entregas by criteria: {}", criteria);
        return ResponseEntity.ok().body(entregaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /entregas/:id} : get the "id" entrega.
     *
     * @param id the id of the entrega to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entrega, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Entrega> getEntrega(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Entrega : {}", id);
        Optional<Entrega> entrega = entregaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(entrega);
    }

    /**
     * {@code DELETE  /entregas/:id} : delete the "id" entrega.
     *
     * @param id the id of the entrega to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntrega(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Entrega : {}", id);
        entregaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
