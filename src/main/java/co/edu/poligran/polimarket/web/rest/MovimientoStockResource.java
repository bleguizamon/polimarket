package co.edu.poligran.polimarket.web.rest;

import co.edu.poligran.polimarket.domain.MovimientoStock;
import co.edu.poligran.polimarket.repository.MovimientoStockRepository;
import co.edu.poligran.polimarket.service.MovimientoStockQueryService;
import co.edu.poligran.polimarket.service.MovimientoStockService;
import co.edu.poligran.polimarket.service.criteria.MovimientoStockCriteria;
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
 * REST controller for managing {@link co.edu.poligran.polimarket.domain.MovimientoStock}.
 */
@RestController
@RequestMapping("/api/movimiento-stocks")
public class MovimientoStockResource {

    private static final Logger LOG = LoggerFactory.getLogger(MovimientoStockResource.class);

    private static final String ENTITY_NAME = "movimientoStock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MovimientoStockService movimientoStockService;

    private final MovimientoStockRepository movimientoStockRepository;

    private final MovimientoStockQueryService movimientoStockQueryService;

    public MovimientoStockResource(
        MovimientoStockService movimientoStockService,
        MovimientoStockRepository movimientoStockRepository,
        MovimientoStockQueryService movimientoStockQueryService
    ) {
        this.movimientoStockService = movimientoStockService;
        this.movimientoStockRepository = movimientoStockRepository;
        this.movimientoStockQueryService = movimientoStockQueryService;
    }

    /**
     * {@code POST  /movimiento-stocks} : Create a new movimientoStock.
     *
     * @param movimientoStock the movimientoStock to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new movimientoStock, or with status {@code 400 (Bad Request)} if the movimientoStock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MovimientoStock> createMovimientoStock(@RequestBody MovimientoStock movimientoStock) throws URISyntaxException {
        LOG.debug("REST request to save MovimientoStock : {}", movimientoStock);
        if (movimientoStock.getId() != null) {
            throw new BadRequestAlertException("A new movimientoStock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        movimientoStock = movimientoStockService.save(movimientoStock);
        return ResponseEntity.created(new URI("/api/movimiento-stocks/" + movimientoStock.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, movimientoStock.getId().toString()))
            .body(movimientoStock);
    }

    /**
     * {@code PUT  /movimiento-stocks/:id} : Updates an existing movimientoStock.
     *
     * @param id the id of the movimientoStock to save.
     * @param movimientoStock the movimientoStock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movimientoStock,
     * or with status {@code 400 (Bad Request)} if the movimientoStock is not valid,
     * or with status {@code 500 (Internal Server Error)} if the movimientoStock couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoStock> updateMovimientoStock(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MovimientoStock movimientoStock
    ) throws URISyntaxException {
        LOG.debug("REST request to update MovimientoStock : {}, {}", id, movimientoStock);
        if (movimientoStock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, movimientoStock.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!movimientoStockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        movimientoStock = movimientoStockService.update(movimientoStock);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movimientoStock.getId().toString()))
            .body(movimientoStock);
    }

    /**
     * {@code PATCH  /movimiento-stocks/:id} : Partial updates given fields of an existing movimientoStock, field will ignore if it is null
     *
     * @param id the id of the movimientoStock to save.
     * @param movimientoStock the movimientoStock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movimientoStock,
     * or with status {@code 400 (Bad Request)} if the movimientoStock is not valid,
     * or with status {@code 404 (Not Found)} if the movimientoStock is not found,
     * or with status {@code 500 (Internal Server Error)} if the movimientoStock couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MovimientoStock> partialUpdateMovimientoStock(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MovimientoStock movimientoStock
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MovimientoStock partially : {}, {}", id, movimientoStock);
        if (movimientoStock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, movimientoStock.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!movimientoStockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MovimientoStock> result = movimientoStockService.partialUpdate(movimientoStock);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movimientoStock.getId().toString())
        );
    }

    /**
     * {@code GET  /movimiento-stocks} : get all the movimientoStocks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of movimientoStocks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MovimientoStock>> getAllMovimientoStocks(
        MovimientoStockCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MovimientoStocks by criteria: {}", criteria);

        Page<MovimientoStock> page = movimientoStockQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /movimiento-stocks/count} : count all the movimientoStocks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMovimientoStocks(MovimientoStockCriteria criteria) {
        LOG.debug("REST request to count MovimientoStocks by criteria: {}", criteria);
        return ResponseEntity.ok().body(movimientoStockQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /movimiento-stocks/:id} : get the "id" movimientoStock.
     *
     * @param id the id of the movimientoStock to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the movimientoStock, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoStock> getMovimientoStock(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MovimientoStock : {}", id);
        Optional<MovimientoStock> movimientoStock = movimientoStockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(movimientoStock);
    }

    /**
     * {@code DELETE  /movimiento-stocks/:id} : delete the "id" movimientoStock.
     *
     * @param id the id of the movimientoStock to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovimientoStock(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MovimientoStock : {}", id);
        movimientoStockService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
