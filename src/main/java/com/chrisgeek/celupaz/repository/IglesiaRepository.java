package com.chrisgeek.celupaz.repository;

import com.chrisgeek.celupaz.domain.Iglesia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Iglesia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IglesiaRepository extends JpaRepository<Iglesia, Long> {}
