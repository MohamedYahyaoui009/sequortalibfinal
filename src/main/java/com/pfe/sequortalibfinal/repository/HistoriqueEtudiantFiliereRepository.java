package com.pfe.sequortalibfinal.repository;

import com.pfe.sequortalibfinal.domain.HistoriqueEtudiantFiliere;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the HistoriqueEtudiantFiliere entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriqueEtudiantFiliereRepository extends JpaRepository<HistoriqueEtudiantFiliere, Long>, JpaSpecificationExecutor<HistoriqueEtudiantFiliere> {
}
