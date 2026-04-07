package com.chrisgeek.celupaz.repository;

import com.chrisgeek.celupaz.domain.Alma;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Alma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlmaRepository extends JpaRepository<Alma, Long> {}
