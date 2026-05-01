package com.chrisgeek.celupaz.web.rest;

import com.chrisgeek.celupaz.domain.Planificacion;
import com.chrisgeek.celupaz.repository.PlanificacionRepository;
import com.chrisgeek.celupaz.service.PlanificacionService;
import com.chrisgeek.celupaz.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
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
 * REST controller for managing {@link com.chrisgeek.celupaz.domain.Planificacion}.
 */
@RestController
@RequestMapping("/api/planificacions")
public class PlanificacionResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlanificacionResource.class);

    private static final String ENTITY_NAME = "planificacion";

    @Value("${jhipster.clientApp.name:celupazmaster}")
    private String applicationName;

    private final PlanificacionService planificacionService;

    private final PlanificacionRepository planificacionRepository;

    public PlanificacionResource(PlanificacionService planificacionService, PlanificacionRepository planificacionRepository) {
        this.planificacionService = planificacionService;
        this.planificacionRepository = planificacionRepository;
    }

    /**
     * {@code POST  /planificacions} : Create a new planificacion.
     *
     * @param planificacion the planificacion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planificacion, or with status {@code 400 (Bad Request)} if the planificacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Planificacion> createPlanificacion(@Valid @RequestBody Planificacion planificacion) throws URISyntaxException {
        LOG.debug("REST request to save Planificacion : {}", planificacion);
        if (planificacion.getId() != null) {
            throw new BadRequestAlertException("A new planificacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        planificacion.setFecha(LocalDate.now());
        planificacion = planificacionService.save(planificacion);
        return ResponseEntity.created(new URI("/api/planificacions/" + planificacion.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, planificacion.getId().toString()))
            .body(planificacion);
    }

    /**
     * {@code PUT  /planificacions/:id} : Updates an existing planificacion.
     *
     * @param id the id of the planificacion to save.
     * @param planificacion the planificacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planificacion,
     * or with status {@code 400 (Bad Request)} if the planificacion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planificacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Planificacion> updatePlanificacion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Planificacion planificacion
    ) throws URISyntaxException {
        LOG.debug("REST request to update Planificacion : {}, {}", id, planificacion);
        if (planificacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planificacion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planificacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        planificacion = planificacionService.update(planificacion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planificacion.getId().toString()))
            .body(planificacion);
    }

    /**
     * {@code PATCH  /planificacions/:id} : Partial updates given fields of an existing planificacion, field will ignore if it is null
     *
     * @param id the id of the planificacion to save.
     * @param planificacion the planificacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planificacion,
     * or with status {@code 400 (Bad Request)} if the planificacion is not valid,
     * or with status {@code 404 (Not Found)} if the planificacion is not found,
     * or with status {@code 500 (Internal Server Error)} if the planificacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Planificacion> partialUpdatePlanificacion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Planificacion planificacion
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Planificacion partially : {}, {}", id, planificacion);
        if (planificacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planificacion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planificacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Planificacion> result = planificacionService.partialUpdate(planificacion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planificacion.getId().toString())
        );
    }

    /**
     * {@code GET  /planificacions} : get all the Planificacions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Planificacions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Planificacion>> getAllPlanificacions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Planificacions");
        Page<Planificacion> page;
        if (eagerload) {
            LOG.debug("ENTRO A getAllPlanificacions findAllWithEagerRelationships");
            page = planificacionService.findAllWithEagerRelationships(pageable);
        } else {
            LOG.debug("ENTRO A  getAllPlanificacions findAll");
            page = planificacionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /planificacions/:id} : get the "id" planificacion.
     *
     * @param id the id of the planificacion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planificacion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Planificacion> getPlanificacion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Planificacion : {}", id);
        Optional<Planificacion> planificacion = planificacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planificacion);
    }

    /**
     * {@code DELETE  /planificacions/:id} : delete the "id" planificacion.
     *
     * @param id the id of the planificacion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanificacion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Planificacion : {}", id);
        planificacionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
