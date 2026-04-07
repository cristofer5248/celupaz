package com.chrisgeek.celupaz.service;

import com.chrisgeek.celupaz.domain.PlaniMaster;
import com.chrisgeek.celupaz.repository.PlaniMasterRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.chrisgeek.celupaz.domain.PlaniMaster}.
 */
@Service
@Transactional
public class PlaniMasterService {

    private static final Logger LOG = LoggerFactory.getLogger(PlaniMasterService.class);

    private final PlaniMasterRepository planiMasterRepository;

    public PlaniMasterService(PlaniMasterRepository planiMasterRepository) {
        this.planiMasterRepository = planiMasterRepository;
    }

    /**
     * Save a planiMaster.
     *
     * @param planiMaster the entity to save.
     * @return the persisted entity.
     */
    public PlaniMaster save(PlaniMaster planiMaster) {
        LOG.debug("Request to save PlaniMaster : {}", planiMaster);
        return planiMasterRepository.save(planiMaster);
    }

    /**
     * Update a planiMaster.
     *
     * @param planiMaster the entity to save.
     * @return the persisted entity.
     */
    public PlaniMaster update(PlaniMaster planiMaster) {
        LOG.debug("Request to update PlaniMaster : {}", planiMaster);
        return planiMasterRepository.save(planiMaster);
    }

    /**
     * Partially update a planiMaster.
     *
     * @param planiMaster the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PlaniMaster> partialUpdate(PlaniMaster planiMaster) {
        LOG.debug("Request to partially update PlaniMaster : {}", planiMaster);

        return planiMasterRepository
            .findById(planiMaster.getId())
            .map(existingPlaniMaster -> {
                updateIfPresent(existingPlaniMaster::setFecha, planiMaster.getFecha());
                updateIfPresent(existingPlaniMaster::setOfrenda, planiMaster.getOfrenda());
                updateIfPresent(existingPlaniMaster::setVisitaCordinador, planiMaster.getVisitaCordinador());
                updateIfPresent(existingPlaniMaster::setVisitaTutor, planiMaster.getVisitaTutor());
                updateIfPresent(existingPlaniMaster::setVisitaDirector, planiMaster.getVisitaDirector());
                updateIfPresent(existingPlaniMaster::setOtraVisita, planiMaster.getOtraVisita());
                updateIfPresent(existingPlaniMaster::setNote, planiMaster.getNote());
                updateIfPresent(existingPlaniMaster::setDoneby, planiMaster.getDoneby());
                updateIfPresent(existingPlaniMaster::setCompletado, planiMaster.getCompletado());

                return existingPlaniMaster;
            })
            .map(planiMasterRepository::save);
    }

    /**
     * Get all the planiMasters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlaniMaster> findAll(Pageable pageable) {
        LOG.debug("Request to get all PlaniMasters");
        return planiMasterRepository.findAll(pageable);
    }

    /**
     * Get one planiMaster by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlaniMaster> findOne(Long id) {
        LOG.debug("Request to get PlaniMaster : {}", id);
        return planiMasterRepository.findById(id);
    }

    /**
     * Delete the planiMaster by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PlaniMaster : {}", id);
        planiMasterRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
