package com.chrisgeek.celupaz.repository;

import com.chrisgeek.celupaz.domain.RolCelula;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RolCelula entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RolCelulaRepository extends JpaRepository<RolCelula, Long> {}
