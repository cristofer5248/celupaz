package com.chrisgeek.celupaz.web.rest;

import com.chrisgeek.celupaz.domain.Cell;
import com.chrisgeek.celupaz.repository.CellRepository;
import com.chrisgeek.celupaz.service.CellService;
import com.chrisgeek.celupaz.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.chrisgeek.celupaz.domain.Cell}.
 */
@RestController
@RequestMapping("/api/cells")
public class CellResource {

    private static final Logger LOG = LoggerFactory.getLogger(CellResource.class);

    private static final String ENTITY_NAME = "cell";

    @Value("${jhipster.clientApp.name:celupazmaster}")
    private String applicationName;

    private final CellService cellService;

    private final CellRepository cellRepository;

    public CellResource(CellService cellService, CellRepository cellRepository) {
        this.cellService = cellService;
        this.cellRepository = cellRepository;
    }

    /**
     * {@code POST  /cells} : Create a new cell.
     *
     * @param cell the cell to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cell, or with status {@code 400 (Bad Request)} if the cell has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Cell> createCell(@RequestBody Cell cell) throws URISyntaxException {
        LOG.debug("REST request to save Cell : {}", cell);
        if (cell.getId() != null) {
            throw new BadRequestAlertException("A new cell cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cell = cellService.save(cell);
        return ResponseEntity.created(new URI("/api/cells/" + cell.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cell.getId().toString()))
            .body(cell);
    }

    /**
     * {@code PUT  /cells/:id} : Updates an existing cell.
     *
     * @param id the id of the cell to save.
     * @param cell the cell to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cell,
     * or with status {@code 400 (Bad Request)} if the cell is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cell couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cell> updateCell(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cell cell)
        throws URISyntaxException {
        LOG.debug("REST request to update Cell : {}, {}", id, cell);
        if (cell.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cell.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cellRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cell = cellService.update(cell);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cell.getId().toString()))
            .body(cell);
    }

    /**
     * {@code PATCH  /cells/:id} : Partial updates given fields of an existing cell, field will ignore if it is null
     *
     * @param id the id of the cell to save.
     * @param cell the cell to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cell,
     * or with status {@code 400 (Bad Request)} if the cell is not valid,
     * or with status {@code 404 (Not Found)} if the cell is not found,
     * or with status {@code 500 (Internal Server Error)} if the cell couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cell> partialUpdateCell(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cell cell)
        throws URISyntaxException {
        LOG.debug("REST request to partial update Cell partially : {}, {}", id, cell);
        if (cell.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cell.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cellRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cell> result = cellService.partialUpdate(cell);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cell.getId().toString())
        );
    }

    /**
     * {@code GET  /cells} : get all the Cells.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cells in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Cell>> getAllCells(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Cells");
        Page<Cell> page;
        if (eagerload) {
            page = cellService.findAllWithEagerRelationships(pageable);
        } else {
            page = cellService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cells/:id} : get the "id" cell.
     *
     * @param id the id of the cell to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cell, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cell> getCell(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Cell : {}", id);
        Optional<Cell> cell = cellService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cell);
    }

    /**
     * {@code DELETE  /cells/:id} : delete the "id" cell.
     *
     * @param id the id of the cell to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCell(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Cell : {}", id);
        cellService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
