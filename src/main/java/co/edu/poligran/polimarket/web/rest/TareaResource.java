package co.edu.poligran.polimarket.web.rest;

import co.edu.poligran.polimarket.domain.Tarea;
import co.edu.poligran.polimarket.repository.TareaRepository;
import co.edu.poligran.polimarket.service.TareaQueryService;
import co.edu.poligran.polimarket.service.TareaService;
import co.edu.poligran.polimarket.service.criteria.TareaCriteria;
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
 * REST controller for managing {@link co.edu.poligran.polimarket.domain.Tarea}.
 */
@RestController
@RequestMapping("/api/tareas")
public class TareaResource {

    private static final Logger LOG = LoggerFactory.getLogger(TareaResource.class);

    private static final String ENTITY_NAME = "tarea";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TareaService tareaService;

    private final TareaRepository tareaRepository;

    private final TareaQueryService tareaQueryService;

    public TareaResource(TareaService tareaService, TareaRepository tareaRepository, TareaQueryService tareaQueryService) {
        this.tareaService = tareaService;
        this.tareaRepository = tareaRepository;
        this.tareaQueryService = tareaQueryService;
    }

    /**
     * {@code POST  /tareas} : Create a new tarea.
     *
     * @param tarea the tarea to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tarea, or with status {@code 400 (Bad Request)} if the tarea has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Tarea> createTarea(@RequestBody Tarea tarea) throws URISyntaxException {
        LOG.debug("REST request to save Tarea : {}", tarea);
        if (tarea.getId() != null) {
            throw new BadRequestAlertException("A new tarea cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tarea = tareaService.save(tarea);
        return ResponseEntity.created(new URI("/api/tareas/" + tarea.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tarea.getId().toString()))
            .body(tarea);
    }

    /**
     * {@code PUT  /tareas/:id} : Updates an existing tarea.
     *
     * @param id the id of the tarea to save.
     * @param tarea the tarea to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarea,
     * or with status {@code 400 (Bad Request)} if the tarea is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tarea couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Tarea> updateTarea(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tarea tarea)
        throws URISyntaxException {
        LOG.debug("REST request to update Tarea : {}, {}", id, tarea);
        if (tarea.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarea.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tareaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tarea = tareaService.update(tarea);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tarea.getId().toString()))
            .body(tarea);
    }

    /**
     * {@code PATCH  /tareas/:id} : Partial updates given fields of an existing tarea, field will ignore if it is null
     *
     * @param id the id of the tarea to save.
     * @param tarea the tarea to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarea,
     * or with status {@code 400 (Bad Request)} if the tarea is not valid,
     * or with status {@code 404 (Not Found)} if the tarea is not found,
     * or with status {@code 500 (Internal Server Error)} if the tarea couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Tarea> partialUpdateTarea(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tarea tarea)
        throws URISyntaxException {
        LOG.debug("REST request to partial update Tarea partially : {}, {}", id, tarea);
        if (tarea.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarea.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tareaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tarea> result = tareaService.partialUpdate(tarea);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tarea.getId().toString())
        );
    }

    /**
     * {@code GET  /tareas} : get all the tareas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tareas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Tarea>> getAllTareas(
        TareaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Tareas by criteria: {}", criteria);

        Page<Tarea> page = tareaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tareas/count} : count all the tareas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTareas(TareaCriteria criteria) {
        LOG.debug("REST request to count Tareas by criteria: {}", criteria);
        return ResponseEntity.ok().body(tareaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tareas/:id} : get the "id" tarea.
     *
     * @param id the id of the tarea to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tarea, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tarea> getTarea(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Tarea : {}", id);
        Optional<Tarea> tarea = tareaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tarea);
    }

    /**
     * {@code DELETE  /tareas/:id} : delete the "id" tarea.
     *
     * @param id the id of the tarea to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarea(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Tarea : {}", id);
        tareaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
