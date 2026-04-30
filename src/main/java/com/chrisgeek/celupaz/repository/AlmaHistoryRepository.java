package com.chrisgeek.celupaz.repository;

import com.chrisgeek.celupaz.domain.AlmaHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlmaHistory entity.
 */
@Repository
public interface AlmaHistoryRepository extends JpaRepository<AlmaHistory, Long> {
    default Optional<AlmaHistory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AlmaHistory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AlmaHistory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select almaHistory from AlmaHistory almaHistory left join fetch almaHistory.alma left join fetch almaHistory.cell left join fetch almaHistory.rolcelula",
        countQuery = "select count(almaHistory) from AlmaHistory almaHistory"
    )
    Page<AlmaHistory> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select almaHistory from AlmaHistory almaHistory left join fetch almaHistory.alma left join fetch almaHistory.cell left join fetch almaHistory.rolcelula"
    )
    List<AlmaHistory> findAllWithToOneRelationships();

    @Query(
        "select almaHistory from AlmaHistory almaHistory left join fetch almaHistory.alma left join fetch almaHistory.cell left join fetch almaHistory.rolcelula where almaHistory.id =:id"
    )
    Optional<AlmaHistory> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        value = "select almaHistory from AlmaHistory almaHistory left join fetch almaHistory.alma left join fetch almaHistory.cell left join fetch almaHistory.rolcelula where almaHistory.cell.lider = :login ",
        countQuery = "select count(almaHistory) from AlmaHistory almaHistory"
    )
    Page<AlmaHistory> findAllByUser(Pageable pageable, @Param("login") String login);
}
