package com.chrisgeek.celupaz.web.rest;

import static com.chrisgeek.celupaz.domain.RolCelulaAsserts.*;
import static com.chrisgeek.celupaz.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chrisgeek.celupaz.IntegrationTest;
import com.chrisgeek.celupaz.domain.RolCelula;
import com.chrisgeek.celupaz.repository.RolCelulaRepository;
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
 * Integration tests for the {@link RolCelulaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RolCelulaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rol-celulas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RolCelulaRepository rolCelulaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRolCelulaMockMvc;

    private RolCelula rolCelula;

    private RolCelula insertedRolCelula;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RolCelula createEntity() {
        return new RolCelula().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RolCelula createUpdatedEntity() {
        return new RolCelula().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        rolCelula = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRolCelula != null) {
            rolCelulaRepository.delete(insertedRolCelula);
            insertedRolCelula = null;
        }
    }

    @Test
    @Transactional
    void createRolCelula() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RolCelula
        var returnedRolCelula = om.readValue(
            restRolCelulaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolCelula)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RolCelula.class
        );

        // Validate the RolCelula in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRolCelulaUpdatableFieldsEquals(returnedRolCelula, getPersistedRolCelula(returnedRolCelula));

        insertedRolCelula = returnedRolCelula;
    }

    @Test
    @Transactional
    void createRolCelulaWithExistingId() throws Exception {
        // Create the RolCelula with an existing ID
        rolCelula.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRolCelulaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolCelula)))
            .andExpect(status().isBadRequest());

        // Validate the RolCelula in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rolCelula.setName(null);

        // Create the RolCelula, which fails.

        restRolCelulaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolCelula)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRolCelulas() throws Exception {
        // Initialize the database
        insertedRolCelula = rolCelulaRepository.saveAndFlush(rolCelula);

        // Get all the rolCelulaList
        restRolCelulaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rolCelula.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRolCelula() throws Exception {
        // Initialize the database
        insertedRolCelula = rolCelulaRepository.saveAndFlush(rolCelula);

        // Get the rolCelula
        restRolCelulaMockMvc
            .perform(get(ENTITY_API_URL_ID, rolCelula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rolCelula.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingRolCelula() throws Exception {
        // Get the rolCelula
        restRolCelulaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRolCelula() throws Exception {
        // Initialize the database
        insertedRolCelula = rolCelulaRepository.saveAndFlush(rolCelula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rolCelula
        RolCelula updatedRolCelula = rolCelulaRepository.findById(rolCelula.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRolCelula are not directly saved in db
        em.detach(updatedRolCelula);
        updatedRolCelula.name(UPDATED_NAME);

        restRolCelulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRolCelula.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRolCelula))
            )
            .andExpect(status().isOk());

        // Validate the RolCelula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRolCelulaToMatchAllProperties(updatedRolCelula);
    }

    @Test
    @Transactional
    void putNonExistingRolCelula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rolCelula.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRolCelulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rolCelula.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolCelula))
            )
            .andExpect(status().isBadRequest());

        // Validate the RolCelula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRolCelula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rolCelula.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolCelulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rolCelula))
            )
            .andExpect(status().isBadRequest());

        // Validate the RolCelula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRolCelula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rolCelula.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolCelulaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolCelula)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RolCelula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRolCelulaWithPatch() throws Exception {
        // Initialize the database
        insertedRolCelula = rolCelulaRepository.saveAndFlush(rolCelula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rolCelula using partial update
        RolCelula partialUpdatedRolCelula = new RolCelula();
        partialUpdatedRolCelula.setId(rolCelula.getId());

        partialUpdatedRolCelula.name(UPDATED_NAME);

        restRolCelulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRolCelula.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRolCelula))
            )
            .andExpect(status().isOk());

        // Validate the RolCelula in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRolCelulaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRolCelula, rolCelula),
            getPersistedRolCelula(rolCelula)
        );
    }

    @Test
    @Transactional
    void fullUpdateRolCelulaWithPatch() throws Exception {
        // Initialize the database
        insertedRolCelula = rolCelulaRepository.saveAndFlush(rolCelula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rolCelula using partial update
        RolCelula partialUpdatedRolCelula = new RolCelula();
        partialUpdatedRolCelula.setId(rolCelula.getId());

        partialUpdatedRolCelula.name(UPDATED_NAME);

        restRolCelulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRolCelula.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRolCelula))
            )
            .andExpect(status().isOk());

        // Validate the RolCelula in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRolCelulaUpdatableFieldsEquals(partialUpdatedRolCelula, getPersistedRolCelula(partialUpdatedRolCelula));
    }

    @Test
    @Transactional
    void patchNonExistingRolCelula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rolCelula.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRolCelulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rolCelula.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rolCelula))
            )
            .andExpect(status().isBadRequest());

        // Validate the RolCelula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRolCelula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rolCelula.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolCelulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rolCelula))
            )
            .andExpect(status().isBadRequest());

        // Validate the RolCelula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRolCelula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rolCelula.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolCelulaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rolCelula)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RolCelula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRolCelula() throws Exception {
        // Initialize the database
        insertedRolCelula = rolCelulaRepository.saveAndFlush(rolCelula);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rolCelula
        restRolCelulaMockMvc
            .perform(delete(ENTITY_API_URL_ID, rolCelula.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rolCelulaRepository.count();
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

    protected RolCelula getPersistedRolCelula(RolCelula rolCelula) {
        return rolCelulaRepository.findById(rolCelula.getId()).orElseThrow();
    }

    protected void assertPersistedRolCelulaToMatchAllProperties(RolCelula expectedRolCelula) {
        assertRolCelulaAllPropertiesEquals(expectedRolCelula, getPersistedRolCelula(expectedRolCelula));
    }

    protected void assertPersistedRolCelulaToMatchUpdatableProperties(RolCelula expectedRolCelula) {
        assertRolCelulaAllUpdatablePropertiesEquals(expectedRolCelula, getPersistedRolCelula(expectedRolCelula));
    }
}
