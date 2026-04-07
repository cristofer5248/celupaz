package com.chrisgeek.celupaz.web.rest;

import static com.chrisgeek.celupaz.domain.AlmaHistoryAsserts.*;
import static com.chrisgeek.celupaz.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chrisgeek.celupaz.IntegrationTest;
import com.chrisgeek.celupaz.domain.AlmaHistory;
import com.chrisgeek.celupaz.repository.AlmaHistoryRepository;
import com.chrisgeek.celupaz.service.AlmaHistoryService;
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
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AlmaHistoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AlmaHistoryResourceIT {

    private static final String DEFAULT_FECHA = "AAAAAAAAAA";
    private static final String UPDATED_FECHA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/alma-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlmaHistoryRepository almaHistoryRepository;

    @Mock
    private AlmaHistoryRepository almaHistoryRepositoryMock;

    @Mock
    private AlmaHistoryService almaHistoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlmaHistoryMockMvc;

    private AlmaHistory almaHistory;

    private AlmaHistory insertedAlmaHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlmaHistory createEntity() {
        return new AlmaHistory().fecha(DEFAULT_FECHA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlmaHistory createUpdatedEntity() {
        return new AlmaHistory().fecha(UPDATED_FECHA);
    }

    @BeforeEach
    void initTest() {
        almaHistory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAlmaHistory != null) {
            almaHistoryRepository.delete(insertedAlmaHistory);
            insertedAlmaHistory = null;
        }
    }

    @Test
    @Transactional
    void createAlmaHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlmaHistory
        var returnedAlmaHistory = om.readValue(
            restAlmaHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(almaHistory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlmaHistory.class
        );

        // Validate the AlmaHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlmaHistoryUpdatableFieldsEquals(returnedAlmaHistory, getPersistedAlmaHistory(returnedAlmaHistory));

        insertedAlmaHistory = returnedAlmaHistory;
    }

    @Test
    @Transactional
    void createAlmaHistoryWithExistingId() throws Exception {
        // Create the AlmaHistory with an existing ID
        almaHistory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlmaHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(almaHistory)))
            .andExpect(status().isBadRequest());

        // Validate the AlmaHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlmaHistories() throws Exception {
        // Initialize the database
        insertedAlmaHistory = almaHistoryRepository.saveAndFlush(almaHistory);

        // Get all the almaHistoryList
        restAlmaHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(almaHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlmaHistoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(almaHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlmaHistoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(almaHistoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlmaHistoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(almaHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlmaHistoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(almaHistoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAlmaHistory() throws Exception {
        // Initialize the database
        insertedAlmaHistory = almaHistoryRepository.saveAndFlush(almaHistory);

        // Get the almaHistory
        restAlmaHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, almaHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(almaHistory.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA));
    }

    @Test
    @Transactional
    void getNonExistingAlmaHistory() throws Exception {
        // Get the almaHistory
        restAlmaHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlmaHistory() throws Exception {
        // Initialize the database
        insertedAlmaHistory = almaHistoryRepository.saveAndFlush(almaHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the almaHistory
        AlmaHistory updatedAlmaHistory = almaHistoryRepository.findById(almaHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlmaHistory are not directly saved in db
        em.detach(updatedAlmaHistory);
        updatedAlmaHistory.fecha(UPDATED_FECHA);

        restAlmaHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlmaHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlmaHistory))
            )
            .andExpect(status().isOk());

        // Validate the AlmaHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlmaHistoryToMatchAllProperties(updatedAlmaHistory);
    }

    @Test
    @Transactional
    void putNonExistingAlmaHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        almaHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlmaHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, almaHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(almaHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlmaHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlmaHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        almaHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlmaHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(almaHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlmaHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlmaHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        almaHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlmaHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(almaHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlmaHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlmaHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedAlmaHistory = almaHistoryRepository.saveAndFlush(almaHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the almaHistory using partial update
        AlmaHistory partialUpdatedAlmaHistory = new AlmaHistory();
        partialUpdatedAlmaHistory.setId(almaHistory.getId());

        partialUpdatedAlmaHistory.fecha(UPDATED_FECHA);

        restAlmaHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlmaHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlmaHistory))
            )
            .andExpect(status().isOk());

        // Validate the AlmaHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlmaHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlmaHistory, almaHistory),
            getPersistedAlmaHistory(almaHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlmaHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedAlmaHistory = almaHistoryRepository.saveAndFlush(almaHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the almaHistory using partial update
        AlmaHistory partialUpdatedAlmaHistory = new AlmaHistory();
        partialUpdatedAlmaHistory.setId(almaHistory.getId());

        partialUpdatedAlmaHistory.fecha(UPDATED_FECHA);

        restAlmaHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlmaHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlmaHistory))
            )
            .andExpect(status().isOk());

        // Validate the AlmaHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlmaHistoryUpdatableFieldsEquals(partialUpdatedAlmaHistory, getPersistedAlmaHistory(partialUpdatedAlmaHistory));
    }

    @Test
    @Transactional
    void patchNonExistingAlmaHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        almaHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlmaHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, almaHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(almaHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlmaHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlmaHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        almaHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlmaHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(almaHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlmaHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlmaHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        almaHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlmaHistoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(almaHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlmaHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlmaHistory() throws Exception {
        // Initialize the database
        insertedAlmaHistory = almaHistoryRepository.saveAndFlush(almaHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the almaHistory
        restAlmaHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, almaHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return almaHistoryRepository.count();
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

    protected AlmaHistory getPersistedAlmaHistory(AlmaHistory almaHistory) {
        return almaHistoryRepository.findById(almaHistory.getId()).orElseThrow();
    }

    protected void assertPersistedAlmaHistoryToMatchAllProperties(AlmaHistory expectedAlmaHistory) {
        assertAlmaHistoryAllPropertiesEquals(expectedAlmaHistory, getPersistedAlmaHistory(expectedAlmaHistory));
    }

    protected void assertPersistedAlmaHistoryToMatchUpdatableProperties(AlmaHistory expectedAlmaHistory) {
        assertAlmaHistoryAllUpdatablePropertiesEquals(expectedAlmaHistory, getPersistedAlmaHistory(expectedAlmaHistory));
    }
}
