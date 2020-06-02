package com.pfe.sequortalibfinal.repository;

import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantModule;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the HistoriqueEnseignantModule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriqueEnseignantModuleRepository extends JpaRepository<HistoriqueEnseignantModule, Long>, JpaSpecificationExecutor<HistoriqueEnseignantModule> {
}
