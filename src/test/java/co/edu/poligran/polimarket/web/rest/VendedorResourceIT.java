package co.edu.poligran.polimarket.web.rest;

import static co.edu.poligran.polimarket.domain.VendedorAsserts.*;
import static co.edu.poligran.polimarket.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.poligran.polimarket.IntegrationTest;
import co.edu.poligran.polimarket.domain.AreaDeNegocio;
import co.edu.poligran.polimarket.domain.Vendedor;
import co.edu.poligran.polimarket.repository.VendedorRepository;
import co.edu.poligran.polimarket.service.VendedorService;
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
 * Integration tests for the {@link VendedorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VendedorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_AUTORIZADO = false;
    private static final Boolean UPDATED_AUTORIZADO = true;

    private static final String ENTITY_API_URL = "/api/vendedors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VendedorRepository vendedorRepository;

    @Mock
    private VendedorRepository vendedorRepositoryMock;

    @Mock
    private VendedorService vendedorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVendedorMockMvc;

    private Vendedor vendedor;

    private Vendedor insertedVendedor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendedor createEntity() {
        return new Vendedor().nombre(DEFAULT_NOMBRE).autorizado(DEFAULT_AUTORIZADO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendedor createUpdatedEntity() {
        return new Vendedor().nombre(UPDATED_NOMBRE).autorizado(UPDATED_AUTORIZADO);
    }

    @BeforeEach
    void initTest() {
        vendedor = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedVendedor != null) {
            vendedorRepository.delete(insertedVendedor);
            insertedVendedor = null;
        }
    }

    @Test
    @Transactional
    void createVendedor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Vendedor
        var returnedVendedor = om.readValue(
            restVendedorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendedor)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Vendedor.class
        );

        // Validate the Vendedor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVendedorUpdatableFieldsEquals(returnedVendedor, getPersistedVendedor(returnedVendedor));

        insertedVendedor = returnedVendedor;
    }

    @Test
    @Transactional
    void createVendedorWithExistingId() throws Exception {
        // Create the Vendedor with an existing ID
        vendedor.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendedor)))
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vendedor.setNombre(null);

        // Create the Vendedor, which fails.

        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendedor)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVendedors() throws Exception {
        // Initialize the database
        insertedVendedor = vendedorRepository.saveAndFlush(vendedor);

        // Get all the vendedorList
        restVendedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].autorizado").value(hasItem(DEFAULT_AUTORIZADO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVendedorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(vendedorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVendedorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vendedorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVendedorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vendedorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVendedorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vendedorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVendedor() throws Exception {
        // Initialize the database
        insertedVendedor = vendedorRepository.saveAndFlush(vendedor);

        // Get the vendedor
        restVendedorMockMvc
            .perform(get(ENTITY_API_URL_ID, vendedor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vendedor.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.autorizado").value(DEFAULT_AUTORIZADO));
    }

    @Test
    @Transactional
    void getVendedorsByIdFiltering() throws Exception {
        // Initialize the database
        insertedVendedor = vendedorRepository.saveAndFlush(vendedor);

        Long id = vendedor.getId();

        defaultVendedorFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultVendedorFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultVendedorFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVendedorsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVendedor = vendedorRepository.saveAndFlush(vendedor);

        // Get all the vendedorList where nombre equals to
        defaultVendedorFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllVendedorsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVendedor = vendedorRepository.saveAndFlush(vendedor);

        // Get all the vendedorList where nombre in
        defaultVendedorFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllVendedorsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVendedor = vendedorRepository.saveAndFlush(vendedor);

        // Get all the vendedorList where nombre is not null
        defaultVendedorFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllVendedorsByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedVendedor = vendedorRepository.saveAndFlush(vendedor);

        // Get all the vendedorList where nombre contains
        defaultVendedorFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllVendedorsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVendedor = vendedorRepository.saveAndFlush(vendedor);

        // Get all the vendedorList where nombre does not contain
        defaultVendedorFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllVendedorsByAutorizadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVendedor = vendedorRepository.saveAndFlush(vendedor);

        // Get all the vendedorList where autorizado equals to
        defaultVendedorFiltering("autorizado.equals=" + DEFAULT_AUTORIZADO, "autorizado.equals=" + UPDATED_AUTORIZADO);
    }

    @Test
    @Transactional
    void getAllVendedorsByAutorizadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVendedor = vendedorRepository.saveAndFlush(vendedor);

        // Get all the vendedorList where autorizado in
        defaultVendedorFiltering("autorizado.in=" + DEFAULT_AUTORIZADO + "," + UPDATED_AUTORIZADO, "autorizado.in=" + UPDATED_AUTORIZADO);
    }

    @Test
    @Transactional
    void getAllVendedorsByAutorizadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVendedor = vendedorRepository.saveAndFlush(vendedor);

        // Get all the vendedorList where autorizado is not null
        defaultVendedorFiltering("autorizado.specified=true", "autorizado.specified=false");
    }

    @Test
    @Transactional
    void getAllVendedorsByAreaDeNegocioIsEqualToSomething() throws Exception {
        AreaDeNegocio areaDeNegocio;
        if (TestUtil.findAll(em, AreaDeNegocio.class).isEmpty()) {
            vendedorRepository.saveAndFlush(vendedor);
            areaDeNegocio = AreaDeNegocioResourceIT.createEntity();
        } else {
            areaDeNegocio = TestUtil.findAll(em, AreaDeNegocio.class).get(0);
        }
        em.persist(areaDeNegocio);
        em.flush();
        vendedor.setAreaDeNegocio(areaDeNegocio);
        vendedorRepository.saveAndFlush(vendedor);
        Long areaDeNegocioId = areaDeNegocio.getId();
        // Get all the vendedorList where areaDeNegocio equals to areaDeNegocioId
        defaultVendedorShouldBeFound("areaDeNegocioId.equals=" + areaDeNegocioId);

        // Get all the vendedorList where areaDeNegocio equals to (areaDeNegocioId + 1)
        defaultVendedorShouldNotBeFound("areaDeNegocioId.equals=" + (areaDeNegocioId + 1));
    }

    private void defaultVendedorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultVendedorShouldBeFound(shouldBeFound);
        defaultVendedorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVendedorShouldBeFound(String filter) throws Exception {
        restVendedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].autorizado").value(hasItem(DEFAULT_AUTORIZADO)));

        // Check, that the count call also returns 1
        restVendedorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVendedorShouldNotBeFound(String filter) throws Exception {
        restVendedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVendedorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVendedor() throws Exception {
        // Get the vendedor
        restVendedorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVendedor() throws Exception {
        // Initialize the database
        insertedVendedor = vendedorRepository.saveAndFlush(vendedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vendedor
        Vendedor updatedVendedor = vendedorRepository.findById(vendedor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVendedor are not directly saved in db
        em.detach(updatedVendedor);
        updatedVendedor.nombre(UPDATED_NOMBRE).autorizado(UPDATED_AUTORIZADO);

        restVendedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVendedor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVendedor))
            )
            .andExpect(status().isOk());

        // Validate the Vendedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVendedorToMatchAllProperties(updatedVendedor);
    }

    @Test
    @Transactional
    void putNonExistingVendedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendedor.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vendedor.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVendedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendedor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vendedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVendedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendedor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendedor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVendedorWithPatch() throws Exception {
        // Initialize the database
        insertedVendedor = vendedorRepository.saveAndFlush(vendedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vendedor using partial update
        Vendedor partialUpdatedVendedor = new Vendedor();
        partialUpdatedVendedor.setId(vendedor.getId());

        partialUpdatedVendedor.nombre(UPDATED_NOMBRE).autorizado(UPDATED_AUTORIZADO);

        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVendedor))
            )
            .andExpect(status().isOk());

        // Validate the Vendedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVendedorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVendedor, vendedor), getPersistedVendedor(vendedor));
    }

    @Test
    @Transactional
    void fullUpdateVendedorWithPatch() throws Exception {
        // Initialize the database
        insertedVendedor = vendedorRepository.saveAndFlush(vendedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vendedor using partial update
        Vendedor partialUpdatedVendedor = new Vendedor();
        partialUpdatedVendedor.setId(vendedor.getId());

        partialUpdatedVendedor.nombre(UPDATED_NOMBRE).autorizado(UPDATED_AUTORIZADO);

        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVendedor))
            )
            .andExpect(status().isOk());

        // Validate the Vendedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVendedorUpdatableFieldsEquals(partialUpdatedVendedor, getPersistedVendedor(partialUpdatedVendedor));
    }

    @Test
    @Transactional
    void patchNonExistingVendedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendedor.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vendedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vendedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVendedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendedor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vendedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVendedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendedor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vendedor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVendedor() throws Exception {
        // Initialize the database
        insertedVendedor = vendedorRepository.saveAndFlush(vendedor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vendedor
        restVendedorMockMvc
            .perform(delete(ENTITY_API_URL_ID, vendedor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vendedorRepository.count();
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

    protected Vendedor getPersistedVendedor(Vendedor vendedor) {
        return vendedorRepository.findById(vendedor.getId()).orElseThrow();
    }

    protected void assertPersistedVendedorToMatchAllProperties(Vendedor expectedVendedor) {
        assertVendedorAllPropertiesEquals(expectedVendedor, getPersistedVendedor(expectedVendedor));
    }

    protected void assertPersistedVendedorToMatchUpdatableProperties(Vendedor expectedVendedor) {
        assertVendedorAllUpdatablePropertiesEquals(expectedVendedor, getPersistedVendedor(expectedVendedor));
    }
}
