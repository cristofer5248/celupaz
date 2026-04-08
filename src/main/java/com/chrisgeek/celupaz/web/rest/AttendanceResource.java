package com.chrisgeek.celupaz.web.rest;

import com.chrisgeek.celupaz.domain.Attendance;
import com.chrisgeek.celupaz.domain.AttendanceDTO;
import com.chrisgeek.celupaz.repository.AttendanceRepository;
import com.chrisgeek.celupaz.service.AttendanceService;
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
 * REST controller for managing {@link com.chrisgeek.celupaz.domain.Attendance}.
 */
@RestController
@RequestMapping("/api/attendances")
public class AttendanceResource {

    private static final Logger LOG = LoggerFactory.getLogger(AttendanceResource.class);

    private static final String ENTITY_NAME = "attendance";

    @Value("${jhipster.clientApp.name:celupazmaster}")
    private String applicationName;

    private final AttendanceService attendanceService;

    private final AttendanceRepository attendanceRepository;

    public AttendanceResource(AttendanceService attendanceService, AttendanceRepository attendanceRepository) {
        this.attendanceService = attendanceService;
        this.attendanceRepository = attendanceRepository;
    }

    /**
     * {@code POST  /attendances} : Create a new attendance.
     *
     * @param attendance the attendance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attendance, or with status {@code 400 (Bad Request)} if the attendance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Attendance> createAttendance(@Valid @RequestBody Attendance attendance) throws URISyntaxException {
        LOG.debug("REST request to save Attendance : {}", attendance);
        if (attendance.getId() != null) {
            throw new BadRequestAlertException("A new attendance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        attendance = attendanceService.save(attendance);
        return ResponseEntity.created(new URI("/api/attendances/" + attendance.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, attendance.getId().toString()))
            .body(attendance);
    }

    /**
     * {@code PUT  /attendances/:id} : Updates an existing attendance.
     *
     * @param id the id of the attendance to save.
     * @param attendance the attendance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attendance,
     * or with status {@code 400 (Bad Request)} if the attendance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attendance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Attendance> updateAttendance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Attendance attendance
    ) throws URISyntaxException {
        LOG.debug("REST request to update Attendance : {}, {}", id, attendance);
        if (attendance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attendance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attendanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        attendance = attendanceService.update(attendance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attendance.getId().toString()))
            .body(attendance);
    }

    /**
     * {@code PATCH  /attendances/:id} : Partial updates given fields of an existing attendance, field will ignore if it is null
     *
     * @param id the id of the attendance to save.
     * @param attendance the attendance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attendance,
     * or with status {@code 400 (Bad Request)} if the attendance is not valid,
     * or with status {@code 404 (Not Found)} if the attendance is not found,
     * or with status {@code 500 (Internal Server Error)} if the attendance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Attendance> partialUpdateAttendance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Attendance attendance
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Attendance partially : {}, {}", id, attendance);
        if (attendance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attendance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attendanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Attendance> result = attendanceService.partialUpdate(attendance);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attendance.getId().toString())
        );
    }

    /**
     * {@code GET  /attendances} : get all the Attendances.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Attendances in body.
     */
    @GetMapping("")
    //    public ResponseEntity<List<Attendance>> getAllAttendances(
    public ResponseEntity<List<AttendanceDTO>> getAllAttendances(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Attendances");
        Page<AttendanceDTO> page;
        if (eagerload) {
            //            page = attendanceService.findAllWithEagerRelationships(pageable);
            page = attendanceService.findAllAttendances(pageable);
        } else {
            //            page = attendanceService.findAll(pageable);
            page = attendanceService.findAllAttendances(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /attendances/:id} : get the "id" attendance.
     *
     * @param id the id of the attendance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attendance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendance(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Attendance : {}", id);
        Optional<Attendance> attendance = attendanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attendance);
    }

    /**
     * {@code DELETE  /attendances/:id} : delete the "id" attendance.
     *
     * @param id the id of the attendance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Attendance : {}", id);
        attendanceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
