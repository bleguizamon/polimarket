package co.edu.poligran.polimarket.web.rest;

import static co.edu.poligran.polimarket.domain.EntregaAsserts.*;
import static co.edu.poligran.polimarket.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.poligran.polimarket.IntegrationTest;
import co.edu.poligran.polimarket.domain.Cliente;
import co.edu.poligran.polimarket.domain.Entrega;
import co.edu.poligran.polimarket.domain.Producto;
import co.edu.poligran.polimarket.domain.Tarea;
import co.edu.poligran.polimarket.repository.EntregaRepository;
import co.edu.poligran.polimarket.service.EntregaService;
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
 * Integration tests for the {@link EntregaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EntregaResourceIT {

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/entregas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntregaRepository entregaRepository;

    @Mock
    private EntregaRepository entregaRepositoryMock;

    @Mock
    private EntregaService entregaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEntregaMockMvc;

    private Entrega entrega;

    private Entrega insertedEntrega;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entrega createEntity() {
        return new Entrega().fecha(DEFAULT_FECHA).estado(DEFAULT_ESTADO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entrega createUpdatedEntity() {
        return new Entrega().fecha(UPDATED_FECHA).estado(UPDATED_ESTADO);
    }

    @BeforeEach
    void initTest() {
        entrega = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEntrega != null) {
            entregaRepository.delete(insertedEntrega);
            insertedEntrega = null;
        }
    }

    @Test
    @Transactional
    void createEntrega() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Entrega
        var returnedEntrega = om.readValue(
            restEntregaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entrega)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Entrega.class
        );

        // Validate the Entrega in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEntregaUpdatableFieldsEquals(returnedEntrega, getPersistedEntrega(returnedEntrega));

        insertedEntrega = returnedEntrega;
    }

    @Test
    @Transactional
    void createEntregaWithExistingId() throws Exception {
        // Create the Entrega with an existing ID
        entrega.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntregaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entrega)))
            .andExpect(status().isBadRequest());

        // Validate the Entrega in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEntregas() throws Exception {
        // Initialize the database
        insertedEntrega = entregaRepository.saveAndFlush(entrega);

        // Get all the entregaList
        restEntregaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entrega.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEntregasWithEagerRelationshipsIsEnabled() throws Exception {
        when(entregaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEntregaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(entregaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEntregasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(entregaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEntregaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(entregaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEntrega() throws Exception {
        // Initialize the database
        insertedEntrega = entregaRepository.saveAndFlush(entrega);

        // Get the entrega
        restEntregaMockMvc
            .perform(get(ENTITY_API_URL_ID, entrega.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entrega.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }

    @Test
    @Transactional
    void getEntregasByIdFiltering() throws Exception {
        // Initialize the database
        insertedEntrega = entregaRepository.saveAndFlush(entrega);

        Long id = entrega.getId();

        defaultEntregaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEntregaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEntregaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEntregasByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEntrega = entregaRepository.saveAndFlush(entrega);

        // Get all the entregaList where fecha equals to
        defaultEntregaFiltering("fecha.equals=" + DEFAULT_FECHA, "fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllEntregasByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEntrega = entregaRepository.saveAndFlush(entrega);

        // Get all the entregaList where fecha in
        defaultEntregaFiltering("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA, "fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllEntregasByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEntrega = entregaRepository.saveAndFlush(entrega);

        // Get all the entregaList where fecha is not null
        defaultEntregaFiltering("fecha.specified=true", "fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllEntregasByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEntrega = entregaRepository.saveAndFlush(entrega);

        // Get all the entregaList where estado equals to
        defaultEntregaFiltering("estado.equals=" + DEFAULT_ESTADO, "estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllEntregasByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEntrega = entregaRepository.saveAndFlush(entrega);

        // Get all the entregaList where estado in
        defaultEntregaFiltering("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO, "estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllEntregasByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEntrega = entregaRepository.saveAndFlush(entrega);

        // Get all the entregaList where estado is not null
        defaultEntregaFiltering("estado.specified=true", "estado.specified=false");
    }

    @Test
    @Transactional
    void getAllEntregasByEstadoContainsSomething() throws Exception {
        // Initialize the database
        insertedEntrega = entregaRepository.saveAndFlush(entrega);

        // Get all the entregaList where estado contains
        defaultEntregaFiltering("estado.contains=" + DEFAULT_ESTADO, "estado.contains=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllEntregasByEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEntrega = entregaRepository.saveAndFlush(entrega);

        // Get all the entregaList where estado does not contain
        defaultEntregaFiltering("estado.doesNotContain=" + UPDATED_ESTADO, "estado.doesNotContain=" + DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void getAllEntregasByTareaIsEqualToSomething() throws Exception {
        Tarea tarea;
        if (TestUtil.findAll(em, Tarea.class).isEmpty()) {
            entregaRepository.saveAndFlush(entrega);
            tarea = TareaResourceIT.createEntity();
        } else {
            tarea = TestUtil.findAll(em, Tarea.class).get(0);
        }
        em.persist(tarea);
        em.flush();
        entrega.setTarea(tarea);
        entregaRepository.saveAndFlush(entrega);
        Long tareaId = tarea.getId();
        // Get all the entregaList where tarea equals to tareaId
        defaultEntregaShouldBeFound("tareaId.equals=" + tareaId);

        // Get all the entregaList where tarea equals to (tareaId + 1)
        defaultEntregaShouldNotBeFound("tareaId.equals=" + (tareaId + 1));
    }

    @Test
    @Transactional
    void getAllEntregasByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            entregaRepository.saveAndFlush(entrega);
            cliente = ClienteResourceIT.createEntity();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        entrega.setCliente(cliente);
        entregaRepository.saveAndFlush(entrega);
        Long clienteId = cliente.getId();
        // Get all the entregaList where cliente equals to clienteId
        defaultEntregaShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the entregaList where cliente equals to (clienteId + 1)
        defaultEntregaShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    @Test
    @Transactional
    void getAllEntregasByProductoIsEqualToSomething() throws Exception {
        Producto producto;
        if (TestUtil.findAll(em, Producto.class).isEmpty()) {
            entregaRepository.saveAndFlush(entrega);
            producto = ProductoResourceIT.createEntity();
        } else {
            producto = TestUtil.findAll(em, Producto.class).get(0);
        }
        em.persist(producto);
        em.flush();
        entrega.setProducto(producto);
        entregaRepository.saveAndFlush(entrega);
        Long productoId = producto.getId();
        // Get all the entregaList where producto equals to productoId
        defaultEntregaShouldBeFound("productoId.equals=" + productoId);

        // Get all the entregaList where producto equals to (productoId + 1)
        defaultEntregaShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    private void defaultEntregaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEntregaShouldBeFound(shouldBeFound);
        defaultEntregaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEntregaShouldBeFound(String filter) throws Exception {
        restEntregaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entrega.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));

        // Check, that the count call also returns 1
        restEntregaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEntregaShouldNotBeFound(String filter) throws Exception {
        restEntregaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEntregaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEntrega() throws Exception {
        // Get the entrega
        restEntregaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEntrega() throws Exception {
        // Initialize the database
        insertedEntrega = entregaRepository.saveAndFlush(entrega);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entrega
        Entrega updatedEntrega = entregaRepository.findById(entrega.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEntrega are not directly saved in db
        em.detach(updatedEntrega);
        updatedEntrega.fecha(UPDATED_FECHA).estado(UPDATED_ESTADO);

        restEntregaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEntrega.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEntrega))
            )
            .andExpect(status().isOk());

        // Validate the Entrega in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEntregaToMatchAllProperties(updatedEntrega);
    }

    @Test
    @Transactional
    void putNonExistingEntrega() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entrega.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntregaMockMvc
            .perform(put(ENTITY_API_URL_ID, entrega.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entrega)))
            .andExpect(status().isBadRequest());

        // Validate the Entrega in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEntrega() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entrega.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntregaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(entrega))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entrega in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEntrega() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entrega.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntregaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entrega)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Entrega in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEntregaWithPatch() throws Exception {
        // Initialize the database
        insertedEntrega = entregaRepository.saveAndFlush(entrega);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entrega using partial update
        Entrega partialUpdatedEntrega = new Entrega();
        partialUpdatedEntrega.setId(entrega.getId());

        restEntregaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntrega.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEntrega))
            )
            .andExpect(status().isOk());

        // Validate the Entrega in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntregaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEntrega, entrega), getPersistedEntrega(entrega));
    }

    @Test
    @Transactional
    void fullUpdateEntregaWithPatch() throws Exception {
        // Initialize the database
        insertedEntrega = entregaRepository.saveAndFlush(entrega);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entrega using partial update
        Entrega partialUpdatedEntrega = new Entrega();
        partialUpdatedEntrega.setId(entrega.getId());

        partialUpdatedEntrega.fecha(UPDATED_FECHA).estado(UPDATED_ESTADO);

        restEntregaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntrega.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEntrega))
            )
            .andExpect(status().isOk());

        // Validate the Entrega in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntregaUpdatableFieldsEquals(partialUpdatedEntrega, getPersistedEntrega(partialUpdatedEntrega));
    }

    @Test
    @Transactional
    void patchNonExistingEntrega() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entrega.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntregaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, entrega.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(entrega))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entrega in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEntrega() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entrega.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntregaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(entrega))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entrega in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEntrega() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entrega.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntregaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(entrega)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Entrega in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEntrega() throws Exception {
        // Initialize the database
        insertedEntrega = entregaRepository.saveAndFlush(entrega);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the entrega
        restEntregaMockMvc
            .perform(delete(ENTITY_API_URL_ID, entrega.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return entregaRepository.count();
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

    protected Entrega getPersistedEntrega(Entrega entrega) {
        return entregaRepository.findById(entrega.getId()).orElseThrow();
    }

    protected void assertPersistedEntregaToMatchAllProperties(Entrega expectedEntrega) {
        assertEntregaAllPropertiesEquals(expectedEntrega, getPersistedEntrega(expectedEntrega));
    }

    protected void assertPersistedEntregaToMatchUpdatableProperties(Entrega expectedEntrega) {
        assertEntregaAllUpdatablePropertiesEquals(expectedEntrega, getPersistedEntrega(expectedEntrega));
    }
}
