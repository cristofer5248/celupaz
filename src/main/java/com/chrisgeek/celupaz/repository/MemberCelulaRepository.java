package com.chrisgeek.celupaz.repository;

import com.chrisgeek.celupaz.domain.MemberCelula;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MemberCelula entity.
 */
@Repository
public interface MemberCelulaRepository extends JpaRepository<MemberCelula, Long> {
    default Optional<MemberCelula> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MemberCelula> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MemberCelula> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select memberCelula from MemberCelula memberCelula left join fetch memberCelula.member left join fetch memberCelula.cell",
        countQuery = "select count(memberCelula) from MemberCelula memberCelula"
    )
    Page<MemberCelula> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select memberCelula from MemberCelula memberCelula " + "left join fetch memberCelula.member " + "left join fetch memberCelula.cell"
    )
    List<MemberCelula> findAllWithToOneRelationships();

    @Query(
        "select memberCelula from MemberCelula memberCelula " +
            "left join fetch memberCelula.member " +
            "left join fetch memberCelula.cell c " +
            "where c.lider = :login"
    ) // Filtramos por el campo 'lider' de la tabla Cell
    List<MemberCelula> findAllByLider(@Param("login") String login);

    @Query(
        "select memberCelula from MemberCelula memberCelula left join fetch memberCelula.member left join fetch memberCelula.cell where memberCelula.id =:id"
    )
    Optional<MemberCelula> findOneWithToOneRelationships(@Param("id") Long id);
}
