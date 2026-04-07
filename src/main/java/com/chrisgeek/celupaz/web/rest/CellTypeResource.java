package com.chrisgeek.celupaz.web.rest;

import com.chrisgeek.celupaz.domain.CellType;
import com.chrisgeek.celupaz.repository.CellTypeRepository;
import com.chrisgeek.celupaz.service.CellTypeService;
import com.chrisgeek.celupaz.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.chrisgeek.celupaz.domain.CellType}.
 */
@RestController
@RequestMapping("/api/cell-types")
public class CellTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CellTypeResource.class);

    private static final String ENTITY_NAME = "cellType";

    @Value("${jhipster.clientApp.name:celupazmaster}")
    private String applicationName;

    private final CellTypeService cellTypeService;

    private final CellTypeRepository cellTypeRepository;

    public CellTypeResource(CellTypeService cellTypeService, CellTypeRepository cellTypeRepository) {
        this.cellTypeService = cellTypeService;
        this.cellTypeRepository = cellTypeRepository;
    }

    /**
     * {@code POST  /cell-types} : Create a new cellType.
     *
     * @param cellType the cellType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cellType, or with status {@code 400 (Bad Request)} if the cellType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CellType> createCellType(@Valid @RequestBody CellType cellType) throws URISyntaxException {
        LOG.debug("REST request to save CellType : {}", cellType);
        if (cellType.getId() != null) {
            throw new BadRequestAlertException("A new cellType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cellType = cellTypeService.save(cellType);
        return ResponseEntity.created(new URI("/api/cell-types/" + cellType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cellType.getId().toString()))
            .body(cellType);
    }

    /**
     * {@code PUT  /cell-types/:id} : Updates an existing cellType.
     *
     * @param id the id of the cellType to save.
     * @param cellType the cellType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cellType,
     * or with status {@code 400 (Bad Request)} if the cellType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cellType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CellType> updateCellType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CellType cellType
    ) throws URISyntaxException {
        LOG.debug("REST request to update CellType : {}, {}", id, cellType);
        if (cellType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cellType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cellTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cellType = cellTypeService.update(cellType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cellType.getId().toString()))
            .body(cellType);
    }

    /**
     * {@code PATCH  /cell-types/:id} : Partial updates given fields of an existing cellType, field will ignore if it is null
     *
     * @param id the id of the cellType to save.
     * @param cellType the cellType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cellType,
     * or with status {@code 400 (Bad Request)} if the cellType is not valid,
     * or with status {@code 404 (Not Found)} if the cellType is not found,
     * or with status {@code 500 (Internal Server Error)} if the cellType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CellType> partialUpdateCellType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CellType cellType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CellType partially : {}, {}", id, cellType);
        if (cellType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cellType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cellTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CellType> result = cellTypeService.partialUpdate(cellType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cellType.getId().toString())
        );
    }

    /**
     * {@code GET  /cell-types} : get all the Cell Types.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cell Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CellType>> getAllCellTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of CellTypes");
        Page<CellType> page = cellTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cell-types/:id} : get the "id" cellType.
     *
     * @param id the id of the cellType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cellType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CellType> getCellType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CellType : {}", id);
        Optional<CellType> cellType = cellTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cellType);
    }

    /**
     * {@code DELETE  /cell-types/:id} : delete the "id" cellType.
     *
     * @param id the id of the cellType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCellType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CellType : {}", id);
        cellTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
