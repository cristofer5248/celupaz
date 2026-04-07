package com.chrisgeek.celupaz.service;

import com.chrisgeek.celupaz.domain.Alma;
import com.chrisgeek.celupaz.repository.AlmaRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.chrisgeek.celupaz.domain.Alma}.
 */
@Service
@Transactional
public class AlmaService {

    private static final Logger LOG = LoggerFactory.getLogger(AlmaService.class);

    private final AlmaRepository almaRepository;

    public AlmaService(AlmaRepository almaRepository) {
        this.almaRepository = almaRepository;
    }

    /**
     * Save a alma.
     *
     * @param alma the entity to save.
     * @return the persisted entity.
     */
    public Alma save(Alma alma) {
        LOG.debug("Request to save Alma : {}", alma);
        return almaRepository.save(alma);
    }

    /**
     * Update a alma.
     *
     * @param alma the entity to save.
     * @return the persisted entity.
     */
    public Alma update(Alma alma) {
        LOG.debug("Request to update Alma : {}", alma);
        return almaRepository.save(alma);
    }

    /**
     * Partially update a alma.
     *
     * @param alma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Alma> partialUpdate(Alma alma) {
        LOG.debug("Request to partially update Alma : {}", alma);

        return almaRepository
            .findById(alma.getId())
            .map(existingAlma -> {
                updateIfPresent(existingAlma::setName, alma.getName());
                updateIfPresent(existingAlma::setEmail, alma.getEmail());
                updateIfPresent(existingAlma::setPhone, alma.getPhone());
                updateIfPresent(existingAlma::setDepartment, alma.getDepartment());
                updateIfPresent(existingAlma::setMunicipality, alma.getMunicipality());
                updateIfPresent(existingAlma::setColony, alma.getColony());
                updateIfPresent(existingAlma::setDescription, alma.getDescription());
                updateIfPresent(existingAlma::setFoto, alma.getFoto());
                updateIfPresent(existingAlma::setFotoContentType, alma.getFotoContentType());

                return existingAlma;
            })
            .map(almaRepository::save);
    }

    /**
     * Get all the almas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Alma> findAll(Pageable pageable) {
        LOG.debug("Request to get all Almas");
        return almaRepository.findAll(pageable);
    }

    /**
     * Get one alma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Alma> findOne(Long id) {
        LOG.debug("Request to get Alma : {}", id);
        return almaRepository.findById(id);
    }

    /**
     * Delete the alma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Alma : {}", id);
        almaRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
