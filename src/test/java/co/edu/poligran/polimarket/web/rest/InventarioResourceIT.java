package co.edu.poligran.polimarket.web.rest;

import static co.edu.poligran.polimarket.domain.InventarioAsserts.*;
import static co.edu.poligran.polimarket.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.poligran.polimarket.IntegrationTest;
import co.edu.poligran.polimarket.domain.Inventario;
import co.edu.poligran.polimarket.domain.Producto;
import co.edu.poligran.polimarket.repository.InventarioRepository;
import co.edu.poligran.polimarket.service.InventarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link InventarioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InventarioResourceIT {

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;
    private static final Integer SMALLER_CANTIDAD = 1 - 1;

    private static final String DEFAULT_UBICACION = "AAAAAAAAAA";
    private static final String UPDATED_UBICACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inventarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InventarioRepository inventarioRepository;

    @Mock
    private InventarioRepository inventarioRepositoryMock;

    @Mock
    private InventarioService inventarioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventarioMockMvc;

    private Inventario inventario;

    private Inventario insertedInventario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventario createEntity() {
        return new Inventario().cantidad(DEFAULT_CANTIDAD).ubicacion(DEFAULT_UBICACION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventario createUpdatedEntity() {
        return new Inventario().cantidad(UPDATED_CANTIDAD).ubicacion(UPDATED_UBICACION);
    }

    @BeforeEach
    void initTest() {
        inventario = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInventario != null) {
            inventarioRepository.delete(insertedInventario);
            insertedInventario = null;
        }
    }

    @Test
    @Transactional
    void createInventario() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Inventario
        var returnedInventario = om.readValue(
            restInventarioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventario)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Inventario.class
        );

        // Validate the Inventario in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInventarioUpdatableFieldsEquals(returnedInventario, getPersistedInventario(returnedInventario));

        insertedInventario = returnedInventario;
    }

    @Test
    @Transactional
    void createInventarioWithExistingId() throws Exception {
        // Create the Inventario with an existing ID
        inventario.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventario)))
            .andExpect(status().isBadRequest());

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInventarios() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        // Get all the inventarioList
        restInventarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventario.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInventariosWithEagerRelationshipsIsEnabled() throws Exception {
        when(inventarioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInventarioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inventarioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInventariosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(inventarioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInventarioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(inventarioRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInventario() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        // Get the inventario
        restInventarioMockMvc
            .perform(get(ENTITY_API_URL_ID, inventario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventario.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.ubicacion").value(DEFAULT_UBICACION));
    }

    @Test
    @Transactional
    void getInventariosByIdFiltering() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        Long id = inventario.getId();

        defaultInventarioFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInventarioFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInventarioFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInventariosByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        // Get all the inventarioList where cantidad equals to
        defaultInventarioFiltering("cantidad.equals=" + DEFAULT_CANTIDAD, "cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllInventariosByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        // Get all the inventarioList where cantidad in
        defaultInventarioFiltering("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD, "cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllInventariosByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        // Get all the inventarioList where cantidad is not null
        defaultInventarioFiltering("cantidad.specified=true", "cantidad.specified=false");
    }

    @Test
    @Transactional
    void getAllInventariosByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        // Get all the inventarioList where cantidad is greater than or equal to
        defaultInventarioFiltering("cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD, "cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllInventariosByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        // Get all the inventarioList where cantidad is less than or equal to
        defaultInventarioFiltering("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD, "cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllInventariosByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        // Get all the inventarioList where cantidad is less than
        defaultInventarioFiltering("cantidad.lessThan=" + UPDATED_CANTIDAD, "cantidad.lessThan=" + DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllInventariosByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        // Get all the inventarioList where cantidad is greater than
        defaultInventarioFiltering("cantidad.greaterThan=" + SMALLER_CANTIDAD, "cantidad.greaterThan=" + DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllInventariosByUbicacionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        // Get all the inventarioList where ubicacion equals to
        defaultInventarioFiltering("ubicacion.equals=" + DEFAULT_UBICACION, "ubicacion.equals=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void getAllInventariosByUbicacionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        // Get all the inventarioList where ubicacion in
        defaultInventarioFiltering("ubicacion.in=" + DEFAULT_UBICACION + "," + UPDATED_UBICACION, "ubicacion.in=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void getAllInventariosByUbicacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        // Get all the inventarioList where ubicacion is not null
        defaultInventarioFiltering("ubicacion.specified=true", "ubicacion.specified=false");
    }

    @Test
    @Transactional
    void getAllInventariosByUbicacionContainsSomething() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        // Get all the inventarioList where ubicacion contains
        defaultInventarioFiltering("ubicacion.contains=" + DEFAULT_UBICACION, "ubicacion.contains=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void getAllInventariosByUbicacionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        // Get all the inventarioList where ubicacion does not contain
        defaultInventarioFiltering("ubicacion.doesNotContain=" + UPDATED_UBICACION, "ubicacion.doesNotContain=" + DEFAULT_UBICACION);
    }

    @Test
    @Transactional
    void getAllInventariosByProductoIsEqualToSomething() throws Exception {
        Producto producto;
        if (TestUtil.findAll(em, Producto.class).isEmpty()) {
            inventarioRepository.saveAndFlush(inventario);
            producto = ProductoResourceIT.createEntity();
        } else {
            producto = TestUtil.findAll(em, Producto.class).get(0);
        }
        em.persist(producto);
        em.flush();
        inventario.setProducto(producto);
        inventarioRepository.saveAndFlush(inventario);
        Long productoId = producto.getId();
        // Get all the inventarioList where producto equals to productoId
        defaultInventarioShouldBeFound("productoId.equals=" + productoId);

        // Get all the inventarioList where producto equals to (productoId + 1)
        defaultInventarioShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    private void defaultInventarioFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInventarioShouldBeFound(shouldBeFound);
        defaultInventarioShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInventarioShouldBeFound(String filter) throws Exception {
        restInventarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventario.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION)));

        // Check, that the count call also returns 1
        restInventarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInventarioShouldNotBeFound(String filter) throws Exception {
        restInventarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInventarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInventario() throws Exception {
        // Get the inventario
        restInventarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInventario() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventario
        Inventario updatedInventario = inventarioRepository.findById(inventario.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInventario are not directly saved in db
        em.detach(updatedInventario);
        updatedInventario.cantidad(UPDATED_CANTIDAD).ubicacion(UPDATED_UBICACION);

        restInventarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInventario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInventario))
            )
            .andExpect(status().isOk());

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInventarioToMatchAllProperties(updatedInventario);
    }

    @Test
    @Transactional
    void putNonExistingInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventario.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventarioWithPatch() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventario using partial update
        Inventario partialUpdatedInventario = new Inventario();
        partialUpdatedInventario.setId(inventario.getId());

        partialUpdatedInventario.cantidad(UPDATED_CANTIDAD);

        restInventarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventario))
            )
            .andExpect(status().isOk());

        // Validate the Inventario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventarioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInventario, inventario),
            getPersistedInventario(inventario)
        );
    }

    @Test
    @Transactional
    void fullUpdateInventarioWithPatch() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventario using partial update
        Inventario partialUpdatedInventario = new Inventario();
        partialUpdatedInventario.setId(inventario.getId());

        partialUpdatedInventario.cantidad(UPDATED_CANTIDAD).ubicacion(UPDATED_UBICACION);

        restInventarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventario))
            )
            .andExpect(status().isOk());

        // Validate the Inventario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventarioUpdatableFieldsEquals(partialUpdatedInventario, getPersistedInventario(partialUpdatedInventario));
    }

    @Test
    @Transactional
    void patchNonExistingInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventarioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inventario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventario() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inventario
        restInventarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inventarioRepository.count();
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

    protected Inventario getPersistedInventario(Inventario inventario) {
        return inventarioRepository.findById(inventario.getId()).orElseThrow();
    }

    protected void assertPersistedInventarioToMatchAllProperties(Inventario expectedInventario) {
        assertInventarioAllPropertiesEquals(expectedInventario, getPersistedInventario(expectedInventario));
    }

    protected void assertPersistedInventarioToMatchUpdatableProperties(Inventario expectedInventario) {
        assertInventarioAllUpdatablePropertiesEquals(expectedInventario, getPersistedInventario(expectedInventario));
    }
}
