package com.chrisgeek.celupaz.service;

import com.chrisgeek.celupaz.domain.RolCelula;
import com.chrisgeek.celupaz.repository.RolCelulaRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.chrisgeek.celupaz.domain.RolCelula}.
 */
@Service
@Transactional
public class RolCelulaService {

    private static final Logger LOG = LoggerFactory.getLogger(RolCelulaService.class);

    private final RolCelulaRepository rolCelulaRepository;

    public RolCelulaService(RolCelulaRepository rolCelulaRepository) {
        this.rolCelulaRepository = rolCelulaRepository;
    }

    /**
     * Save a rolCelula.
     *
     * @param rolCelula the entity to save.
     * @return the persisted entity.
     */
    public RolCelula save(RolCelula rolCelula) {
        LOG.debug("Request to save RolCelula : {}", rolCelula);
        return rolCelulaRepository.save(rolCelula);
    }

    /**
     * Update a rolCelula.
     *
     * @param rolCelula the entity to save.
     * @return the persisted entity.
     */
    public RolCelula update(RolCelula rolCelula) {
        LOG.debug("Request to update RolCelula : {}", rolCelula);
        return rolCelulaRepository.save(rolCelula);
    }

    /**
     * Partially update a rolCelula.
     *
     * @param rolCelula the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RolCelula> partialUpdate(RolCelula rolCelula) {
        LOG.debug("Request to partially update RolCelula : {}", rolCelula);

        return rolCelulaRepository
            .findById(rolCelula.getId())
            .map(existingRolCelula -> {
                updateIfPresent(existingRolCelula::setName, rolCelula.getName());

                return existingRolCelula;
            })
            .map(rolCelulaRepository::save);
    }

    /**
     * Get all the rolCelulas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RolCelula> findAll() {
        LOG.debug("Request to get all RolCelulas");
        return rolCelulaRepository.findAll();
    }

    /**
     * Get one rolCelula by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RolCelula> findOne(Long id) {
        LOG.debug("Request to get RolCelula : {}", id);
        return rolCelulaRepository.findById(id);
    }

    /**
     * Delete the rolCelula by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RolCelula : {}", id);
        rolCelulaRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
