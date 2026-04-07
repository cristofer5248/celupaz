package com.chrisgeek.celupaz.service;

import com.chrisgeek.celupaz.domain.Attendance;
import com.chrisgeek.celupaz.repository.AttendanceRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.chrisgeek.celupaz.domain.Attendance}.
 */
@Service
@Transactional
public class AttendanceService {

    private static final Logger LOG = LoggerFactory.getLogger(AttendanceService.class);

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    /**
     * Save a attendance.
     *
     * @param attendance the entity to save.
     * @return the persisted entity.
     */
    public Attendance save(Attendance attendance) {
        LOG.debug("Request to save Attendance : {}", attendance);
        return attendanceRepository.save(attendance);
    }

    /**
     * Update a attendance.
     *
     * @param attendance the entity to save.
     * @return the persisted entity.
     */
    public Attendance update(Attendance attendance) {
        LOG.debug("Request to update Attendance : {}", attendance);
        return attendanceRepository.save(attendance);
    }

    /**
     * Partially update a attendance.
     *
     * @param attendance the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Attendance> partialUpdate(Attendance attendance) {
        LOG.debug("Request to partially update Attendance : {}", attendance);

        return attendanceRepository
            .findById(attendance.getId())
            .map(existingAttendance -> {
                updateIfPresent(existingAttendance::setFecha, attendance.getFecha());

                return existingAttendance;
            })
            .map(attendanceRepository::save);
    }

    /**
     * Get all the attendances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Attendance> findAll(Pageable pageable) {
        LOG.debug("Request to get all Attendances");
        return attendanceRepository.findAll(pageable);
    }

    /**
     * Get all the attendances with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Attendance> findAllWithEagerRelationships(Pageable pageable) {
        return attendanceRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one attendance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Attendance> findOne(Long id) {
        LOG.debug("Request to get Attendance : {}", id);
        return attendanceRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the attendance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Attendance : {}", id);
        attendanceRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
