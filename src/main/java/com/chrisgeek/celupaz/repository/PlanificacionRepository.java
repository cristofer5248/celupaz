package com.chrisgeek.celupaz.repository;

import com.chrisgeek.celupaz.domain.Planificacion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Planificacion entity.
 */
@Repository
public interface PlanificacionRepository extends JpaRepository<Planificacion, Long> {
    default Optional<Planificacion> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Planificacion> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Planificacion> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select planificacion from Planificacion planificacion left join fetch planificacion.privilege left join fetch planificacion.planiMaster",
        countQuery = "select count(planificacion) from Planificacion planificacion"
    )
    Page<Planificacion> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select planificacion from Planificacion planificacion left join fetch planificacion.privilege left join fetch planificacion.planiMaster"
    )
    List<Planificacion> findAllWithToOneRelationships();

    @Query(
        "select planificacion from Planificacion planificacion left join fetch planificacion.privilege left join fetch planificacion.planiMaster where planificacion.id =:id"
    )
    Optional<Planificacion> findOneWithToOneRelationships(@Param("id") Long id);
}
