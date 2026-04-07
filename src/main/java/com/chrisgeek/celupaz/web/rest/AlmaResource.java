package com.chrisgeek.celupaz.web.rest;

import com.chrisgeek.celupaz.domain.Alma;
import com.chrisgeek.celupaz.repository.AlmaRepository;
import com.chrisgeek.celupaz.service.AlmaService;
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
 * REST controller for managing {@link com.chrisgeek.celupaz.domain.Alma}.
 */
@RestController
@RequestMapping("/api/almas")
public class AlmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlmaResource.class);

    private static final String ENTITY_NAME = "alma";

    @Value("${jhipster.clientApp.name:celupazmaster}")
    private String applicationName;

    private final AlmaService almaService;

    private final AlmaRepository almaRepository;

    public AlmaResource(AlmaService almaService, AlmaRepository almaRepository) {
        this.almaService = almaService;
        this.almaRepository = almaRepository;
    }

    /**
     * {@code POST  /almas} : Create a new alma.
     *
     * @param alma the alma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alma, or with status {@code 400 (Bad Request)} if the alma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Alma> createAlma(@Valid @RequestBody Alma alma) throws URISyntaxException {
        LOG.debug("REST request to save Alma : {}", alma);
        if (alma.getId() != null) {
            throw new BadRequestAlertException("A new alma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alma = almaService.save(alma);
        return ResponseEntity.created(new URI("/api/almas/" + alma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alma.getId().toString()))
            .body(alma);
    }

    /**
     * {@code PUT  /almas/:id} : Updates an existing alma.
     *
     * @param id the id of the alma to save.
     * @param alma the alma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alma,
     * or with status {@code 400 (Bad Request)} if the alma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Alma> updateAlma(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Alma alma)
        throws URISyntaxException {
        LOG.debug("REST request to update Alma : {}, {}", id, alma);
        if (alma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!almaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alma = almaService.update(alma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alma.getId().toString()))
            .body(alma);
    }

    /**
     * {@code PATCH  /almas/:id} : Partial updates given fields of an existing alma, field will ignore if it is null
     *
     * @param id the id of the alma to save.
     * @param alma the alma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alma,
     * or with status {@code 400 (Bad Request)} if the alma is not valid,
     * or with status {@code 404 (Not Found)} if the alma is not found,
     * or with status {@code 500 (Internal Server Error)} if the alma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Alma> partialUpdateAlma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Alma alma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Alma partially : {}, {}", id, alma);
        if (alma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!almaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Alma> result = almaService.partialUpdate(alma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alma.getId().toString())
        );
    }

    /**
     * {@code GET  /almas} : get all the Almas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Almas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Alma>> getAllAlmas(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Almas");
        Page<Alma> page = almaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /almas/:id} : get the "id" alma.
     *
     * @param id the id of the alma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Alma> getAlma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Alma : {}", id);
        Optional<Alma> alma = almaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alma);
    }

    /**
     * {@code DELETE  /almas/:id} : delete the "id" alma.
     *
     * @param id the id of the alma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Alma : {}", id);
        almaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
