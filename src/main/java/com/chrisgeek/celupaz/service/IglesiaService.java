package com.chrisgeek.celupaz.service;

import com.chrisgeek.celupaz.domain.Iglesia;
import com.chrisgeek.celupaz.repository.IglesiaRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.chrisgeek.celupaz.domain.Iglesia}.
 */
@Service
@Transactional
public class IglesiaService {

    private static final Logger LOG = LoggerFactory.getLogger(IglesiaService.class);

    private final IglesiaRepository iglesiaRepository;

    public IglesiaService(IglesiaRepository iglesiaRepository) {
        this.iglesiaRepository = iglesiaRepository;
    }

    /**
     * Save a iglesia.
     *
     * @param iglesia the entity to save.
     * @return the persisted entity.
     */
    public Iglesia save(Iglesia iglesia) {
        LOG.debug("Request to save Iglesia : {}", iglesia);
        return iglesiaRepository.save(iglesia);
    }

    /**
     * Update a iglesia.
     *
     * @param iglesia the entity to save.
     * @return the persisted entity.
     */
    public Iglesia update(Iglesia iglesia) {
        LOG.debug("Request to update Iglesia : {}", iglesia);
        return iglesiaRepository.save(iglesia);
    }

    /**
     * Partially update a iglesia.
     *
     * @param iglesia the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Iglesia> partialUpdate(Iglesia iglesia) {
        LOG.debug("Request to partially update Iglesia : {}", iglesia);

        return iglesiaRepository
            .findById(iglesia.getId())
            .map(existingIglesia -> {
                updateIfPresent(existingIglesia::setName, iglesia.getName());

                return existingIglesia;
            })
            .map(iglesiaRepository::save);
    }

    /**
     * Get all the iglesias.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Iglesia> findAll() {
        LOG.debug("Request to get all Iglesias");
        return iglesiaRepository.findAll();
    }

    /**
     * Get one iglesia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Iglesia> findOne(Long id) {
        LOG.debug("Request to get Iglesia : {}", id);
        return iglesiaRepository.findById(id);
    }

    /**
     * Delete the iglesia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Iglesia : {}", id);
        iglesiaRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
