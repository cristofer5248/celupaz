package com.chrisgeek.celupaz.web.rest;

import com.chrisgeek.celupaz.domain.RolCelula;
import com.chrisgeek.celupaz.repository.RolCelulaRepository;
import com.chrisgeek.celupaz.service.RolCelulaService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.chrisgeek.celupaz.domain.RolCelula}.
 */
@RestController
@RequestMapping("/api/rol-celulas")
public class RolCelulaResource {

    private static final Logger LOG = LoggerFactory.getLogger(RolCelulaResource.class);

    private static final String ENTITY_NAME = "rolCelula";

    @Value("${jhipster.clientApp.name:celupazmaster}")
    private String applicationName;

    private final RolCelulaService rolCelulaService;

    private final RolCelulaRepository rolCelulaRepository;

    public RolCelulaResource(RolCelulaService rolCelulaService, RolCelulaRepository rolCelulaRepository) {
        this.rolCelulaService = rolCelulaService;
        this.rolCelulaRepository = rolCelulaRepository;
    }

    /**
     * {@code POST  /rol-celulas} : Create a new rolCelula.
     *
     * @param rolCelula the rolCelula to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rolCelula, or with status {@code 400 (Bad Request)} if the rolCelula has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RolCelula> createRolCelula(@Valid @RequestBody RolCelula rolCelula) throws URISyntaxException {
        LOG.debug("REST request to save RolCelula : {}", rolCelula);
        if (rolCelula.getId() != null) {
            throw new BadRequestAlertException("A new rolCelula cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rolCelula = rolCelulaService.save(rolCelula);
        return ResponseEntity.created(new URI("/api/rol-celulas/" + rolCelula.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, rolCelula.getId().toString()))
            .body(rolCelula);
    }

    /**
     * {@code PUT  /rol-celulas/:id} : Updates an existing rolCelula.
     *
     * @param id the id of the rolCelula to save.
     * @param rolCelula the rolCelula to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rolCelula,
     * or with status {@code 400 (Bad Request)} if the rolCelula is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rolCelula couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RolCelula> updateRolCelula(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RolCelula rolCelula
    ) throws URISyntaxException {
        LOG.debug("REST request to update RolCelula : {}, {}", id, rolCelula);
        if (rolCelula.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rolCelula.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rolCelulaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rolCelula = rolCelulaService.update(rolCelula);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rolCelula.getId().toString()))
            .body(rolCelula);
    }

    /**
     * {@code PATCH  /rol-celulas/:id} : Partial updates given fields of an existing rolCelula, field will ignore if it is null
     *
     * @param id the id of the rolCelula to save.
     * @param rolCelula the rolCelula to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rolCelula,
     * or with status {@code 400 (Bad Request)} if the rolCelula is not valid,
     * or with status {@code 404 (Not Found)} if the rolCelula is not found,
     * or with status {@code 500 (Internal Server Error)} if the rolCelula couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RolCelula> partialUpdateRolCelula(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RolCelula rolCelula
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RolCelula partially : {}, {}", id, rolCelula);
        if (rolCelula.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rolCelula.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rolCelulaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RolCelula> result = rolCelulaService.partialUpdate(rolCelula);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rolCelula.getId().toString())
        );
    }

    /**
     * {@code GET  /rol-celulas} : get all the Rol Celulas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Rol Celulas in body.
     */
    @GetMapping("")
    public List<RolCelula> getAllRolCelulas() {
        LOG.debug("REST request to get all RolCelulas");
        return rolCelulaService.findAll();
    }

    /**
     * {@code GET  /rol-celulas/:id} : get the "id" rolCelula.
     *
     * @param id the id of the rolCelula to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rolCelula, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RolCelula> getRolCelula(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RolCelula : {}", id);
        Optional<RolCelula> rolCelula = rolCelulaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rolCelula);
    }

    /**
     * {@code DELETE  /rol-celulas/:id} : delete the "id" rolCelula.
     *
     * @param id the id of the rolCelula to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRolCelula(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RolCelula : {}", id);
        rolCelulaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
