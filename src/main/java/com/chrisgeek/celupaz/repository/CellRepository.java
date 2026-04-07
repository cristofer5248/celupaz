package com.chrisgeek.celupaz.repository;

import com.chrisgeek.celupaz.domain.Cell;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cell entity.
 */
@Repository
public interface CellRepository extends JpaRepository<Cell, Long> {
    default Optional<Cell> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Cell> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Cell> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select cell from Cell cell left join fetch cell.cellType", countQuery = "select count(cell) from Cell cell")
    Page<Cell> findAllWithToOneRelationships(Pageable pageable);

    @Query("select cell from Cell cell left join fetch cell.cellType")
    List<Cell> findAllWithToOneRelationships();

    @Query("select cell from Cell cell left join fetch cell.cellType where cell.id =:id")
    Optional<Cell> findOneWithToOneRelationships(@Param("id") Long id);
}
