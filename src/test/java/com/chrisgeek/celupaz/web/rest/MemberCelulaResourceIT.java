package com.chrisgeek.celupaz.web.rest;

import static com.chrisgeek.celupaz.domain.MemberCelulaAsserts.*;
import static com.chrisgeek.celupaz.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chrisgeek.celupaz.IntegrationTest;
import com.chrisgeek.celupaz.domain.MemberCelula;
import com.chrisgeek.celupaz.repository.MemberCelulaRepository;
import com.chrisgeek.celupaz.service.MemberCelulaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link MemberCelulaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MemberCelulaResourceIT {

    private static final LocalDate DEFAULT_FECHA_CREADA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_CREADA = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/member-celulas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MemberCelulaRepository memberCelulaRepository;

    @Mock
    private MemberCelulaRepository memberCelulaRepositoryMock;

    @Mock
    private MemberCelulaService memberCelulaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemberCelulaMockMvc;

    private MemberCelula memberCelula;

    private MemberCelula insertedMemberCelula;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberCelula createEntity() {
        return new MemberCelula().fechaCreada(DEFAULT_FECHA_CREADA).enabled(DEFAULT_ENABLED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberCelula createUpdatedEntity() {
        return new MemberCelula().fechaCreada(UPDATED_FECHA_CREADA).enabled(UPDATED_ENABLED);
    }

    @BeforeEach
    void initTest() {
        memberCelula = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMemberCelula != null) {
            memberCelulaRepository.delete(insertedMemberCelula);
            insertedMemberCelula = null;
        }
    }

    @Test
    @Transactional
    void createMemberCelula() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MemberCelula
        var returnedMemberCelula = om.readValue(
            restMemberCelulaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberCelula)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MemberCelula.class
        );

        // Validate the MemberCelula in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMemberCelulaUpdatableFieldsEquals(returnedMemberCelula, getPersistedMemberCelula(returnedMemberCelula));

        insertedMemberCelula = returnedMemberCelula;
    }

    @Test
    @Transactional
    void createMemberCelulaWithExistingId() throws Exception {
        // Create the MemberCelula with an existing ID
        memberCelula.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberCelulaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberCelula)))
            .andExpect(status().isBadRequest());

        // Validate the MemberCelula in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaCreadaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        memberCelula.setFechaCreada(null);

        // Create the MemberCelula, which fails.

        restMemberCelulaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberCelula)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnabledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        memberCelula.setEnabled(null);

        // Create the MemberCelula, which fails.

        restMemberCelulaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberCelula)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMemberCelulas() throws Exception {
        // Initialize the database
        insertedMemberCelula = memberCelulaRepository.saveAndFlush(memberCelula);

        // Get all the memberCelulaList
        restMemberCelulaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberCelula.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCreada").value(hasItem(DEFAULT_FECHA_CREADA.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMemberCelulasWithEagerRelationshipsIsEnabled() throws Exception {
        when(memberCelulaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMemberCelulaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(memberCelulaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMemberCelulasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(memberCelulaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMemberCelulaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(memberCelulaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMemberCelula() throws Exception {
        // Initialize the database
        insertedMemberCelula = memberCelulaRepository.saveAndFlush(memberCelula);

        // Get the memberCelula
        restMemberCelulaMockMvc
            .perform(get(ENTITY_API_URL_ID, memberCelula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(memberCelula.getId().intValue()))
            .andExpect(jsonPath("$.fechaCreada").value(DEFAULT_FECHA_CREADA.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED));
    }

    @Test
    @Transactional
    void getNonExistingMemberCelula() throws Exception {
        // Get the memberCelula
        restMemberCelulaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMemberCelula() throws Exception {
        // Initialize the database
        insertedMemberCelula = memberCelulaRepository.saveAndFlush(memberCelula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the memberCelula
        MemberCelula updatedMemberCelula = memberCelulaRepository.findById(memberCelula.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMemberCelula are not directly saved in db
        em.detach(updatedMemberCelula);
        updatedMemberCelula.fechaCreada(UPDATED_FECHA_CREADA).enabled(UPDATED_ENABLED);

        restMemberCelulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMemberCelula.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMemberCelula))
            )
            .andExpect(status().isOk());

        // Validate the MemberCelula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMemberCelulaToMatchAllProperties(updatedMemberCelula);
    }

    @Test
    @Transactional
    void putNonExistingMemberCelula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberCelula.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberCelulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memberCelula.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(memberCelula))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberCelula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMemberCelula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberCelula.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberCelulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(memberCelula))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberCelula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMemberCelula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberCelula.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberCelulaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberCelula)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MemberCelula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMemberCelulaWithPatch() throws Exception {
        // Initialize the database
        insertedMemberCelula = memberCelulaRepository.saveAndFlush(memberCelula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the memberCelula using partial update
        MemberCelula partialUpdatedMemberCelula = new MemberCelula();
        partialUpdatedMemberCelula.setId(memberCelula.getId());

        partialUpdatedMemberCelula.fechaCreada(UPDATED_FECHA_CREADA);

        restMemberCelulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMemberCelula.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMemberCelula))
            )
            .andExpect(status().isOk());

        // Validate the MemberCelula in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMemberCelulaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMemberCelula, memberCelula),
            getPersistedMemberCelula(memberCelula)
        );
    }

    @Test
    @Transactional
    void fullUpdateMemberCelulaWithPatch() throws Exception {
        // Initialize the database
        insertedMemberCelula = memberCelulaRepository.saveAndFlush(memberCelula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the memberCelula using partial update
        MemberCelula partialUpdatedMemberCelula = new MemberCelula();
        partialUpdatedMemberCelula.setId(memberCelula.getId());

        partialUpdatedMemberCelula.fechaCreada(UPDATED_FECHA_CREADA).enabled(UPDATED_ENABLED);

        restMemberCelulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMemberCelula.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMemberCelula))
            )
            .andExpect(status().isOk());

        // Validate the MemberCelula in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMemberCelulaUpdatableFieldsEquals(partialUpdatedMemberCelula, getPersistedMemberCelula(partialUpdatedMemberCelula));
    }

    @Test
    @Transactional
    void patchNonExistingMemberCelula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberCelula.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberCelulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, memberCelula.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(memberCelula))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberCelula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMemberCelula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberCelula.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberCelulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(memberCelula))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberCelula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMemberCelula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberCelula.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberCelulaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(memberCelula)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MemberCelula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMemberCelula() throws Exception {
        // Initialize the database
        insertedMemberCelula = memberCelulaRepository.saveAndFlush(memberCelula);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the memberCelula
        restMemberCelulaMockMvc
            .perform(delete(ENTITY_API_URL_ID, memberCelula.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return memberCelulaRepository.count();
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

    protected MemberCelula getPersistedMemberCelula(MemberCelula memberCelula) {
        return memberCelulaRepository.findById(memberCelula.getId()).orElseThrow();
    }

    protected void assertPersistedMemberCelulaToMatchAllProperties(MemberCelula expectedMemberCelula) {
        assertMemberCelulaAllPropertiesEquals(expectedMemberCelula, getPersistedMemberCelula(expectedMemberCelula));
    }

    protected void assertPersistedMemberCelulaToMatchUpdatableProperties(MemberCelula expectedMemberCelula) {
        assertMemberCelulaAllUpdatablePropertiesEquals(expectedMemberCelula, getPersistedMemberCelula(expectedMemberCelula));
    }
}
