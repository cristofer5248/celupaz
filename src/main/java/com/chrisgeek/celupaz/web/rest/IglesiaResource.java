package com.chrisgeek.celupaz.web.rest;

import com.chrisgeek.celupaz.domain.Iglesia;
import com.chrisgeek.celupaz.repository.IglesiaRepository;
import com.chrisgeek.celupaz.service.IglesiaService;
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
 * REST controller for managing {@link com.chrisgeek.celupaz.domain.Iglesia}.
 */
@RestController
@RequestMapping("/api/iglesias")
public class IglesiaResource {

    private static final Logger LOG = LoggerFactory.getLogger(IglesiaResource.class);

    private static final String ENTITY_NAME = "iglesia";

    @Value("${jhipster.clientApp.name:celupazmaster}")
    private String applicationName;

    private final IglesiaService iglesiaService;

    private final IglesiaRepository iglesiaRepository;

    public IglesiaResource(IglesiaService iglesiaService, IglesiaRepository iglesiaRepository) {
        this.iglesiaService = iglesiaService;
        this.iglesiaRepository = iglesiaRepository;
    }

    /**
     * {@code POST  /iglesias} : Create a new iglesia.
     *
     * @param iglesia the iglesia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iglesia, or with status {@code 400 (Bad Request)} if the iglesia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Iglesia> createIglesia(@Valid @RequestBody Iglesia iglesia) throws URISyntaxException {
        LOG.debug("REST request to save Iglesia : {}", iglesia);
        if (iglesia.getId() != null) {
            throw new BadRequestAlertException("A new iglesia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        iglesia = iglesiaService.save(iglesia);
        return ResponseEntity.created(new URI("/api/iglesias/" + iglesia.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, iglesia.getId().toString()))
            .body(iglesia);
    }

    /**
     * {@code PUT  /iglesias/:id} : Updates an existing iglesia.
     *
     * @param id the id of the iglesia to save.
     * @param iglesia the iglesia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iglesia,
     * or with status {@code 400 (Bad Request)} if the iglesia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iglesia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Iglesia> updateIglesia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Iglesia iglesia
    ) throws URISyntaxException {
        LOG.debug("REST request to update Iglesia : {}, {}", id, iglesia);
        if (iglesia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iglesia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iglesiaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        iglesia = iglesiaService.update(iglesia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iglesia.getId().toString()))
            .body(iglesia);
    }

    /**
     * {@code PATCH  /iglesias/:id} : Partial updates given fields of an existing iglesia, field will ignore if it is null
     *
     * @param id the id of the iglesia to save.
     * @param iglesia the iglesia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iglesia,
     * or with status {@code 400 (Bad Request)} if the iglesia is not valid,
     * or with status {@code 404 (Not Found)} if the iglesia is not found,
     * or with status {@code 500 (Internal Server Error)} if the iglesia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Iglesia> partialUpdateIglesia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Iglesia iglesia
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Iglesia partially : {}, {}", id, iglesia);
        if (iglesia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iglesia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iglesiaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Iglesia> result = iglesiaService.partialUpdate(iglesia);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iglesia.getId().toString())
        );
    }

    /**
     * {@code GET  /iglesias} : get all the Iglesias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Iglesias in body.
     */
    @GetMapping("")
    public List<Iglesia> getAllIglesias() {
        LOG.debug("REST request to get all Iglesias");
        return iglesiaService.findAll();
    }

    /**
     * {@code GET  /iglesias/:id} : get the "id" iglesia.
     *
     * @param id the id of the iglesia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iglesia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Iglesia> getIglesia(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Iglesia : {}", id);
        Optional<Iglesia> iglesia = iglesiaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iglesia);
    }

    /**
     * {@code DELETE  /iglesias/:id} : delete the "id" iglesia.
     *
     * @param id the id of the iglesia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIglesia(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Iglesia : {}", id);
        iglesiaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
