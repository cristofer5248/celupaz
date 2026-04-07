package com.chrisgeek.celupaz.web.rest;

import static com.chrisgeek.celupaz.domain.PlanificacionAsserts.*;
import static com.chrisgeek.celupaz.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chrisgeek.celupaz.IntegrationTest;
import com.chrisgeek.celupaz.domain.Planificacion;
import com.chrisgeek.celupaz.repository.PlanificacionRepository;
import com.chrisgeek.celupaz.service.PlanificacionService;
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
 * Integration tests for the {@link PlanificacionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PlanificacionResourceIT {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/planificacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlanificacionRepository planificacionRepository;

    @Mock
    private PlanificacionRepository planificacionRepositoryMock;

    @Mock
    private PlanificacionService planificacionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanificacionMockMvc;

    private Planificacion planificacion;

    private Planificacion insertedPlanificacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Planificacion createEntity() {
        return new Planificacion().fecha(DEFAULT_FECHA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Planificacion createUpdatedEntity() {
        return new Planificacion().fecha(UPDATED_FECHA);
    }

    @BeforeEach
    void initTest() {
        planificacion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPlanificacion != null) {
            planificacionRepository.delete(insertedPlanificacion);
            insertedPlanificacion = null;
        }
    }

    @Test
    @Transactional
    void createPlanificacion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Planificacion
        var returnedPlanificacion = om.readValue(
            restPlanificacionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planificacion)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Planificacion.class
        );

        // Validate the Planificacion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPlanificacionUpdatableFieldsEquals(returnedPlanificacion, getPersistedPlanificacion(returnedPlanificacion));

        insertedPlanificacion = returnedPlanificacion;
    }

    @Test
    @Transactional
    void createPlanificacionWithExistingId() throws Exception {
        // Create the Planificacion with an existing ID
        planificacion.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanificacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planificacion)))
            .andExpect(status().isBadRequest());

        // Validate the Planificacion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        planificacion.setFecha(null);

        // Create the Planificacion, which fails.

        restPlanificacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planificacion)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlanificacions() throws Exception {
        // Initialize the database
        insertedPlanificacion = planificacionRepository.saveAndFlush(planificacion);

        // Get all the planificacionList
        restPlanificacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planificacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlanificacionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(planificacionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlanificacionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(planificacionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlanificacionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(planificacionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlanificacionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(planificacionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPlanificacion() throws Exception {
        // Initialize the database
        insertedPlanificacion = planificacionRepository.saveAndFlush(planificacion);

        // Get the planificacion
        restPlanificacionMockMvc
            .perform(get(ENTITY_API_URL_ID, planificacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(planificacion.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPlanificacion() throws Exception {
        // Get the planificacion
        restPlanificacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlanificacion() throws Exception {
        // Initialize the database
        insertedPlanificacion = planificacionRepository.saveAndFlush(planificacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planificacion
        Planificacion updatedPlanificacion = planificacionRepository.findById(planificacion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlanificacion are not directly saved in db
        em.detach(updatedPlanificacion);
        updatedPlanificacion.fecha(UPDATED_FECHA);

        restPlanificacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlanificacion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPlanificacion))
            )
            .andExpect(status().isOk());

        // Validate the Planificacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlanificacionToMatchAllProperties(updatedPlanificacion);
    }

    @Test
    @Transactional
    void putNonExistingPlanificacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planificacion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanificacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planificacion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planificacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planificacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlanificacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planificacion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanificacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planificacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planificacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlanificacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planificacion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanificacionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planificacion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Planificacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlanificacionWithPatch() throws Exception {
        // Initialize the database
        insertedPlanificacion = planificacionRepository.saveAndFlush(planificacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planificacion using partial update
        Planificacion partialUpdatedPlanificacion = new Planificacion();
        partialUpdatedPlanificacion.setId(planificacion.getId());

        restPlanificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanificacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlanificacion))
            )
            .andExpect(status().isOk());

        // Validate the Planificacion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlanificacionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPlanificacion, planificacion),
            getPersistedPlanificacion(planificacion)
        );
    }

    @Test
    @Transactional
    void fullUpdatePlanificacionWithPatch() throws Exception {
        // Initialize the database
        insertedPlanificacion = planificacionRepository.saveAndFlush(planificacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planificacion using partial update
        Planificacion partialUpdatedPlanificacion = new Planificacion();
        partialUpdatedPlanificacion.setId(planificacion.getId());

        partialUpdatedPlanificacion.fecha(UPDATED_FECHA);

        restPlanificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanificacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlanificacion))
            )
            .andExpect(status().isOk());

        // Validate the Planificacion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlanificacionUpdatableFieldsEquals(partialUpdatedPlanificacion, getPersistedPlanificacion(partialUpdatedPlanificacion));
    }

    @Test
    @Transactional
    void patchNonExistingPlanificacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planificacion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, planificacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planificacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planificacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlanificacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planificacion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planificacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planificacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlanificacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planificacion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanificacionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(planificacion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Planificacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlanificacion() throws Exception {
        // Initialize the database
        insertedPlanificacion = planificacionRepository.saveAndFlush(planificacion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the planificacion
        restPlanificacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, planificacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return planificacionRepository.count();
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

    protected Planificacion getPersistedPlanificacion(Planificacion planificacion) {
        return planificacionRepository.findById(planificacion.getId()).orElseThrow();
    }

    protected void assertPersistedPlanificacionToMatchAllProperties(Planificacion expectedPlanificacion) {
        assertPlanificacionAllPropertiesEquals(expectedPlanificacion, getPersistedPlanificacion(expectedPlanificacion));
    }

    protected void assertPersistedPlanificacionToMatchUpdatableProperties(Planificacion expectedPlanificacion) {
        assertPlanificacionAllUpdatablePropertiesEquals(expectedPlanificacion, getPersistedPlanificacion(expectedPlanificacion));
    }
}
