package com.chrisgeek.celupaz.web.rest;

import com.chrisgeek.celupaz.domain.PlaniMaster;
import com.chrisgeek.celupaz.repository.PlaniMasterRepository;
import com.chrisgeek.celupaz.service.PlaniMasterService;
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
 * REST controller for managing {@link com.chrisgeek.celupaz.domain.PlaniMaster}.
 */
@RestController
@RequestMapping("/api/plani-masters")
public class PlaniMasterResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlaniMasterResource.class);

    private static final String ENTITY_NAME = "planiMaster";

    @Value("${jhipster.clientApp.name:celupazmaster}")
    private String applicationName;

    private final PlaniMasterService planiMasterService;

    private final PlaniMasterRepository planiMasterRepository;

    public PlaniMasterResource(PlaniMasterService planiMasterService, PlaniMasterRepository planiMasterRepository) {
        this.planiMasterService = planiMasterService;
        this.planiMasterRepository = planiMasterRepository;
    }

    /**
     * {@code POST  /plani-masters} : Create a new planiMaster.
     *
     * @param planiMaster the planiMaster to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planiMaster, or with status {@code 400 (Bad Request)} if the planiMaster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PlaniMaster> createPlaniMaster(@Valid @RequestBody PlaniMaster planiMaster) throws URISyntaxException {
        LOG.debug("REST request to save PlaniMaster : {}", planiMaster);
        if (planiMaster.getId() != null) {
            throw new BadRequestAlertException("A new planiMaster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        planiMaster = planiMasterService.save(planiMaster);
        return ResponseEntity.created(new URI("/api/plani-masters/" + planiMaster.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, planiMaster.getId().toString()))
            .body(planiMaster);
    }

    /**
     * {@code PUT  /plani-masters/:id} : Updates an existing planiMaster.
     *
     * @param id the id of the planiMaster to save.
     * @param planiMaster the planiMaster to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planiMaster,
     * or with status {@code 400 (Bad Request)} if the planiMaster is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planiMaster couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlaniMaster> updatePlaniMaster(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlaniMaster planiMaster
    ) throws URISyntaxException {
        LOG.debug("REST request to update PlaniMaster : {}, {}", id, planiMaster);
        if (planiMaster.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planiMaster.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planiMasterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        planiMaster = planiMasterService.update(planiMaster);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planiMaster.getId().toString()))
            .body(planiMaster);
    }

    /**
     * {@code PATCH  /plani-masters/:id} : Partial updates given fields of an existing planiMaster, field will ignore if it is null
     *
     * @param id the id of the planiMaster to save.
     * @param planiMaster the planiMaster to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planiMaster,
     * or with status {@code 400 (Bad Request)} if the planiMaster is not valid,
     * or with status {@code 404 (Not Found)} if the planiMaster is not found,
     * or with status {@code 500 (Internal Server Error)} if the planiMaster couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlaniMaster> partialUpdatePlaniMaster(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlaniMaster planiMaster
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PlaniMaster partially : {}, {}", id, planiMaster);
        if (planiMaster.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planiMaster.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planiMasterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlaniMaster> result = planiMasterService.partialUpdate(planiMaster);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planiMaster.getId().toString())
        );
    }

    /**
     * {@code GET  /plani-masters} : get all the Plani Masters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Plani Masters in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PlaniMaster>> getAllPlaniMasters(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of PlaniMasters");
        Page<PlaniMaster> page = planiMasterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plani-masters/:id} : get the "id" planiMaster.
     *
     * @param id the id of the planiMaster to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planiMaster, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlaniMaster> getPlaniMaster(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PlaniMaster : {}", id);
        Optional<PlaniMaster> planiMaster = planiMasterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planiMaster);
    }

    /**
     * {@code DELETE  /plani-masters/:id} : delete the "id" planiMaster.
     *
     * @param id the id of the planiMaster to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaniMaster(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PlaniMaster : {}", id);
        planiMasterService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
