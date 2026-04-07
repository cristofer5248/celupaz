package com.chrisgeek.celupaz.service;

import com.chrisgeek.celupaz.domain.Privilege;
import com.chrisgeek.celupaz.repository.PrivilegeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.chrisgeek.celupaz.domain.Privilege}.
 */
@Service
@Transactional
public class PrivilegeService {

    private static final Logger LOG = LoggerFactory.getLogger(PrivilegeService.class);

    private final PrivilegeRepository privilegeRepository;

    public PrivilegeService(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    /**
     * Save a privilege.
     *
     * @param privilege the entity to save.
     * @return the persisted entity.
     */
    public Privilege save(Privilege privilege) {
        LOG.debug("Request to save Privilege : {}", privilege);
        return privilegeRepository.save(privilege);
    }

    /**
     * Update a privilege.
     *
     * @param privilege the entity to save.
     * @return the persisted entity.
     */
    public Privilege update(Privilege privilege) {
        LOG.debug("Request to update Privilege : {}", privilege);
        return privilegeRepository.save(privilege);
    }

    /**
     * Partially update a privilege.
     *
     * @param privilege the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Privilege> partialUpdate(Privilege privilege) {
        LOG.debug("Request to partially update Privilege : {}", privilege);

        return privilegeRepository
            .findById(privilege.getId())
            .map(existingPrivilege -> {
                updateIfPresent(existingPrivilege::setName, privilege.getName());

                return existingPrivilege;
            })
            .map(privilegeRepository::save);
    }

    /**
     * Get all the privileges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Privilege> findAll(Pageable pageable) {
        LOG.debug("Request to get all Privileges");
        return privilegeRepository.findAll(pageable);
    }

    /**
     * Get one privilege by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Privilege> findOne(Long id) {
        LOG.debug("Request to get Privilege : {}", id);
        return privilegeRepository.findById(id);
    }

    /**
     * Delete the privilege by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Privilege : {}", id);
        privilegeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
