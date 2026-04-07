package com.chrisgeek.celupaz.web.rest;

import static com.chrisgeek.celupaz.domain.PrivilegeAsserts.*;
import static com.chrisgeek.celupaz.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chrisgeek.celupaz.IntegrationTest;
import com.chrisgeek.celupaz.domain.Privilege;
import com.chrisgeek.celupaz.repository.PrivilegeRepository;
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
 * Integration tests for the {@link PrivilegeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrivilegeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/privileges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrivilegeMockMvc;

    private Privilege privilege;

    private Privilege insertedPrivilege;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Privilege createEntity() {
        return new Privilege().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Privilege createUpdatedEntity() {
        return new Privilege().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        privilege = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPrivilege != null) {
            privilegeRepository.delete(insertedPrivilege);
            insertedPrivilege = null;
        }
    }

    @Test
    @Transactional
    void createPrivilege() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Privilege
        var returnedPrivilege = om.readValue(
            restPrivilegeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(privilege)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Privilege.class
        );

        // Validate the Privilege in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPrivilegeUpdatableFieldsEquals(returnedPrivilege, getPersistedPrivilege(returnedPrivilege));

        insertedPrivilege = returnedPrivilege;
    }

    @Test
    @Transactional
    void createPrivilegeWithExistingId() throws Exception {
        // Create the Privilege with an existing ID
        privilege.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrivilegeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(privilege)))
            .andExpect(status().isBadRequest());

        // Validate the Privilege in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        privilege.setName(null);

        // Create the Privilege, which fails.

        restPrivilegeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(privilege)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrivileges() throws Exception {
        // Initialize the database
        insertedPrivilege = privilegeRepository.saveAndFlush(privilege);

        // Get all the privilegeList
        restPrivilegeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(privilege.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPrivilege() throws Exception {
        // Initialize the database
        insertedPrivilege = privilegeRepository.saveAndFlush(privilege);

        // Get the privilege
        restPrivilegeMockMvc
            .perform(get(ENTITY_API_URL_ID, privilege.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(privilege.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPrivilege() throws Exception {
        // Get the privilege
        restPrivilegeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrivilege() throws Exception {
        // Initialize the database
        insertedPrivilege = privilegeRepository.saveAndFlush(privilege);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the privilege
        Privilege updatedPrivilege = privilegeRepository.findById(privilege.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPrivilege are not directly saved in db
        em.detach(updatedPrivilege);
        updatedPrivilege.name(UPDATED_NAME);

        restPrivilegeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrivilege.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPrivilege))
            )
            .andExpect(status().isOk());

        // Validate the Privilege in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPrivilegeToMatchAllProperties(updatedPrivilege);
    }

    @Test
    @Transactional
    void putNonExistingPrivilege() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        privilege.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrivilegeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, privilege.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(privilege))
            )
            .andExpect(status().isBadRequest());

        // Validate the Privilege in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrivilege() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        privilege.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrivilegeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(privilege))
            )
            .andExpect(status().isBadRequest());

        // Validate the Privilege in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrivilege() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        privilege.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrivilegeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(privilege)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Privilege in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrivilegeWithPatch() throws Exception {
        // Initialize the database
        insertedPrivilege = privilegeRepository.saveAndFlush(privilege);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the privilege using partial update
        Privilege partialUpdatedPrivilege = new Privilege();
        partialUpdatedPrivilege.setId(privilege.getId());

        partialUpdatedPrivilege.name(UPDATED_NAME);

        restPrivilegeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrivilege.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrivilege))
            )
            .andExpect(status().isOk());

        // Validate the Privilege in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrivilegeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPrivilege, privilege),
            getPersistedPrivilege(privilege)
        );
    }

    @Test
    @Transactional
    void fullUpdatePrivilegeWithPatch() throws Exception {
        // Initialize the database
        insertedPrivilege = privilegeRepository.saveAndFlush(privilege);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the privilege using partial update
        Privilege partialUpdatedPrivilege = new Privilege();
        partialUpdatedPrivilege.setId(privilege.getId());

        partialUpdatedPrivilege.name(UPDATED_NAME);

        restPrivilegeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrivilege.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrivilege))
            )
            .andExpect(status().isOk());

        // Validate the Privilege in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrivilegeUpdatableFieldsEquals(partialUpdatedPrivilege, getPersistedPrivilege(partialUpdatedPrivilege));
    }

    @Test
    @Transactional
    void patchNonExistingPrivilege() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        privilege.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrivilegeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, privilege.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(privilege))
            )
            .andExpect(status().isBadRequest());

        // Validate the Privilege in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrivilege() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        privilege.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrivilegeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(privilege))
            )
            .andExpect(status().isBadRequest());

        // Validate the Privilege in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrivilege() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        privilege.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrivilegeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(privilege)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Privilege in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrivilege() throws Exception {
        // Initialize the database
        insertedPrivilege = privilegeRepository.saveAndFlush(privilege);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the privilege
        restPrivilegeMockMvc
            .perform(delete(ENTITY_API_URL_ID, privilege.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return privilegeRepository.count();
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

    protected Privilege getPersistedPrivilege(Privilege privilege) {
        return privilegeRepository.findById(privilege.getId()).orElseThrow();
    }

    protected void assertPersistedPrivilegeToMatchAllProperties(Privilege expectedPrivilege) {
        assertPrivilegeAllPropertiesEquals(expectedPrivilege, getPersistedPrivilege(expectedPrivilege));
    }

    protected void assertPersistedPrivilegeToMatchUpdatableProperties(Privilege expectedPrivilege) {
        assertPrivilegeAllUpdatablePropertiesEquals(expectedPrivilege, getPersistedPrivilege(expectedPrivilege));
    }
}
