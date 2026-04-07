package com.chrisgeek.celupaz.service;

import com.chrisgeek.celupaz.domain.MemberCelula;
import com.chrisgeek.celupaz.repository.MemberCelulaRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.chrisgeek.celupaz.domain.MemberCelula}.
 */
@Service
@Transactional
public class MemberCelulaService {

    private static final Logger LOG = LoggerFactory.getLogger(MemberCelulaService.class);

    private final MemberCelulaRepository memberCelulaRepository;

    public MemberCelulaService(MemberCelulaRepository memberCelulaRepository) {
        this.memberCelulaRepository = memberCelulaRepository;
    }

    /**
     * Save a memberCelula.
     *
     * @param memberCelula the entity to save.
     * @return the persisted entity.
     */
    public MemberCelula save(MemberCelula memberCelula) {
        LOG.debug("Request to save MemberCelula : {}", memberCelula);
        return memberCelulaRepository.save(memberCelula);
    }

    /**
     * Update a memberCelula.
     *
     * @param memberCelula the entity to save.
     * @return the persisted entity.
     */
    public MemberCelula update(MemberCelula memberCelula) {
        LOG.debug("Request to update MemberCelula : {}", memberCelula);
        return memberCelulaRepository.save(memberCelula);
    }

    /**
     * Partially update a memberCelula.
     *
     * @param memberCelula the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MemberCelula> partialUpdate(MemberCelula memberCelula) {
        LOG.debug("Request to partially update MemberCelula : {}", memberCelula);

        return memberCelulaRepository
            .findById(memberCelula.getId())
            .map(existingMemberCelula -> {
                updateIfPresent(existingMemberCelula::setFechaCreada, memberCelula.getFechaCreada());
                updateIfPresent(existingMemberCelula::setEnabled, memberCelula.getEnabled());

                return existingMemberCelula;
            })
            .map(memberCelulaRepository::save);
    }

    /**
     * Get all the memberCelulas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MemberCelula> findAll() {
        LOG.debug("Request to get all MemberCelulas");
        return memberCelulaRepository.findAll();
    }

    /**
     * Get all the memberCelulas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MemberCelula> findAllWithEagerRelationships(Pageable pageable) {
        return memberCelulaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one memberCelula by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MemberCelula> findOne(Long id) {
        LOG.debug("Request to get MemberCelula : {}", id);
        return memberCelulaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the memberCelula by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MemberCelula : {}", id);
        memberCelulaRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
