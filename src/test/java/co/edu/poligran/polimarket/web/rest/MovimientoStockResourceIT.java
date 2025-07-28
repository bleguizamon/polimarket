package co.edu.poligran.polimarket.web.rest;

import static co.edu.poligran.polimarket.domain.MovimientoStockAsserts.*;
import static co.edu.poligran.polimarket.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.poligran.polimarket.IntegrationTest;
import co.edu.poligran.polimarket.domain.Inventario;
import co.edu.poligran.polimarket.domain.MovimientoStock;
import co.edu.poligran.polimarket.domain.Producto;
import co.edu.poligran.polimarket.repository.MovimientoStockRepository;
import co.edu.poligran.polimarket.service.MovimientoStockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MovimientoStockResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MovimientoStockResourceIT {

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;
    private static final Integer SMALLER_CANTIDAD = 1 - 1;

    private static final String DEFAULT_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCIA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/movimiento-stocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MovimientoStockRepository movimientoStockRepository;

    @Mock
    private MovimientoStockRepository movimientoStockRepositoryMock;

    @Mock
    private MovimientoStockService movimientoStockServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMovimientoStockMockMvc;

    private MovimientoStock movimientoStock;

    private MovimientoStock insertedMovimientoStock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MovimientoStock createEntity() {
        return new MovimientoStock().fecha(DEFAULT_FECHA).tipo(DEFAULT_TIPO).cantidad(DEFAULT_CANTIDAD).referencia(DEFAULT_REFERENCIA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MovimientoStock createUpdatedEntity() {
        return new MovimientoStock().fecha(UPDATED_FECHA).tipo(UPDATED_TIPO).cantidad(UPDATED_CANTIDAD).referencia(UPDATED_REFERENCIA);
    }

    @BeforeEach
    void initTest() {
        movimientoStock = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMovimientoStock != null) {
            movimientoStockRepository.delete(insertedMovimientoStock);
            insertedMovimientoStock = null;
        }
    }

    @Test
    @Transactional
    void createMovimientoStock() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MovimientoStock
        var returnedMovimientoStock = om.readValue(
            restMovimientoStockMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(movimientoStock)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MovimientoStock.class
        );

        // Validate the MovimientoStock in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMovimientoStockUpdatableFieldsEquals(returnedMovimientoStock, getPersistedMovimientoStock(returnedMovimientoStock));

        insertedMovimientoStock = returnedMovimientoStock;
    }

    @Test
    @Transactional
    void createMovimientoStockWithExistingId() throws Exception {
        // Create the MovimientoStock with an existing ID
        movimientoStock.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovimientoStockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(movimientoStock)))
            .andExpect(status().isBadRequest());

        // Validate the MovimientoStock in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMovimientoStocks() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList
        restMovimientoStockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movimientoStock.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].referencia").value(hasItem(DEFAULT_REFERENCIA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMovimientoStocksWithEagerRelationshipsIsEnabled() throws Exception {
        when(movimientoStockServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMovimientoStockMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(movimientoStockServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMovimientoStocksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(movimientoStockServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMovimientoStockMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(movimientoStockRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMovimientoStock() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get the movimientoStock
        restMovimientoStockMockMvc
            .perform(get(ENTITY_API_URL_ID, movimientoStock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(movimientoStock.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.referencia").value(DEFAULT_REFERENCIA));
    }

    @Test
    @Transactional
    void getMovimientoStocksByIdFiltering() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        Long id = movimientoStock.getId();

        defaultMovimientoStockFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMovimientoStockFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMovimientoStockFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where fecha equals to
        defaultMovimientoStockFiltering("fecha.equals=" + DEFAULT_FECHA, "fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where fecha in
        defaultMovimientoStockFiltering("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA, "fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where fecha is not null
        defaultMovimientoStockFiltering("fecha.specified=true", "fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where tipo equals to
        defaultMovimientoStockFiltering("tipo.equals=" + DEFAULT_TIPO, "tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where tipo in
        defaultMovimientoStockFiltering("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO, "tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where tipo is not null
        defaultMovimientoStockFiltering("tipo.specified=true", "tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByTipoContainsSomething() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where tipo contains
        defaultMovimientoStockFiltering("tipo.contains=" + DEFAULT_TIPO, "tipo.contains=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByTipoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where tipo does not contain
        defaultMovimientoStockFiltering("tipo.doesNotContain=" + UPDATED_TIPO, "tipo.doesNotContain=" + DEFAULT_TIPO);
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where cantidad equals to
        defaultMovimientoStockFiltering("cantidad.equals=" + DEFAULT_CANTIDAD, "cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where cantidad in
        defaultMovimientoStockFiltering("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD, "cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where cantidad is not null
        defaultMovimientoStockFiltering("cantidad.specified=true", "cantidad.specified=false");
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where cantidad is greater than or equal to
        defaultMovimientoStockFiltering(
            "cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD,
            "cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD
        );
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where cantidad is less than or equal to
        defaultMovimientoStockFiltering("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD, "cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where cantidad is less than
        defaultMovimientoStockFiltering("cantidad.lessThan=" + UPDATED_CANTIDAD, "cantidad.lessThan=" + DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where cantidad is greater than
        defaultMovimientoStockFiltering("cantidad.greaterThan=" + SMALLER_CANTIDAD, "cantidad.greaterThan=" + DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByReferenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where referencia equals to
        defaultMovimientoStockFiltering("referencia.equals=" + DEFAULT_REFERENCIA, "referencia.equals=" + UPDATED_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByReferenciaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where referencia in
        defaultMovimientoStockFiltering(
            "referencia.in=" + DEFAULT_REFERENCIA + "," + UPDATED_REFERENCIA,
            "referencia.in=" + UPDATED_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByReferenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where referencia is not null
        defaultMovimientoStockFiltering("referencia.specified=true", "referencia.specified=false");
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByReferenciaContainsSomething() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where referencia contains
        defaultMovimientoStockFiltering("referencia.contains=" + DEFAULT_REFERENCIA, "referencia.contains=" + UPDATED_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByReferenciaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        // Get all the movimientoStockList where referencia does not contain
        defaultMovimientoStockFiltering(
            "referencia.doesNotContain=" + UPDATED_REFERENCIA,
            "referencia.doesNotContain=" + DEFAULT_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByProductoIsEqualToSomething() throws Exception {
        Producto producto;
        if (TestUtil.findAll(em, Producto.class).isEmpty()) {
            movimientoStockRepository.saveAndFlush(movimientoStock);
            producto = ProductoResourceIT.createEntity();
        } else {
            producto = TestUtil.findAll(em, Producto.class).get(0);
        }
        em.persist(producto);
        em.flush();
        movimientoStock.setProducto(producto);
        movimientoStockRepository.saveAndFlush(movimientoStock);
        Long productoId = producto.getId();
        // Get all the movimientoStockList where producto equals to productoId
        defaultMovimientoStockShouldBeFound("productoId.equals=" + productoId);

        // Get all the movimientoStockList where producto equals to (productoId + 1)
        defaultMovimientoStockShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    @Test
    @Transactional
    void getAllMovimientoStocksByInventarioIsEqualToSomething() throws Exception {
        Inventario inventario;
        if (TestUtil.findAll(em, Inventario.class).isEmpty()) {
            movimientoStockRepository.saveAndFlush(movimientoStock);
            inventario = InventarioResourceIT.createEntity();
        } else {
            inventario = TestUtil.findAll(em, Inventario.class).get(0);
        }
        em.persist(inventario);
        em.flush();
        movimientoStock.setInventario(inventario);
        movimientoStockRepository.saveAndFlush(movimientoStock);
        Long inventarioId = inventario.getId();
        // Get all the movimientoStockList where inventario equals to inventarioId
        defaultMovimientoStockShouldBeFound("inventarioId.equals=" + inventarioId);

        // Get all the movimientoStockList where inventario equals to (inventarioId + 1)
        defaultMovimientoStockShouldNotBeFound("inventarioId.equals=" + (inventarioId + 1));
    }

    private void defaultMovimientoStockFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMovimientoStockShouldBeFound(shouldBeFound);
        defaultMovimientoStockShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMovimientoStockShouldBeFound(String filter) throws Exception {
        restMovimientoStockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movimientoStock.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].referencia").value(hasItem(DEFAULT_REFERENCIA)));

        // Check, that the count call also returns 1
        restMovimientoStockMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMovimientoStockShouldNotBeFound(String filter) throws Exception {
        restMovimientoStockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMovimientoStockMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMovimientoStock() throws Exception {
        // Get the movimientoStock
        restMovimientoStockMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMovimientoStock() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the movimientoStock
        MovimientoStock updatedMovimientoStock = movimientoStockRepository.findById(movimientoStock.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMovimientoStock are not directly saved in db
        em.detach(updatedMovimientoStock);
        updatedMovimientoStock.fecha(UPDATED_FECHA).tipo(UPDATED_TIPO).cantidad(UPDATED_CANTIDAD).referencia(UPDATED_REFERENCIA);

        restMovimientoStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMovimientoStock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMovimientoStock))
            )
            .andExpect(status().isOk());

        // Validate the MovimientoStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMovimientoStockToMatchAllProperties(updatedMovimientoStock);
    }

    @Test
    @Transactional
    void putNonExistingMovimientoStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movimientoStock.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovimientoStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movimientoStock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(movimientoStock))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovimientoStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMovimientoStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movimientoStock.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovimientoStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(movimientoStock))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovimientoStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMovimientoStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movimientoStock.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovimientoStockMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(movimientoStock)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MovimientoStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMovimientoStockWithPatch() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the movimientoStock using partial update
        MovimientoStock partialUpdatedMovimientoStock = new MovimientoStock();
        partialUpdatedMovimientoStock.setId(movimientoStock.getId());

        partialUpdatedMovimientoStock.tipo(UPDATED_TIPO);

        restMovimientoStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovimientoStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMovimientoStock))
            )
            .andExpect(status().isOk());

        // Validate the MovimientoStock in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMovimientoStockUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMovimientoStock, movimientoStock),
            getPersistedMovimientoStock(movimientoStock)
        );
    }

    @Test
    @Transactional
    void fullUpdateMovimientoStockWithPatch() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the movimientoStock using partial update
        MovimientoStock partialUpdatedMovimientoStock = new MovimientoStock();
        partialUpdatedMovimientoStock.setId(movimientoStock.getId());

        partialUpdatedMovimientoStock.fecha(UPDATED_FECHA).tipo(UPDATED_TIPO).cantidad(UPDATED_CANTIDAD).referencia(UPDATED_REFERENCIA);

        restMovimientoStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovimientoStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMovimientoStock))
            )
            .andExpect(status().isOk());

        // Validate the MovimientoStock in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMovimientoStockUpdatableFieldsEquals(
            partialUpdatedMovimientoStock,
            getPersistedMovimientoStock(partialUpdatedMovimientoStock)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMovimientoStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movimientoStock.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovimientoStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, movimientoStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(movimientoStock))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovimientoStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMovimientoStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movimientoStock.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovimientoStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(movimientoStock))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovimientoStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMovimientoStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movimientoStock.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovimientoStockMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(movimientoStock)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MovimientoStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMovimientoStock() throws Exception {
        // Initialize the database
        insertedMovimientoStock = movimientoStockRepository.saveAndFlush(movimientoStock);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the movimientoStock
        restMovimientoStockMockMvc
            .perform(delete(ENTITY_API_URL_ID, movimientoStock.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return movimientoStockRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected MovimientoStock getPersistedMovimientoStock(MovimientoStock movimientoStock) {
        return movimientoStockRepository.findById(movimientoStock.getId()).orElseThrow();
    }

    protected void assertPersistedMovimientoStockToMatchAllProperties(MovimientoStock expectedMovimientoStock) {
        assertMovimientoStockAllPropertiesEquals(expectedMovimientoStock, getPersistedMovimientoStock(expectedMovimientoStock));
    }

    protected void assertPersistedMovimientoStockToMatchUpdatableProperties(MovimientoStock expectedMovimientoStock) {
        assertMovimientoStockAllUpdatablePropertiesEquals(expectedMovimientoStock, getPersistedMovimientoStock(expectedMovimientoStock));
    }
}
