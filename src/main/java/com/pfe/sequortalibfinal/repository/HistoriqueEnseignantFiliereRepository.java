package com.pfe.sequortalibfinal.repository;

import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantFiliere;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the HistoriqueEnseignantFiliere entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriqueEnseignantFiliereRepository extends JpaRepository<HistoriqueEnseignantFiliere, Long>, JpaSpecificationExecutor<HistoriqueEnseignantFiliere> {
}
