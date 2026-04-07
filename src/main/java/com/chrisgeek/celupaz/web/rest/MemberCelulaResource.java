package com.chrisgeek.celupaz.web.rest;

import com.chrisgeek.celupaz.domain.MemberCelula;
import com.chrisgeek.celupaz.repository.MemberCelulaRepository;
import com.chrisgeek.celupaz.service.MemberCelulaService;
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
 * REST controller for managing {@link com.chrisgeek.celupaz.domain.MemberCelula}.
 */
@RestController
@RequestMapping("/api/member-celulas")
public class MemberCelulaResource {

    private static final Logger LOG = LoggerFactory.getLogger(MemberCelulaResource.class);

    private static final String ENTITY_NAME = "memberCelula";

    @Value("${jhipster.clientApp.name:celupazmaster}")
    private String applicationName;

    private final MemberCelulaService memberCelulaService;

    private final MemberCelulaRepository memberCelulaRepository;

    public MemberCelulaResource(MemberCelulaService memberCelulaService, MemberCelulaRepository memberCelulaRepository) {
        this.memberCelulaService = memberCelulaService;
        this.memberCelulaRepository = memberCelulaRepository;
    }

    /**
     * {@code POST  /member-celulas} : Create a new memberCelula.
     *
     * @param memberCelula the memberCelula to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memberCelula, or with status {@code 400 (Bad Request)} if the memberCelula has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MemberCelula> createMemberCelula(@Valid @RequestBody MemberCelula memberCelula) throws URISyntaxException {
        LOG.debug("REST request to save MemberCelula : {}", memberCelula);
        if (memberCelula.getId() != null) {
            throw new BadRequestAlertException("A new memberCelula cannot already have an ID", ENTITY_NAME, "idexists");
        }
        memberCelula = memberCelulaService.save(memberCelula);
        return ResponseEntity.created(new URI("/api/member-celulas/" + memberCelula.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, memberCelula.getId().toString()))
            .body(memberCelula);
    }

    /**
     * {@code PUT  /member-celulas/:id} : Updates an existing memberCelula.
     *
     * @param id the id of the memberCelula to save.
     * @param memberCelula the memberCelula to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberCelula,
     * or with status {@code 400 (Bad Request)} if the memberCelula is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memberCelula couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MemberCelula> updateMemberCelula(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MemberCelula memberCelula
    ) throws URISyntaxException {
        LOG.debug("REST request to update MemberCelula : {}, {}", id, memberCelula);
        if (memberCelula.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memberCelula.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memberCelulaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        memberCelula = memberCelulaService.update(memberCelula);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memberCelula.getId().toString()))
            .body(memberCelula);
    }

    /**
     * {@code PATCH  /member-celulas/:id} : Partial updates given fields of an existing memberCelula, field will ignore if it is null
     *
     * @param id the id of the memberCelula to save.
     * @param memberCelula the memberCelula to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberCelula,
     * or with status {@code 400 (Bad Request)} if the memberCelula is not valid,
     * or with status {@code 404 (Not Found)} if the memberCelula is not found,
     * or with status {@code 500 (Internal Server Error)} if the memberCelula couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MemberCelula> partialUpdateMemberCelula(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MemberCelula memberCelula
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MemberCelula partially : {}, {}", id, memberCelula);
        if (memberCelula.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memberCelula.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memberCelulaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MemberCelula> result = memberCelulaService.partialUpdate(memberCelula);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memberCelula.getId().toString())
        );
    }

    /**
     * {@code GET  /member-celulas} : get all the Member Celulas.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Member Celulas in body.
     */
    @GetMapping("")
    public List<MemberCelula> getAllMemberCelulas(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all MemberCelulas");
        return memberCelulaService.findAll();
    }

    /**
     * {@code GET  /member-celulas/:id} : get the "id" memberCelula.
     *
     * @param id the id of the memberCelula to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memberCelula, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MemberCelula> getMemberCelula(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MemberCelula : {}", id);
        Optional<MemberCelula> memberCelula = memberCelulaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(memberCelula);
    }

    /**
     * {@code DELETE  /member-celulas/:id} : delete the "id" memberCelula.
     *
     * @param id the id of the memberCelula to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemberCelula(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MemberCelula : {}", id);
        memberCelulaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
