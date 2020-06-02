package com.pfe.sequortalibfinal.service;

import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantModule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link HistoriqueEnseignantModule}.
 */
public interface HistoriqueEnseignantModuleService {

    /**
     * Save a historiqueEnseignantModule.
     *
     * @param historiqueEnseignantModule the entity to save.
     * @return the persisted entity.
     */
    HistoriqueEnseignantModule save(HistoriqueEnseignantModule historiqueEnseignantModule);

    /**
     * Get all the historiqueEnseignantModules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoriqueEnseignantModule> findAll(Pageable pageable);

    /**
     * Get the "id" historiqueEnseignantModule.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HistoriqueEnseignantModule> findOne(Long id);

    /**
     * Delete the "id" historiqueEnseignantModule.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the historiqueEnseignantModule corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoriqueEnseignantModule> search(String query, Pageable pageable);
}
