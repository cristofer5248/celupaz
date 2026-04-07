package com.chrisgeek.celupaz.web.rest;

import static com.chrisgeek.celupaz.domain.PlaniMasterAsserts.*;
import static com.chrisgeek.celupaz.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chrisgeek.celupaz.IntegrationTest;
import com.chrisgeek.celupaz.domain.PlaniMaster;
import com.chrisgeek.celupaz.repository.PlaniMasterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PlaniMasterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlaniMasterResourceIT {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_OFRENDA = 1D;
    private static final Double UPDATED_OFRENDA = 2D;

    private static final Boolean DEFAULT_VISITA_CORDINADOR = false;
    private static final Boolean UPDATED_VISITA_CORDINADOR = true;

    private static final Boolean DEFAULT_VISITA_TUTOR = false;
    private static final Boolean UPDATED_VISITA_TUTOR = true;

    private static final Boolean DEFAULT_VISITA_DIRECTOR = false;
    private static final Boolean UPDATED_VISITA_DIRECTOR = true;

    private static final Boolean DEFAULT_OTRA_VISITA = false;
    private static final Boolean UPDATED_OTRA_VISITA = true;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_DONEBY = "AAAAAAAAAA";
    private static final String UPDATED_DONEBY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPLETADO = false;
    private static final Boolean UPDATED_COMPLETADO = true;

    private static final String ENTITY_API_URL = "/api/plani-masters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlaniMasterRepository planiMasterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlaniMasterMockMvc;

    private PlaniMaster planiMaster;

    private PlaniMaster insertedPlaniMaster;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlaniMaster createEntity() {
        return new PlaniMaster()
            .fecha(DEFAULT_FECHA)
            .ofrenda(DEFAULT_OFRENDA)
            .visitaCordinador(DEFAULT_VISITA_CORDINADOR)
            .visitaTutor(DEFAULT_VISITA_TUTOR)
            .visitaDirector(DEFAULT_VISITA_DIRECTOR)
            .otraVisita(DEFAULT_OTRA_VISITA)
            .note(DEFAULT_NOTE)
            .doneby(DEFAULT_DONEBY)
            .completado(DEFAULT_COMPLETADO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlaniMaster createUpdatedEntity() {
        return new PlaniMaster()
            .fecha(UPDATED_FECHA)
            .ofrenda(UPDATED_OFRENDA)
            .visitaCordinador(UPDATED_VISITA_CORDINADOR)
            .visitaTutor(UPDATED_VISITA_TUTOR)
            .visitaDirector(UPDATED_VISITA_DIRECTOR)
            .otraVisita(UPDATED_OTRA_VISITA)
            .note(UPDATED_NOTE)
            .doneby(UPDATED_DONEBY)
            .completado(UPDATED_COMPLETADO);
    }

    @BeforeEach
    void initTest() {
        planiMaster = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPlaniMaster != null) {
            planiMasterRepository.delete(insertedPlaniMaster);
            insertedPlaniMaster = null;
        }
    }

    @Test
    @Transactional
    void createPlaniMaster() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PlaniMaster
        var returnedPlaniMaster = om.readValue(
            restPlaniMasterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planiMaster)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PlaniMaster.class
        );

        // Validate the PlaniMaster in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPlaniMasterUpdatableFieldsEquals(returnedPlaniMaster, getPersistedPlaniMaster(returnedPlaniMaster));

        insertedPlaniMaster = returnedPlaniMaster;
    }

    @Test
    @Transactional
    void createPlaniMasterWithExistingId() throws Exception {
        // Create the PlaniMaster with an existing ID
        planiMaster.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaniMasterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planiMaster)))
            .andExpect(status().isBadRequest());

        // Validate the PlaniMaster in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        planiMaster.setFecha(null);

        // Create the PlaniMaster, which fails.

        restPlaniMasterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planiMaster)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlaniMasters() throws Exception {
        // Initialize the database
        insertedPlaniMaster = planiMasterRepository.saveAndFlush(planiMaster);

        // Get all the planiMasterList
        restPlaniMasterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planiMaster.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].ofrenda").value(hasItem(DEFAULT_OFRENDA)))
            .andExpect(jsonPath("$.[*].visitaCordinador").value(hasItem(DEFAULT_VISITA_CORDINADOR)))
            .andExpect(jsonPath("$.[*].visitaTutor").value(hasItem(DEFAULT_VISITA_TUTOR)))
            .andExpect(jsonPath("$.[*].visitaDirector").value(hasItem(DEFAULT_VISITA_DIRECTOR)))
            .andExpect(jsonPath("$.[*].otraVisita").value(hasItem(DEFAULT_OTRA_VISITA)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].doneby").value(hasItem(DEFAULT_DONEBY)))
            .andExpect(jsonPath("$.[*].completado").value(hasItem(DEFAULT_COMPLETADO)));
    }

    @Test
    @Transactional
    void getPlaniMaster() throws Exception {
        // Initialize the database
        insertedPlaniMaster = planiMasterRepository.saveAndFlush(planiMaster);

        // Get the planiMaster
        restPlaniMasterMockMvc
            .perform(get(ENTITY_API_URL_ID, planiMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(planiMaster.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.ofrenda").value(DEFAULT_OFRENDA))
            .andExpect(jsonPath("$.visitaCordinador").value(DEFAULT_VISITA_CORDINADOR))
            .andExpect(jsonPath("$.visitaTutor").value(DEFAULT_VISITA_TUTOR))
            .andExpect(jsonPath("$.visitaDirector").value(DEFAULT_VISITA_DIRECTOR))
            .andExpect(jsonPath("$.otraVisita").value(DEFAULT_OTRA_VISITA))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.doneby").value(DEFAULT_DONEBY))
            .andExpect(jsonPath("$.completado").value(DEFAULT_COMPLETADO));
    }

    @Test
    @Transactional
    void getNonExistingPlaniMaster() throws Exception {
        // Get the planiMaster
        restPlaniMasterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlaniMaster() throws Exception {
        // Initialize the database
        insertedPlaniMaster = planiMasterRepository.saveAndFlush(planiMaster);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planiMaster
        PlaniMaster updatedPlaniMaster = planiMasterRepository.findById(planiMaster.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlaniMaster are not directly saved in db
        em.detach(updatedPlaniMaster);
        updatedPlaniMaster
            .fecha(UPDATED_FECHA)
            .ofrenda(UPDATED_OFRENDA)
            .visitaCordinador(UPDATED_VISITA_CORDINADOR)
            .visitaTutor(UPDATED_VISITA_TUTOR)
            .visitaDirector(UPDATED_VISITA_DIRECTOR)
            .otraVisita(UPDATED_OTRA_VISITA)
            .note(UPDATED_NOTE)
            .doneby(UPDATED_DONEBY)
            .completado(UPDATED_COMPLETADO);

        restPlaniMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlaniMaster.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPlaniMaster))
            )
            .andExpect(status().isOk());

        // Validate the PlaniMaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlaniMasterToMatchAllProperties(updatedPlaniMaster);
    }

    @Test
    @Transactional
    void putNonExistingPlaniMaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planiMaster.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaniMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planiMaster.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planiMaster))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaniMaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlaniMaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planiMaster.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaniMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planiMaster))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaniMaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlaniMaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planiMaster.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaniMasterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planiMaster)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlaniMaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlaniMasterWithPatch() throws Exception {
        // Initialize the database
        insertedPlaniMaster = planiMasterRepository.saveAndFlush(planiMaster);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planiMaster using partial update
        PlaniMaster partialUpdatedPlaniMaster = new PlaniMaster();
        partialUpdatedPlaniMaster.setId(planiMaster.getId());

        partialUpdatedPlaniMaster
            .fecha(UPDATED_FECHA)
            .ofrenda(UPDATED_OFRENDA)
            .visitaTutor(UPDATED_VISITA_TUTOR)
            .visitaDirector(UPDATED_VISITA_DIRECTOR)
            .completado(UPDATED_COMPLETADO);

        restPlaniMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlaniMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlaniMaster))
            )
            .andExpect(status().isOk());

        // Validate the PlaniMaster in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlaniMasterUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPlaniMaster, planiMaster),
            getPersistedPlaniMaster(planiMaster)
        );
    }

    @Test
    @Transactional
    void fullUpdatePlaniMasterWithPatch() throws Exception {
        // Initialize the database
        insertedPlaniMaster = planiMasterRepository.saveAndFlush(planiMaster);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planiMaster using partial update
        PlaniMaster partialUpdatedPlaniMaster = new PlaniMaster();
        partialUpdatedPlaniMaster.setId(planiMaster.getId());

        partialUpdatedPlaniMaster
            .fecha(UPDATED_FECHA)
            .ofrenda(UPDATED_OFRENDA)
            .visitaCordinador(UPDATED_VISITA_CORDINADOR)
            .visitaTutor(UPDATED_VISITA_TUTOR)
            .visitaDirector(UPDATED_VISITA_DIRECTOR)
            .otraVisita(UPDATED_OTRA_VISITA)
            .note(UPDATED_NOTE)
            .doneby(UPDATED_DONEBY)
            .completado(UPDATED_COMPLETADO);

        restPlaniMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlaniMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlaniMaster))
            )
            .andExpect(status().isOk());

        // Validate the PlaniMaster in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlaniMasterUpdatableFieldsEquals(partialUpdatedPlaniMaster, getPersistedPlaniMaster(partialUpdatedPlaniMaster));
    }

    @Test
    @Transactional
    void patchNonExistingPlaniMaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planiMaster.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaniMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, planiMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planiMaster))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaniMaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlaniMaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planiMaster.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaniMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planiMaster))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaniMaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlaniMaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planiMaster.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaniMasterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(planiMaster)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlaniMaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlaniMaster() throws Exception {
        // Initialize the database
        insertedPlaniMaster = planiMasterRepository.saveAndFlush(planiMaster);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the planiMaster
        restPlaniMasterMockMvc
            .perform(delete(ENTITY_API_URL_ID, planiMaster.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return planiMasterRepository.count();
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

    protected PlaniMaster getPersistedPlaniMaster(PlaniMaster planiMaster) {
        return planiMasterRepository.findById(planiMaster.getId()).orElseThrow();
    }

    protected void assertPersistedPlaniMasterToMatchAllProperties(PlaniMaster expectedPlaniMaster) {
        assertPlaniMasterAllPropertiesEquals(expectedPlaniMaster, getPersistedPlaniMaster(expectedPlaniMaster));
    }

    protected void assertPersistedPlaniMasterToMatchUpdatableProperties(PlaniMaster expectedPlaniMaster) {
        assertPlaniMasterAllUpdatablePropertiesEquals(expectedPlaniMaster, getPersistedPlaniMaster(expectedPlaniMaster));
    }
}
