package com.chrisgeek.celupaz.repository;

import com.chrisgeek.celupaz.domain.Attendance;
import com.chrisgeek.celupaz.domain.AttendanceDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Attendance entity.
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    default Optional<Attendance> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Attendance> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Attendance> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select attendance from Attendance attendance left join fetch attendance.planificacion",
        countQuery = "select count(attendance) from Attendance attendance"
    )
    Page<Attendance> findAllWithToOneRelationships(Pageable pageable);

    @Query("select attendance from Attendance attendance left join fetch attendance.planificacion")
    List<Attendance> findAllWithToOneRelationships();

    @Query("select attendance from Attendance attendance left join fetch attendance.planificacion where attendance.id =:id")
    Optional<Attendance> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select new com.chrisgeek.celupaz.domain.AttendanceDTO(a.id, p.fecha, m.name, c.name, mc.enabled) " +
            "from Attendance a " +
            "join a.planificacion p " +
            "join a.membercelula mc " +
            "join mc.member m " +
            "join mc.cell c"
    )
    Page<AttendanceDTO> findAllDTO(Pageable pageable);

    @Query(
        "select new com.chrisgeek.celupaz.domain.AttendanceDTO(a.id, p.fecha, m.name, c.name, mc.enabled) " +
            "from Attendance a " +
            "join a.planificacion p " +
            "join a.membercelula mc " +
            "join mc.member m " +
            "join mc.cell c " +
            "where c.lider = :login"
    )
    Page<AttendanceDTO> findAllByLiderDTO(@Param("login") String login, Pageable pageable);
}
