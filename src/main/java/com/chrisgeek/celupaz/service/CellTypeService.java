package com.chrisgeek.celupaz.service;

import com.chrisgeek.celupaz.domain.CellType;
import com.chrisgeek.celupaz.repository.CellTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.chrisgeek.celupaz.domain.CellType}.
 */
@Service
@Transactional
public class CellTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(CellTypeService.class);

    private final CellTypeRepository cellTypeRepository;

    public CellTypeService(CellTypeRepository cellTypeRepository) {
        this.cellTypeRepository = cellTypeRepository;
    }

    /**
     * Save a cellType.
     *
     * @param cellType the entity to save.
     * @return the persisted entity.
     */
    public CellType save(CellType cellType) {
        LOG.debug("Request to save CellType : {}", cellType);
        return cellTypeRepository.save(cellType);
    }

    /**
     * Update a cellType.
     *
     * @param cellType the entity to save.
     * @return the persisted entity.
     */
    public CellType update(CellType cellType) {
        LOG.debug("Request to update CellType : {}", cellType);
        return cellTypeRepository.save(cellType);
    }

    /**
     * Partially update a cellType.
     *
     * @param cellType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CellType> partialUpdate(CellType cellType) {
        LOG.debug("Request to partially update CellType : {}", cellType);

        return cellTypeRepository
            .findById(cellType.getId())
            .map(existingCellType -> {
                updateIfPresent(existingCellType::setName, cellType.getName());

                return existingCellType;
            })
            .map(cellTypeRepository::save);
    }

    /**
     * Get all the cellTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CellType> findAll(Pageable pageable) {
        LOG.debug("Request to get all CellTypes");
        return cellTypeRepository.findAll(pageable);
    }

    /**
     * Get one cellType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CellType> findOne(Long id) {
        LOG.debug("Request to get CellType : {}", id);
        return cellTypeRepository.findById(id);
    }

    /**
     * Delete the cellType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CellType : {}", id);
        cellTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
