package com.pfe.sequortalibfinal.repository;

import com.pfe.sequortalibfinal.domain.HistoriqueEtudiantModule;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the HistoriqueEtudiantModule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriqueEtudiantModuleRepository extends JpaRepository<HistoriqueEtudiantModule, Long>, JpaSpecificationExecutor<HistoriqueEtudiantModule> {
}
