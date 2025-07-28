package co.edu.poligran.polimarket.web.rest;

import static co.edu.poligran.polimarket.domain.TareaAsserts.*;
import static co.edu.poligran.polimarket.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.poligran.polimarket.IntegrationTest;
import co.edu.poligran.polimarket.domain.Tarea;
import co.edu.poligran.polimarket.domain.Vendedor;
import co.edu.poligran.polimarket.repository.TareaRepository;
import co.edu.poligran.polimarket.service.TareaService;
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
 * Integration tests for the {@link TareaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TareaResourceIT {

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tareas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TareaRepository tareaRepository;

    @Mock
    private TareaRepository tareaRepositoryMock;

    @Mock
    private TareaService tareaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTareaMockMvc;

    private Tarea tarea;

    private Tarea insertedTarea;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarea createEntity() {
        return new Tarea().descripcion(DEFAULT_DESCRIPCION).estado(DEFAULT_ESTADO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarea createUpdatedEntity() {
        return new Tarea().descripcion(UPDATED_DESCRIPCION).estado(UPDATED_ESTADO);
    }

    @BeforeEach
    void initTest() {
        tarea = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTarea != null) {
            tareaRepository.delete(insertedTarea);
            insertedTarea = null;
        }
    }

    @Test
    @Transactional
    void createTarea() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Tarea
        var returnedTarea = om.readValue(
            restTareaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarea)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Tarea.class
        );

        // Validate the Tarea in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTareaUpdatableFieldsEquals(returnedTarea, getPersistedTarea(returnedTarea));

        insertedTarea = returnedTarea;
    }

    @Test
    @Transactional
    void createTareaWithExistingId() throws Exception {
        // Create the Tarea with an existing ID
        tarea.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTareaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarea)))
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTareas() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        // Get all the tareaList
        restTareaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarea.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTareasWithEagerRelationshipsIsEnabled() throws Exception {
        when(tareaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTareaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tareaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTareasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tareaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTareaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(tareaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTarea() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        // Get the tarea
        restTareaMockMvc
            .perform(get(ENTITY_API_URL_ID, tarea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tarea.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }

    @Test
    @Transactional
    void getTareasByIdFiltering() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        Long id = tarea.getId();

        defaultTareaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTareaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTareaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTareasByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        // Get all the tareaList where descripcion equals to
        defaultTareaFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTareasByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        // Get all the tareaList where descripcion in
        defaultTareaFiltering("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION, "descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTareasByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        // Get all the tareaList where descripcion is not null
        defaultTareaFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllTareasByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        // Get all the tareaList where descripcion contains
        defaultTareaFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTareasByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        // Get all the tareaList where descripcion does not contain
        defaultTareaFiltering("descripcion.doesNotContain=" + UPDATED_DESCRIPCION, "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTareasByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        // Get all the tareaList where estado equals to
        defaultTareaFiltering("estado.equals=" + DEFAULT_ESTADO, "estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllTareasByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        // Get all the tareaList where estado in
        defaultTareaFiltering("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO, "estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllTareasByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        // Get all the tareaList where estado is not null
        defaultTareaFiltering("estado.specified=true", "estado.specified=false");
    }

    @Test
    @Transactional
    void getAllTareasByEstadoContainsSomething() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        // Get all the tareaList where estado contains
        defaultTareaFiltering("estado.contains=" + DEFAULT_ESTADO, "estado.contains=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllTareasByEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        // Get all the tareaList where estado does not contain
        defaultTareaFiltering("estado.doesNotContain=" + UPDATED_ESTADO, "estado.doesNotContain=" + DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void getAllTareasByVendedorIsEqualToSomething() throws Exception {
        Vendedor vendedor;
        if (TestUtil.findAll(em, Vendedor.class).isEmpty()) {
            tareaRepository.saveAndFlush(tarea);
            vendedor = VendedorResourceIT.createEntity();
        } else {
            vendedor = TestUtil.findAll(em, Vendedor.class).get(0);
        }
        em.persist(vendedor);
        em.flush();
        tarea.setVendedor(vendedor);
        tareaRepository.saveAndFlush(tarea);
        Long vendedorId = vendedor.getId();
        // Get all the tareaList where vendedor equals to vendedorId
        defaultTareaShouldBeFound("vendedorId.equals=" + vendedorId);

        // Get all the tareaList where vendedor equals to (vendedorId + 1)
        defaultTareaShouldNotBeFound("vendedorId.equals=" + (vendedorId + 1));
    }

    private void defaultTareaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTareaShouldBeFound(shouldBeFound);
        defaultTareaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTareaShouldBeFound(String filter) throws Exception {
        restTareaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarea.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));

        // Check, that the count call also returns 1
        restTareaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTareaShouldNotBeFound(String filter) throws Exception {
        restTareaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTareaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTarea() throws Exception {
        // Get the tarea
        restTareaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTarea() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarea
        Tarea updatedTarea = tareaRepository.findById(tarea.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTarea are not directly saved in db
        em.detach(updatedTarea);
        updatedTarea.descripcion(UPDATED_DESCRIPCION).estado(UPDATED_ESTADO);

        restTareaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTarea.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTarea))
            )
            .andExpect(status().isOk());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTareaToMatchAllProperties(updatedTarea);
    }

    @Test
    @Transactional
    void putNonExistingTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(put(ENTITY_API_URL_ID, tarea.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarea)))
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tarea))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarea)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTareaWithPatch() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarea using partial update
        Tarea partialUpdatedTarea = new Tarea();
        partialUpdatedTarea.setId(tarea.getId());

        restTareaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarea.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTarea))
            )
            .andExpect(status().isOk());

        // Validate the Tarea in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTareaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTarea, tarea), getPersistedTarea(tarea));
    }

    @Test
    @Transactional
    void fullUpdateTareaWithPatch() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarea using partial update
        Tarea partialUpdatedTarea = new Tarea();
        partialUpdatedTarea.setId(tarea.getId());

        partialUpdatedTarea.descripcion(UPDATED_DESCRIPCION).estado(UPDATED_ESTADO);

        restTareaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarea.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTarea))
            )
            .andExpect(status().isOk());

        // Validate the Tarea in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTareaUpdatableFieldsEquals(partialUpdatedTarea, getPersistedTarea(partialUpdatedTarea));
    }

    @Test
    @Transactional
    void patchNonExistingTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tarea.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tarea))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tarea))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tarea)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTarea() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tarea
        restTareaMockMvc
            .perform(delete(ENTITY_API_URL_ID, tarea.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tareaRepository.count();
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

    protected Tarea getPersistedTarea(Tarea tarea) {
        return tareaRepository.findById(tarea.getId()).orElseThrow();
    }

    protected void assertPersistedTareaToMatchAllProperties(Tarea expectedTarea) {
        assertTareaAllPropertiesEquals(expectedTarea, getPersistedTarea(expectedTarea));
    }

    protected void assertPersistedTareaToMatchUpdatableProperties(Tarea expectedTarea) {
        assertTareaAllUpdatablePropertiesEquals(expectedTarea, getPersistedTarea(expectedTarea));
    }
}
