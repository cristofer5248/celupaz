package com.chrisgeek.celupaz.service;

import com.chrisgeek.celupaz.domain.Planificacion;
import com.chrisgeek.celupaz.repository.PlanificacionRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.chrisgeek.celupaz.domain.Planificacion}.
 */
@Service
@Transactional
public class PlanificacionService {

    private static final Logger LOG = LoggerFactory.getLogger(PlanificacionService.class);

    private final PlanificacionRepository planificacionRepository;

    public PlanificacionService(PlanificacionRepository planificacionRepository) {
        this.planificacionRepository = planificacionRepository;
    }

    /**
     * Save a planificacion.
     *
     * @param planificacion the entity to save.
     * @return the persisted entity.
     */
    public Planificacion save(Planificacion planificacion) {
        LOG.debug("Request to save Planificacion : {}", planificacion);
        return planificacionRepository.save(planificacion);
    }

    /**
     * Update a planificacion.
     *
     * @param planificacion the entity to save.
     * @return the persisted entity.
     */
    public Planificacion update(Planificacion planificacion) {
        LOG.debug("Request to update Planificacion : {}", planificacion);
        return planificacionRepository.save(planificacion);
    }

    /**
     * Partially update a planificacion.
     *
     * @param planificacion the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Planificacion> partialUpdate(Planificacion planificacion) {
        LOG.debug("Request to partially update Planificacion : {}", planificacion);

        return planificacionRepository
            .findById(planificacion.getId())
            .map(existingPlanificacion -> {
                updateIfPresent(existingPlanificacion::setFecha, planificacion.getFecha());

                return existingPlanificacion;
            })
            .map(planificacionRepository::save);
    }

    /**
     * Get all the planificacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Planificacion> findAll(Pageable pageable) {
        LOG.debug("Request to get all Planificacions");
        return planificacionRepository.findAll(pageable);
    }

    /**
     * Get all the planificacions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Planificacion> findAllWithEagerRelationships(Pageable pageable) {
        return planificacionRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one planificacion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Planificacion> findOne(Long id) {
        LOG.debug("Request to get Planificacion : {}", id);
        return planificacionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the planificacion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Planificacion : {}", id);
        planificacionRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
