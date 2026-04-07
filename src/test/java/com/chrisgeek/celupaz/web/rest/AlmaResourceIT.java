package com.chrisgeek.celupaz.web.rest;

import static com.chrisgeek.celupaz.domain.AlmaAsserts.*;
import static com.chrisgeek.celupaz.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chrisgeek.celupaz.IntegrationTest;
import com.chrisgeek.celupaz.domain.Alma;
import com.chrisgeek.celupaz.repository.AlmaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Base64;
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
 * Integration tests for the {@link AlmaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlmaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_MUNICIPALITY = "AAAAAAAAAA";
    private static final String UPDATED_MUNICIPALITY = "BBBBBBBBBB";

    private static final String DEFAULT_COLONY = "AAAAAAAAAA";
    private static final String UPDATED_COLONY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/almas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlmaRepository almaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlmaMockMvc;

    private Alma alma;

    private Alma insertedAlma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alma createEntity() {
        return new Alma()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .department(DEFAULT_DEPARTMENT)
            .municipality(DEFAULT_MUNICIPALITY)
            .colony(DEFAULT_COLONY)
            .description(DEFAULT_DESCRIPTION)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alma createUpdatedEntity() {
        return new Alma()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .department(UPDATED_DEPARTMENT)
            .municipality(UPDATED_MUNICIPALITY)
            .colony(UPDATED_COLONY)
            .description(UPDATED_DESCRIPTION)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);
    }

    @BeforeEach
    void initTest() {
        alma = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAlma != null) {
            almaRepository.delete(insertedAlma);
            insertedAlma = null;
        }
    }

    @Test
    @Transactional
    void createAlma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Alma
        var returnedAlma = om.readValue(
            restAlmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Alma.class
        );

        // Validate the Alma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlmaUpdatableFieldsEquals(returnedAlma, getPersistedAlma(returnedAlma));

        insertedAlma = returnedAlma;
    }

    @Test
    @Transactional
    void createAlmaWithExistingId() throws Exception {
        // Create the Alma with an existing ID
        alma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alma)))
            .andExpect(status().isBadRequest());

        // Validate the Alma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alma.setName(null);

        // Create the Alma, which fails.

        restAlmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlmas() throws Exception {
        // Initialize the database
        insertedAlma = almaRepository.saveAndFlush(alma);

        // Get all the almaList
        restAlmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].municipality").value(hasItem(DEFAULT_MUNICIPALITY)))
            .andExpect(jsonPath("$.[*].colony").value(hasItem(DEFAULT_COLONY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FOTO))));
    }

    @Test
    @Transactional
    void getAlma() throws Exception {
        // Initialize the database
        insertedAlma = almaRepository.saveAndFlush(alma);

        // Get the alma
        restAlmaMockMvc
            .perform(get(ENTITY_API_URL_ID, alma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alma.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.municipality").value(DEFAULT_MUNICIPALITY))
            .andExpect(jsonPath("$.colony").value(DEFAULT_COLONY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64.getEncoder().encodeToString(DEFAULT_FOTO)));
    }

    @Test
    @Transactional
    void getNonExistingAlma() throws Exception {
        // Get the alma
        restAlmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlma() throws Exception {
        // Initialize the database
        insertedAlma = almaRepository.saveAndFlush(alma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alma
        Alma updatedAlma = almaRepository.findById(alma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlma are not directly saved in db
        em.detach(updatedAlma);
        updatedAlma
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .department(UPDATED_DEPARTMENT)
            .municipality(UPDATED_MUNICIPALITY)
            .colony(UPDATED_COLONY)
            .description(UPDATED_DESCRIPTION)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        restAlmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlma))
            )
            .andExpect(status().isOk());

        // Validate the Alma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlmaToMatchAllProperties(updatedAlma);
    }

    @Test
    @Transactional
    void putNonExistingAlma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlmaMockMvc
            .perform(put(ENTITY_API_URL_ID, alma.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alma)))
            .andExpect(status().isBadRequest());

        // Validate the Alma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alma))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlmaWithPatch() throws Exception {
        // Initialize the database
        insertedAlma = almaRepository.saveAndFlush(alma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alma using partial update
        Alma partialUpdatedAlma = new Alma();
        partialUpdatedAlma.setId(alma.getId());

        partialUpdatedAlma
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .colony(UPDATED_COLONY)
            .description(UPDATED_DESCRIPTION)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        restAlmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlma))
            )
            .andExpect(status().isOk());

        // Validate the Alma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlmaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlma, alma), getPersistedAlma(alma));
    }

    @Test
    @Transactional
    void fullUpdateAlmaWithPatch() throws Exception {
        // Initialize the database
        insertedAlma = almaRepository.saveAndFlush(alma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alma using partial update
        Alma partialUpdatedAlma = new Alma();
        partialUpdatedAlma.setId(alma.getId());

        partialUpdatedAlma
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .department(UPDATED_DEPARTMENT)
            .municipality(UPDATED_MUNICIPALITY)
            .colony(UPDATED_COLONY)
            .description(UPDATED_DESCRIPTION)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        restAlmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlma))
            )
            .andExpect(status().isOk());

        // Validate the Alma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlmaUpdatableFieldsEquals(partialUpdatedAlma, getPersistedAlma(partialUpdatedAlma));
    }

    @Test
    @Transactional
    void patchNonExistingAlma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlmaMockMvc
            .perform(patch(ENTITY_API_URL_ID, alma.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alma)))
            .andExpect(status().isBadRequest());

        // Validate the Alma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alma))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlma() throws Exception {
        // Initialize the database
        insertedAlma = almaRepository.saveAndFlush(alma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alma
        restAlmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, alma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return almaRepository.count();
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

    protected Alma getPersistedAlma(Alma alma) {
        return almaRepository.findById(alma.getId()).orElseThrow();
    }

    protected void assertPersistedAlmaToMatchAllProperties(Alma expectedAlma) {
        assertAlmaAllPropertiesEquals(expectedAlma, getPersistedAlma(expectedAlma));
    }

    protected void assertPersistedAlmaToMatchUpdatableProperties(Alma expectedAlma) {
        assertAlmaAllUpdatablePropertiesEquals(expectedAlma, getPersistedAlma(expectedAlma));
    }
}
