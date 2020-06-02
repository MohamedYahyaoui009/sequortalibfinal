package com.pfe.sequortalibfinal.service;

import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantFiliere;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link HistoriqueEnseignantFiliere}.
 */
public interface HistoriqueEnseignantFiliereService {

    /**
     * Save a historiqueEnseignantFiliere.
     *
     * @param historiqueEnseignantFiliere the entity to save.
     * @return the persisted entity.
     */
    HistoriqueEnseignantFiliere save(HistoriqueEnseignantFiliere historiqueEnseignantFiliere);

    /**
     * Get all the historiqueEnseignantFilieres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoriqueEnseignantFiliere> findAll(Pageable pageable);

    /**
     * Get the "id" historiqueEnseignantFiliere.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HistoriqueEnseignantFiliere> findOne(Long id);

    /**
     * Delete the "id" historiqueEnseignantFiliere.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the historiqueEnseignantFiliere corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoriqueEnseignantFiliere> search(String query, Pageable pageable);
}
