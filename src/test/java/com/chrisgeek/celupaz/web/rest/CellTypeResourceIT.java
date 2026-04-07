package com.chrisgeek.celupaz.web.rest;

import static com.chrisgeek.celupaz.domain.CellTypeAsserts.*;
import static com.chrisgeek.celupaz.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chrisgeek.celupaz.IntegrationTest;
import com.chrisgeek.celupaz.domain.CellType;
import com.chrisgeek.celupaz.repository.CellTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CellTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CellTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cell-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CellTypeRepository cellTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCellTypeMockMvc;

    private CellType cellType;

    private CellType insertedCellType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CellType createEntity() {
        return new CellType().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CellType createUpdatedEntity() {
        return new CellType().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        cellType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCellType != null) {
            cellTypeRepository.delete(insertedCellType);
            insertedCellType = null;
        }
    }

    @Test
    @Transactional
    void createCellType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CellType
        var returnedCellType = om.readValue(
            restCellTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cellType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CellType.class
        );

        // Validate the CellType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCellTypeUpdatableFieldsEquals(returnedCellType, getPersistedCellType(returnedCellType));

        insertedCellType = returnedCellType;
    }

    @Test
    @Transactional
    void createCellTypeWithExistingId() throws Exception {
        // Create the CellType with an existing ID
        cellType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCellTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cellType)))
            .andExpect(status().isBadRequest());

        // Validate the CellType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cellType.setName(null);

        // Create the CellType, which fails.

        restCellTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cellType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCellTypes() throws Exception {
        // Initialize the database
        insertedCellType = cellTypeRepository.saveAndFlush(cellType);

        // Get all the cellTypeList
        restCellTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cellType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCellType() throws Exception {
        // Initialize the database
        insertedCellType = cellTypeRepository.saveAndFlush(cellType);

        // Get the cellType
        restCellTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, cellType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cellType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCellType() throws Exception {
        // Get the cellType
        restCellTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCellType() throws Exception {
        // Initialize the database
        insertedCellType = cellTypeRepository.saveAndFlush(cellType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cellType
        CellType updatedCellType = cellTypeRepository.findById(cellType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCellType are not directly saved in db
        em.detach(updatedCellType);
        updatedCellType.name(UPDATED_NAME);

        restCellTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCellType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCellType))
            )
            .andExpect(status().isOk());

        // Validate the CellType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCellTypeToMatchAllProperties(updatedCellType);
    }

    @Test
    @Transactional
    void putNonExistingCellType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cellType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCellTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cellType.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cellType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CellType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCellType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cellType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCellTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cellType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CellType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCellType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cellType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCellTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cellType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CellType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCellTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCellType = cellTypeRepository.saveAndFlush(cellType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cellType using partial update
        CellType partialUpdatedCellType = new CellType();
        partialUpdatedCellType.setId(cellType.getId());

        restCellTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCellType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCellType))
            )
            .andExpect(status().isOk());

        // Validate the CellType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCellTypeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCellType, cellType), getPersistedCellType(cellType));
    }

    @Test
    @Transactional
    void fullUpdateCellTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCellType = cellTypeRepository.saveAndFlush(cellType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cellType using partial update
        CellType partialUpdatedCellType = new CellType();
        partialUpdatedCellType.setId(cellType.getId());

        partialUpdatedCellType.name(UPDATED_NAME);

        restCellTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCellType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCellType))
            )
            .andExpect(status().isOk());

        // Validate the CellType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCellTypeUpdatableFieldsEquals(partialUpdatedCellType, getPersistedCellType(partialUpdatedCellType));
    }

    @Test
    @Transactional
    void patchNonExistingCellType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cellType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCellTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cellType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cellType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CellType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCellType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cellType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCellTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cellType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CellType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCellType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cellType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCellTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cellType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CellType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCellType() throws Exception {
        // Initialize the database
        insertedCellType = cellTypeRepository.saveAndFlush(cellType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cellType
        restCellTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cellType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cellTypeRepository.count();
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

    protected CellType getPersistedCellType(CellType cellType) {
        return cellTypeRepository.findById(cellType.getId()).orElseThrow();
    }

    protected void assertPersistedCellTypeToMatchAllProperties(CellType expectedCellType) {
        assertCellTypeAllPropertiesEquals(expectedCellType, getPersistedCellType(expectedCellType));
    }

    protected void assertPersistedCellTypeToMatchUpdatableProperties(CellType expectedCellType) {
        assertCellTypeAllUpdatablePropertiesEquals(expectedCellType, getPersistedCellType(expectedCellType));
    }
}
