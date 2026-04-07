package com.chrisgeek.celupaz.web.rest;

import static com.chrisgeek.celupaz.domain.CellAsserts.*;
import static com.chrisgeek.celupaz.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chrisgeek.celupaz.IntegrationTest;
import com.chrisgeek.celupaz.domain.Cell;
import com.chrisgeek.celupaz.repository.CellRepository;
import com.chrisgeek.celupaz.service.CellService;
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
 * Integration tests for the {@link CellResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CellResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SECTOR = 1;
    private static final Integer UPDATED_SECTOR = 2;

    private static final String DEFAULT_LIDER = "AAAAAAAAAA";
    private static final String UPDATED_LIDER = "BBBBBBBBBB";

    private static final String DEFAULT_CORDINADOR = "AAAAAAAAAA";
    private static final String UPDATED_CORDINADOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cells";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CellRepository cellRepository;

    @Mock
    private CellRepository cellRepositoryMock;

    @Mock
    private CellService cellServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCellMockMvc;

    private Cell cell;

    private Cell insertedCell;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cell createEntity() {
        return new Cell()
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .description(DEFAULT_DESCRIPTION)
            .sector(DEFAULT_SECTOR)
            .lider(DEFAULT_LIDER)
            .cordinador(DEFAULT_CORDINADOR);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cell createUpdatedEntity() {
        return new Cell()
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .description(UPDATED_DESCRIPTION)
            .sector(UPDATED_SECTOR)
            .lider(UPDATED_LIDER)
            .cordinador(UPDATED_CORDINADOR);
    }

    @BeforeEach
    void initTest() {
        cell = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCell != null) {
            cellRepository.delete(insertedCell);
            insertedCell = null;
        }
    }

    @Test
    @Transactional
    void createCell() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cell
        var returnedCell = om.readValue(
            restCellMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cell)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Cell.class
        );

        // Validate the Cell in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCellUpdatableFieldsEquals(returnedCell, getPersistedCell(returnedCell));

        insertedCell = returnedCell;
    }

    @Test
    @Transactional
    void createCellWithExistingId() throws Exception {
        // Create the Cell with an existing ID
        cell.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCellMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cell)))
            .andExpect(status().isBadRequest());

        // Validate the Cell in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCells() throws Exception {
        // Initialize the database
        insertedCell = cellRepository.saveAndFlush(cell);

        // Get all the cellList
        restCellMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cell.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sector").value(hasItem(DEFAULT_SECTOR)))
            .andExpect(jsonPath("$.[*].lider").value(hasItem(DEFAULT_LIDER)))
            .andExpect(jsonPath("$.[*].cordinador").value(hasItem(DEFAULT_CORDINADOR)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCellsWithEagerRelationshipsIsEnabled() throws Exception {
        when(cellServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCellMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cellServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCellsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cellServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCellMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cellRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCell() throws Exception {
        // Initialize the database
        insertedCell = cellRepository.saveAndFlush(cell);

        // Get the cell
        restCellMockMvc
            .perform(get(ENTITY_API_URL_ID, cell.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cell.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.sector").value(DEFAULT_SECTOR))
            .andExpect(jsonPath("$.lider").value(DEFAULT_LIDER))
            .andExpect(jsonPath("$.cordinador").value(DEFAULT_CORDINADOR));
    }

    @Test
    @Transactional
    void getNonExistingCell() throws Exception {
        // Get the cell
        restCellMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCell() throws Exception {
        // Initialize the database
        insertedCell = cellRepository.saveAndFlush(cell);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cell
        Cell updatedCell = cellRepository.findById(cell.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCell are not directly saved in db
        em.detach(updatedCell);
        updatedCell
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .description(UPDATED_DESCRIPTION)
            .sector(UPDATED_SECTOR)
            .lider(UPDATED_LIDER)
            .cordinador(UPDATED_CORDINADOR);

        restCellMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCell.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCell))
            )
            .andExpect(status().isOk());

        // Validate the Cell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCellToMatchAllProperties(updatedCell);
    }

    @Test
    @Transactional
    void putNonExistingCell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cell.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCellMockMvc
            .perform(put(ENTITY_API_URL_ID, cell.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cell)))
            .andExpect(status().isBadRequest());

        // Validate the Cell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cell.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCellMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cell))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cell.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCellMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cell)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCellWithPatch() throws Exception {
        // Initialize the database
        insertedCell = cellRepository.saveAndFlush(cell);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cell using partial update
        Cell partialUpdatedCell = new Cell();
        partialUpdatedCell.setId(cell.getId());

        partialUpdatedCell.name(UPDATED_NAME).startDate(UPDATED_START_DATE).description(UPDATED_DESCRIPTION).lider(UPDATED_LIDER);

        restCellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCell.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCell))
            )
            .andExpect(status().isOk());

        // Validate the Cell in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCellUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCell, cell), getPersistedCell(cell));
    }

    @Test
    @Transactional
    void fullUpdateCellWithPatch() throws Exception {
        // Initialize the database
        insertedCell = cellRepository.saveAndFlush(cell);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cell using partial update
        Cell partialUpdatedCell = new Cell();
        partialUpdatedCell.setId(cell.getId());

        partialUpdatedCell
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .description(UPDATED_DESCRIPTION)
            .sector(UPDATED_SECTOR)
            .lider(UPDATED_LIDER)
            .cordinador(UPDATED_CORDINADOR);

        restCellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCell.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCell))
            )
            .andExpect(status().isOk());

        // Validate the Cell in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCellUpdatableFieldsEquals(partialUpdatedCell, getPersistedCell(partialUpdatedCell));
    }

    @Test
    @Transactional
    void patchNonExistingCell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cell.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCellMockMvc
            .perform(patch(ENTITY_API_URL_ID, cell.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cell)))
            .andExpect(status().isBadRequest());

        // Validate the Cell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cell.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cell))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cell.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCellMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cell)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCell() throws Exception {
        // Initialize the database
        insertedCell = cellRepository.saveAndFlush(cell);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cell
        restCellMockMvc
            .perform(delete(ENTITY_API_URL_ID, cell.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cellRepository.count();
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

    protected Cell getPersistedCell(Cell cell) {
        return cellRepository.findById(cell.getId()).orElseThrow();
    }

    protected void assertPersistedCellToMatchAllProperties(Cell expectedCell) {
        assertCellAllPropertiesEquals(expectedCell, getPersistedCell(expectedCell));
    }

    protected void assertPersistedCellToMatchUpdatableProperties(Cell expectedCell) {
        assertCellAllUpdatablePropertiesEquals(expectedCell, getPersistedCell(expectedCell));
    }
}
