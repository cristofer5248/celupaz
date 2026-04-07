package com.chrisgeek.celupaz.service;

import com.chrisgeek.celupaz.domain.Cell;
import com.chrisgeek.celupaz.repository.CellRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.chrisgeek.celupaz.domain.Cell}.
 */
@Service
@Transactional
public class CellService {

    private static final Logger LOG = LoggerFactory.getLogger(CellService.class);

    private final CellRepository cellRepository;

    public CellService(CellRepository cellRepository) {
        this.cellRepository = cellRepository;
    }

    /**
     * Save a cell.
     *
     * @param cell the entity to save.
     * @return the persisted entity.
     */
    public Cell save(Cell cell) {
        LOG.debug("Request to save Cell : {}", cell);
        return cellRepository.save(cell);
    }

    /**
     * Update a cell.
     *
     * @param cell the entity to save.
     * @return the persisted entity.
     */
    public Cell update(Cell cell) {
        LOG.debug("Request to update Cell : {}", cell);
        return cellRepository.save(cell);
    }

    /**
     * Partially update a cell.
     *
     * @param cell the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Cell> partialUpdate(Cell cell) {
        LOG.debug("Request to partially update Cell : {}", cell);

        return cellRepository
            .findById(cell.getId())
            .map(existingCell -> {
                updateIfPresent(existingCell::setName, cell.getName());
                updateIfPresent(existingCell::setStartDate, cell.getStartDate());
                updateIfPresent(existingCell::setDescription, cell.getDescription());
                updateIfPresent(existingCell::setSector, cell.getSector());
                updateIfPresent(existingCell::setLider, cell.getLider());
                updateIfPresent(existingCell::setCordinador, cell.getCordinador());

                return existingCell;
            })
            .map(cellRepository::save);
    }

    /**
     * Get all the cells.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Cell> findAll(Pageable pageable) {
        LOG.debug("Request to get all Cells");
        return cellRepository.findAll(pageable);
    }

    /**
     * Get all the cells with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Cell> findAllWithEagerRelationships(Pageable pageable) {
        return cellRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one cell by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Cell> findOne(Long id) {
        LOG.debug("Request to get Cell : {}", id);
        return cellRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the cell by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Cell : {}", id);
        cellRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
