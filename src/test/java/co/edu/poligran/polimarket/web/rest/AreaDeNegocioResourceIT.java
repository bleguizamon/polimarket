package co.edu.poligran.polimarket.web.rest;

import static co.edu.poligran.polimarket.domain.AreaDeNegocioAsserts.*;
import static co.edu.poligran.polimarket.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.poligran.polimarket.IntegrationTest;
import co.edu.poligran.polimarket.domain.AreaDeNegocio;
import co.edu.poligran.polimarket.repository.AreaDeNegocioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AreaDeNegocioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AreaDeNegocioResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/area-de-negocios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AreaDeNegocioRepository areaDeNegocioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAreaDeNegocioMockMvc;

    private AreaDeNegocio areaDeNegocio;

    private AreaDeNegocio insertedAreaDeNegocio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AreaDeNegocio createEntity() {
        return new AreaDeNegocio().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AreaDeNegocio createUpdatedEntity() {
        return new AreaDeNegocio().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
    }

    @BeforeEach
    void initTest() {
        areaDeNegocio = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAreaDeNegocio != null) {
            areaDeNegocioRepository.delete(insertedAreaDeNegocio);
            insertedAreaDeNegocio = null;
        }
    }

    @Test
    @Transactional
    void createAreaDeNegocio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AreaDeNegocio
        var returnedAreaDeNegocio = om.readValue(
            restAreaDeNegocioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(areaDeNegocio)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AreaDeNegocio.class
        );

        // Validate the AreaDeNegocio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAreaDeNegocioUpdatableFieldsEquals(returnedAreaDeNegocio, getPersistedAreaDeNegocio(returnedAreaDeNegocio));

        insertedAreaDeNegocio = returnedAreaDeNegocio;
    }

    @Test
    @Transactional
    void createAreaDeNegocioWithExistingId() throws Exception {
        // Create the AreaDeNegocio with an existing ID
        areaDeNegocio.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAreaDeNegocioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(areaDeNegocio)))
            .andExpect(status().isBadRequest());

        // Validate the AreaDeNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        areaDeNegocio.setNombre(null);

        // Create the AreaDeNegocio, which fails.

        restAreaDeNegocioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(areaDeNegocio)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAreaDeNegocios() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        // Get all the areaDeNegocioList
        restAreaDeNegocioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(areaDeNegocio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getAreaDeNegocio() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        // Get the areaDeNegocio
        restAreaDeNegocioMockMvc
            .perform(get(ENTITY_API_URL_ID, areaDeNegocio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(areaDeNegocio.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getAreaDeNegociosByIdFiltering() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        Long id = areaDeNegocio.getId();

        defaultAreaDeNegocioFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAreaDeNegocioFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAreaDeNegocioFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAreaDeNegociosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        // Get all the areaDeNegocioList where nombre equals to
        defaultAreaDeNegocioFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllAreaDeNegociosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        // Get all the areaDeNegocioList where nombre in
        defaultAreaDeNegocioFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllAreaDeNegociosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        // Get all the areaDeNegocioList where nombre is not null
        defaultAreaDeNegocioFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllAreaDeNegociosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        // Get all the areaDeNegocioList where nombre contains
        defaultAreaDeNegocioFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllAreaDeNegociosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        // Get all the areaDeNegocioList where nombre does not contain
        defaultAreaDeNegocioFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllAreaDeNegociosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        // Get all the areaDeNegocioList where descripcion equals to
        defaultAreaDeNegocioFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllAreaDeNegociosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        // Get all the areaDeNegocioList where descripcion in
        defaultAreaDeNegocioFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllAreaDeNegociosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        // Get all the areaDeNegocioList where descripcion is not null
        defaultAreaDeNegocioFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllAreaDeNegociosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        // Get all the areaDeNegocioList where descripcion contains
        defaultAreaDeNegocioFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllAreaDeNegociosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        // Get all the areaDeNegocioList where descripcion does not contain
        defaultAreaDeNegocioFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    private void defaultAreaDeNegocioFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAreaDeNegocioShouldBeFound(shouldBeFound);
        defaultAreaDeNegocioShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAreaDeNegocioShouldBeFound(String filter) throws Exception {
        restAreaDeNegocioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(areaDeNegocio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restAreaDeNegocioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAreaDeNegocioShouldNotBeFound(String filter) throws Exception {
        restAreaDeNegocioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAreaDeNegocioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAreaDeNegocio() throws Exception {
        // Get the areaDeNegocio
        restAreaDeNegocioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAreaDeNegocio() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the areaDeNegocio
        AreaDeNegocio updatedAreaDeNegocio = areaDeNegocioRepository.findById(areaDeNegocio.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAreaDeNegocio are not directly saved in db
        em.detach(updatedAreaDeNegocio);
        updatedAreaDeNegocio.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restAreaDeNegocioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAreaDeNegocio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAreaDeNegocio))
            )
            .andExpect(status().isOk());

        // Validate the AreaDeNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAreaDeNegocioToMatchAllProperties(updatedAreaDeNegocio);
    }

    @Test
    @Transactional
    void putNonExistingAreaDeNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        areaDeNegocio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAreaDeNegocioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, areaDeNegocio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(areaDeNegocio))
            )
            .andExpect(status().isBadRequest());

        // Validate the AreaDeNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAreaDeNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        areaDeNegocio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAreaDeNegocioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(areaDeNegocio))
            )
            .andExpect(status().isBadRequest());

        // Validate the AreaDeNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAreaDeNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        areaDeNegocio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAreaDeNegocioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(areaDeNegocio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AreaDeNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAreaDeNegocioWithPatch() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the areaDeNegocio using partial update
        AreaDeNegocio partialUpdatedAreaDeNegocio = new AreaDeNegocio();
        partialUpdatedAreaDeNegocio.setId(areaDeNegocio.getId());

        partialUpdatedAreaDeNegocio.nombre(UPDATED_NOMBRE);

        restAreaDeNegocioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAreaDeNegocio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAreaDeNegocio))
            )
            .andExpect(status().isOk());

        // Validate the AreaDeNegocio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAreaDeNegocioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAreaDeNegocio, areaDeNegocio),
            getPersistedAreaDeNegocio(areaDeNegocio)
        );
    }

    @Test
    @Transactional
    void fullUpdateAreaDeNegocioWithPatch() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the areaDeNegocio using partial update
        AreaDeNegocio partialUpdatedAreaDeNegocio = new AreaDeNegocio();
        partialUpdatedAreaDeNegocio.setId(areaDeNegocio.getId());

        partialUpdatedAreaDeNegocio.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restAreaDeNegocioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAreaDeNegocio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAreaDeNegocio))
            )
            .andExpect(status().isOk());

        // Validate the AreaDeNegocio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAreaDeNegocioUpdatableFieldsEquals(partialUpdatedAreaDeNegocio, getPersistedAreaDeNegocio(partialUpdatedAreaDeNegocio));
    }

    @Test
    @Transactional
    void patchNonExistingAreaDeNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        areaDeNegocio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAreaDeNegocioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, areaDeNegocio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(areaDeNegocio))
            )
            .andExpect(status().isBadRequest());

        // Validate the AreaDeNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAreaDeNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        areaDeNegocio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAreaDeNegocioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(areaDeNegocio))
            )
            .andExpect(status().isBadRequest());

        // Validate the AreaDeNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAreaDeNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        areaDeNegocio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAreaDeNegocioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(areaDeNegocio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AreaDeNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAreaDeNegocio() throws Exception {
        // Initialize the database
        insertedAreaDeNegocio = areaDeNegocioRepository.saveAndFlush(areaDeNegocio);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the areaDeNegocio
        restAreaDeNegocioMockMvc
            .perform(delete(ENTITY_API_URL_ID, areaDeNegocio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return areaDeNegocioRepository.count();
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

    protected AreaDeNegocio getPersistedAreaDeNegocio(AreaDeNegocio areaDeNegocio) {
        return areaDeNegocioRepository.findById(areaDeNegocio.getId()).orElseThrow();
    }

    protected void assertPersistedAreaDeNegocioToMatchAllProperties(AreaDeNegocio expectedAreaDeNegocio) {
        assertAreaDeNegocioAllPropertiesEquals(expectedAreaDeNegocio, getPersistedAreaDeNegocio(expectedAreaDeNegocio));
    }

    protected void assertPersistedAreaDeNegocioToMatchUpdatableProperties(AreaDeNegocio expectedAreaDeNegocio) {
        assertAreaDeNegocioAllUpdatablePropertiesEquals(expectedAreaDeNegocio, getPersistedAreaDeNegocio(expectedAreaDeNegocio));
    }
}
