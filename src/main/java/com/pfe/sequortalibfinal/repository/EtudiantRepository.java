package com.pfe.sequortalibfinal.repository;

import com.pfe.sequortalibfinal.domain.Etudiant;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Etudiant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long>, JpaSpecificationExecutor<Etudiant> {
}
