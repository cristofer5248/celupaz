package com.chrisgeek.celupaz.service;

import com.chrisgeek.celupaz.domain.AlmaHistory;
import com.chrisgeek.celupaz.repository.AlmaHistoryRepository;
import com.chrisgeek.celupaz.security.AuthoritiesConstants;
import com.chrisgeek.celupaz.security.SecurityUtils;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.chrisgeek.celupaz.domain.AlmaHistory}.
 */
@Service
@Transactional
public class AlmaHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(AlmaHistoryService.class);

    private final AlmaHistoryRepository almaHistoryRepository;

    public AlmaHistoryService(AlmaHistoryRepository almaHistoryRepository) {
        this.almaHistoryRepository = almaHistoryRepository;
    }

    /**
     * Save a almaHistory.
     *
     * @param almaHistory the entity to save.
     * @return the persisted entity.
     */
    public AlmaHistory save(AlmaHistory almaHistory) {
        LOG.debug("Request to save AlmaHistory : {}", almaHistory);
        return almaHistoryRepository.save(almaHistory);
    }

    /**
     * Update a almaHistory.
     *
     * @param almaHistory the entity to save.
     * @return the persisted entity.
     */
    public AlmaHistory update(AlmaHistory almaHistory) {
        LOG.debug("Request to update AlmaHistory : {}", almaHistory);
        return almaHistoryRepository.save(almaHistory);
    }

    /**
     * Partially update a almaHistory.
     *
     * @param almaHistory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlmaHistory> partialUpdate(AlmaHistory almaHistory) {
        LOG.debug("Request to partially update AlmaHistory : {}", almaHistory);

        return almaHistoryRepository
            .findById(almaHistory.getId())
            .map(existingAlmaHistory -> {
                updateIfPresent(existingAlmaHistory::setFecha, almaHistory.getFecha());

                return existingAlmaHistory;
            })
            .map(almaHistoryRepository::save);
    }

    /**
     * Get all the almaHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AlmaHistory> findAll(Pageable pageable) {
        LOG.debug("Request to get all AlmaHistories");
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            return almaHistoryRepository.findAll(pageable);
        } else {
            return SecurityUtils.getCurrentUserLogin()
                .map(login -> almaHistoryRepository.findAllByUser(pageable, login))
                .orElse(Page.empty(pageable));
        }
    }

    /**
     * Get all the almaHistories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AlmaHistory> findAllWithEagerRelationships(Pageable pageable) {
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            return almaHistoryRepository.findAllWithEagerRelationships(pageable);
        } else {
            return SecurityUtils.getCurrentUserLogin()
                .map(login -> almaHistoryRepository.findAllByUser(pageable, login))
                .orElse(Page.empty(pageable));
        }
    }

    /**
     * Get one almaHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlmaHistory> findOne(Long id) {
        LOG.debug("Request to get AlmaHistory : {}", id);
        return almaHistoryRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the almaHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlmaHistory : {}", id);
        almaHistoryRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
