package com.chrisgeek.celupaz.web.rest;

import static com.chrisgeek.celupaz.domain.IglesiaAsserts.*;
import static com.chrisgeek.celupaz.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chrisgeek.celupaz.IntegrationTest;
import com.chrisgeek.celupaz.domain.Iglesia;
import com.chrisgeek.celupaz.repository.IglesiaRepository;
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
 * Integration tests for the {@link IglesiaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IglesiaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/iglesias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IglesiaRepository iglesiaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIglesiaMockMvc;

    private Iglesia iglesia;

    private Iglesia insertedIglesia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Iglesia createEntity() {
        return new Iglesia().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Iglesia createUpdatedEntity() {
        return new Iglesia().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        iglesia = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIglesia != null) {
            iglesiaRepository.delete(insertedIglesia);
            insertedIglesia = null;
        }
    }

    @Test
    @Transactional
    void createIglesia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Iglesia
        var returnedIglesia = om.readValue(
            restIglesiaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(iglesia)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Iglesia.class
        );

        // Validate the Iglesia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIglesiaUpdatableFieldsEquals(returnedIglesia, getPersistedIglesia(returnedIglesia));

        insertedIglesia = returnedIglesia;
    }

    @Test
    @Transactional
    void createIglesiaWithExistingId() throws Exception {
        // Create the Iglesia with an existing ID
        iglesia.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIglesiaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(iglesia)))
            .andExpect(status().isBadRequest());

        // Validate the Iglesia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        iglesia.setName(null);

        // Create the Iglesia, which fails.

        restIglesiaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(iglesia)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIglesias() throws Exception {
        // Initialize the database
        insertedIglesia = iglesiaRepository.saveAndFlush(iglesia);

        // Get all the iglesiaList
        restIglesiaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iglesia.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getIglesia() throws Exception {
        // Initialize the database
        insertedIglesia = iglesiaRepository.saveAndFlush(iglesia);

        // Get the iglesia
        restIglesiaMockMvc
            .perform(get(ENTITY_API_URL_ID, iglesia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(iglesia.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingIglesia() throws Exception {
        // Get the iglesia
        restIglesiaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIglesia() throws Exception {
        // Initialize the database
        insertedIglesia = iglesiaRepository.saveAndFlush(iglesia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the iglesia
        Iglesia updatedIglesia = iglesiaRepository.findById(iglesia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIglesia are not directly saved in db
        em.detach(updatedIglesia);
        updatedIglesia.name(UPDATED_NAME);

        restIglesiaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIglesia.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIglesia))
            )
            .andExpect(status().isOk());

        // Validate the Iglesia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIglesiaToMatchAllProperties(updatedIglesia);
    }

    @Test
    @Transactional
    void putNonExistingIglesia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        iglesia.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIglesiaMockMvc
            .perform(put(ENTITY_API_URL_ID, iglesia.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(iglesia)))
            .andExpect(status().isBadRequest());

        // Validate the Iglesia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIglesia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        iglesia.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIglesiaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(iglesia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Iglesia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIglesia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        iglesia.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIglesiaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(iglesia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Iglesia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIglesiaWithPatch() throws Exception {
        // Initialize the database
        insertedIglesia = iglesiaRepository.saveAndFlush(iglesia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the iglesia using partial update
        Iglesia partialUpdatedIglesia = new Iglesia();
        partialUpdatedIglesia.setId(iglesia.getId());

        restIglesiaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIglesia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIglesia))
            )
            .andExpect(status().isOk());

        // Validate the Iglesia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIglesiaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedIglesia, iglesia), getPersistedIglesia(iglesia));
    }

    @Test
    @Transactional
    void fullUpdateIglesiaWithPatch() throws Exception {
        // Initialize the database
        insertedIglesia = iglesiaRepository.saveAndFlush(iglesia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the iglesia using partial update
        Iglesia partialUpdatedIglesia = new Iglesia();
        partialUpdatedIglesia.setId(iglesia.getId());

        partialUpdatedIglesia.name(UPDATED_NAME);

        restIglesiaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIglesia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIglesia))
            )
            .andExpect(status().isOk());

        // Validate the Iglesia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIglesiaUpdatableFieldsEquals(partialUpdatedIglesia, getPersistedIglesia(partialUpdatedIglesia));
    }

    @Test
    @Transactional
    void patchNonExistingIglesia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        iglesia.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIglesiaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, iglesia.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(iglesia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Iglesia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIglesia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        iglesia.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIglesiaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(iglesia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Iglesia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIglesia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        iglesia.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIglesiaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(iglesia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Iglesia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIglesia() throws Exception {
        // Initialize the database
        insertedIglesia = iglesiaRepository.saveAndFlush(iglesia);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the iglesia
        restIglesiaMockMvc
            .perform(delete(ENTITY_API_URL_ID, iglesia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return iglesiaRepository.count();
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

    protected Iglesia getPersistedIglesia(Iglesia iglesia) {
        return iglesiaRepository.findById(iglesia.getId()).orElseThrow();
    }

    protected void assertPersistedIglesiaToMatchAllProperties(Iglesia expectedIglesia) {
        assertIglesiaAllPropertiesEquals(expectedIglesia, getPersistedIglesia(expectedIglesia));
    }

    protected void assertPersistedIglesiaToMatchUpdatableProperties(Iglesia expectedIglesia) {
        assertIglesiaAllUpdatablePropertiesEquals(expectedIglesia, getPersistedIglesia(expectedIglesia));
    }
}
