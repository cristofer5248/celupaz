package com.chrisgeek.celupaz.repository;

import com.chrisgeek.celupaz.domain.PlaniMaster;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PlaniMaster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaniMasterRepository extends JpaRepository<PlaniMaster, Long> {}
