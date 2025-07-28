package co.edu.poligran.polimarket.web.rest;

import static co.edu.poligran.polimarket.domain.ProductoAsserts.*;
import static co.edu.poligran.polimarket.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.poligran.polimarket.IntegrationTest;
import co.edu.poligran.polimarket.domain.Producto;
import co.edu.poligran.polimarket.domain.Proveedor;
import co.edu.poligran.polimarket.domain.Tarea;
import co.edu.poligran.polimarket.repository.ProductoRepository;
import co.edu.poligran.polimarket.service.ProductoService;
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
 * Integration tests for the {@link ProductoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD_DISPONIBLE = 1;
    private static final Integer UPDATED_CANTIDAD_DISPONIBLE = 2;
    private static final Integer SMALLER_CANTIDAD_DISPONIBLE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/productos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductoRepository productoRepository;

    @Mock
    private ProductoRepository productoRepositoryMock;

    @Mock
    private ProductoService productoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductoMockMvc;

    private Producto producto;

    private Producto insertedProducto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producto createEntity() {
        return new Producto().nombre(DEFAULT_NOMBRE).cantidadDisponible(DEFAULT_CANTIDAD_DISPONIBLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producto createUpdatedEntity() {
        return new Producto().nombre(UPDATED_NOMBRE).cantidadDisponible(UPDATED_CANTIDAD_DISPONIBLE);
    }

    @BeforeEach
    void initTest() {
        producto = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProducto != null) {
            productoRepository.delete(insertedProducto);
            insertedProducto = null;
        }
    }

    @Test
    @Transactional
    void createProducto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Producto
        var returnedProducto = om.readValue(
            restProductoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(producto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Producto.class
        );

        // Validate the Producto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductoUpdatableFieldsEquals(returnedProducto, getPersistedProducto(returnedProducto));

        insertedProducto = returnedProducto;
    }

    @Test
    @Transactional
    void createProductoWithExistingId() throws Exception {
        // Create the Producto with an existing ID
        producto.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(producto)))
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producto.setNombre(null);

        // Create the Producto, which fails.

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(producto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductos() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].cantidadDisponible").value(hasItem(DEFAULT_CANTIDAD_DISPONIBLE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductosWithEagerRelationshipsIsEnabled() throws Exception {
        when(productoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProducto() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get the producto
        restProductoMockMvc
            .perform(get(ENTITY_API_URL_ID, producto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(producto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.cantidadDisponible").value(DEFAULT_CANTIDAD_DISPONIBLE));
    }

    @Test
    @Transactional
    void getProductosByIdFiltering() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        Long id = producto.getId();

        defaultProductoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre equals to
        defaultProductoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProductosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre in
        defaultProductoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProductosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre is not null
        defaultProductoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre contains
        defaultProductoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProductosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre does not contain
        defaultProductoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProductosByCantidadDisponibleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidadDisponible equals to
        defaultProductoFiltering(
            "cantidadDisponible.equals=" + DEFAULT_CANTIDAD_DISPONIBLE,
            "cantidadDisponible.equals=" + UPDATED_CANTIDAD_DISPONIBLE
        );
    }

    @Test
    @Transactional
    void getAllProductosByCantidadDisponibleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidadDisponible in
        defaultProductoFiltering(
            "cantidadDisponible.in=" + DEFAULT_CANTIDAD_DISPONIBLE + "," + UPDATED_CANTIDAD_DISPONIBLE,
            "cantidadDisponible.in=" + UPDATED_CANTIDAD_DISPONIBLE
        );
    }

    @Test
    @Transactional
    void getAllProductosByCantidadDisponibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidadDisponible is not null
        defaultProductoFiltering("cantidadDisponible.specified=true", "cantidadDisponible.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByCantidadDisponibleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidadDisponible is greater than or equal to
        defaultProductoFiltering(
            "cantidadDisponible.greaterThanOrEqual=" + DEFAULT_CANTIDAD_DISPONIBLE,
            "cantidadDisponible.greaterThanOrEqual=" + UPDATED_CANTIDAD_DISPONIBLE
        );
    }

    @Test
    @Transactional
    void getAllProductosByCantidadDisponibleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidadDisponible is less than or equal to
        defaultProductoFiltering(
            "cantidadDisponible.lessThanOrEqual=" + DEFAULT_CANTIDAD_DISPONIBLE,
            "cantidadDisponible.lessThanOrEqual=" + SMALLER_CANTIDAD_DISPONIBLE
        );
    }

    @Test
    @Transactional
    void getAllProductosByCantidadDisponibleIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidadDisponible is less than
        defaultProductoFiltering(
            "cantidadDisponible.lessThan=" + UPDATED_CANTIDAD_DISPONIBLE,
            "cantidadDisponible.lessThan=" + DEFAULT_CANTIDAD_DISPONIBLE
        );
    }

    @Test
    @Transactional
    void getAllProductosByCantidadDisponibleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidadDisponible is greater than
        defaultProductoFiltering(
            "cantidadDisponible.greaterThan=" + SMALLER_CANTIDAD_DISPONIBLE,
            "cantidadDisponible.greaterThan=" + DEFAULT_CANTIDAD_DISPONIBLE
        );
    }

    @Test
    @Transactional
    void getAllProductosByProveedorIsEqualToSomething() throws Exception {
        Proveedor proveedor;
        if (TestUtil.findAll(em, Proveedor.class).isEmpty()) {
            productoRepository.saveAndFlush(producto);
            proveedor = ProveedorResourceIT.createEntity();
        } else {
            proveedor = TestUtil.findAll(em, Proveedor.class).get(0);
        }
        em.persist(proveedor);
        em.flush();
        producto.setProveedor(proveedor);
        productoRepository.saveAndFlush(producto);
        Long proveedorId = proveedor.getId();
        // Get all the productoList where proveedor equals to proveedorId
        defaultProductoShouldBeFound("proveedorId.equals=" + proveedorId);

        // Get all the productoList where proveedor equals to (proveedorId + 1)
        defaultProductoShouldNotBeFound("proveedorId.equals=" + (proveedorId + 1));
    }

    @Test
    @Transactional
    void getAllProductosByTareaIsEqualToSomething() throws Exception {
        Tarea tarea;
        if (TestUtil.findAll(em, Tarea.class).isEmpty()) {
            productoRepository.saveAndFlush(producto);
            tarea = TareaResourceIT.createEntity();
        } else {
            tarea = TestUtil.findAll(em, Tarea.class).get(0);
        }
        em.persist(tarea);
        em.flush();
        producto.setTarea(tarea);
        productoRepository.saveAndFlush(producto);
        Long tareaId = tarea.getId();
        // Get all the productoList where tarea equals to tareaId
        defaultProductoShouldBeFound("tareaId.equals=" + tareaId);

        // Get all the productoList where tarea equals to (tareaId + 1)
        defaultProductoShouldNotBeFound("tareaId.equals=" + (tareaId + 1));
    }

    private void defaultProductoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductoShouldBeFound(shouldBeFound);
        defaultProductoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductoShouldBeFound(String filter) throws Exception {
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].cantidadDisponible").value(hasItem(DEFAULT_CANTIDAD_DISPONIBLE)));

        // Check, that the count call also returns 1
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductoShouldNotBeFound(String filter) throws Exception {
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProducto() throws Exception {
        // Get the producto
        restProductoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProducto() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producto
        Producto updatedProducto = productoRepository.findById(producto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProducto are not directly saved in db
        em.detach(updatedProducto);
        updatedProducto.nombre(UPDATED_NOMBRE).cantidadDisponible(UPDATED_CANTIDAD_DISPONIBLE);

        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProducto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProducto))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductoToMatchAllProperties(updatedProducto);
    }

    @Test
    @Transactional
    void putNonExistingProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, producto.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(producto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(producto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(producto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductoWithPatch() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producto using partial update
        Producto partialUpdatedProducto = new Producto();
        partialUpdatedProducto.setId(producto.getId());

        partialUpdatedProducto.cantidadDisponible(UPDATED_CANTIDAD_DISPONIBLE);

        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProducto))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProducto, producto), getPersistedProducto(producto));
    }

    @Test
    @Transactional
    void fullUpdateProductoWithPatch() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producto using partial update
        Producto partialUpdatedProducto = new Producto();
        partialUpdatedProducto.setId(producto.getId());

        partialUpdatedProducto.nombre(UPDATED_NOMBRE).cantidadDisponible(UPDATED_CANTIDAD_DISPONIBLE);

        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProducto))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductoUpdatableFieldsEquals(partialUpdatedProducto, getPersistedProducto(partialUpdatedProducto));
    }

    @Test
    @Transactional
    void patchNonExistingProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, producto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(producto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(producto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(producto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProducto() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the producto
        restProductoMockMvc
            .perform(delete(ENTITY_API_URL_ID, producto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productoRepository.count();
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

    protected Producto getPersistedProducto(Producto producto) {
        return productoRepository.findById(producto.getId()).orElseThrow();
    }

    protected void assertPersistedProductoToMatchAllProperties(Producto expectedProducto) {
        assertProductoAllPropertiesEquals(expectedProducto, getPersistedProducto(expectedProducto));
    }

    protected void assertPersistedProductoToMatchUpdatableProperties(Producto expectedProducto) {
        assertProductoAllUpdatablePropertiesEquals(expectedProducto, getPersistedProducto(expectedProducto));
    }
}
