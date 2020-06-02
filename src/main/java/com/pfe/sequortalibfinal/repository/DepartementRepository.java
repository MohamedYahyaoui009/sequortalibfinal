package com.pfe.sequortalibfinal.repository;

import com.pfe.sequortalibfinal.domain.Departement;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Departement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long>, JpaSpecificationExecutor<Departement> {
}
