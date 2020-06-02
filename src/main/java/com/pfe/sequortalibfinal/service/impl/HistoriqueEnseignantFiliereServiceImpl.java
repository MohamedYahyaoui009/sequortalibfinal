package com.pfe.sequortalibfinal.service.impl;

import com.pfe.sequortalibfinal.service.HistoriqueEnseignantFiliereService;
import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantFiliere;
import com.pfe.sequortalibfinal.repository.HistoriqueEnseignantFiliereRepository;
import com.pfe.sequortalibfinal.repository.search.HistoriqueEnseignantFiliereSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link HistoriqueEnseignantFiliere}.
 */
@Service
@Transactional
public class HistoriqueEnseignantFiliereServiceImpl implements HistoriqueEnseignantFiliereService {

    private final Logger log = LoggerFactory.getLogger(HistoriqueEnseignantFiliereServiceImpl.class);

    private final HistoriqueEnseignantFiliereRepository historiqueEnseignantFiliereRepository;

    private final HistoriqueEnseignantFiliereSearchRepository historiqueEnseignantFiliereSearchRepository;

    public HistoriqueEnseignantFiliereServiceImpl(HistoriqueEnseignantFiliereRepository historiqueEnseignantFiliereRepository, HistoriqueEnseignantFiliereSearchRepository historiqueEnseignantFiliereSearchRepository) {
        this.historiqueEnseignantFiliereRepository = historiqueEnseignantFiliereRepository;
        this.historiqueEnseignantFiliereSearchRepository = historiqueEnseignantFiliereSearchRepository;
    }

    /**
     * Save a historiqueEnseignantFiliere.
     *
     * @param historiqueEnseignantFiliere the entity to save.
     * @return the persisted entity.
     */
    @Override
    public HistoriqueEnseignantFiliere save(HistoriqueEnseignantFiliere historiqueEnseignantFiliere) {
        log.debug("Request to save HistoriqueEnseignantFiliere : {}", historiqueEnseignantFiliere);
        HistoriqueEnseignantFiliere result = historiqueEnseignantFiliereRepository.save(historiqueEnseignantFiliere);
        historiqueEnseignantFiliereSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the historiqueEnseignantFilieres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HistoriqueEnseignantFiliere> findAll(Pageable pageable) {
        log.debug("Request to get all HistoriqueEnseignantFilieres");
        return historiqueEnseignantFiliereRepository.findAll(pageable);
    }

    /**
     * Get one historiqueEnseignantFiliere by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HistoriqueEnseignantFiliere> findOne(Long id) {
        log.debug("Request to get HistoriqueEnseignantFiliere : {}", id);
        return historiqueEnseignantFiliereRepository.findById(id);
    }

    /**
     * Delete the historiqueEnseignantFiliere by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete HistoriqueEnseignantFiliere : {}", id);
        historiqueEnseignantFiliereRepository.deleteById(id);
        historiqueEnseignantFiliereSearchRepository.deleteById(id);
    }

    /**
     * Search for the historiqueEnseignantFiliere corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HistoriqueEnseignantFiliere> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HistoriqueEnseignantFilieres for query {}", query);
        return historiqueEnseignantFiliereSearchRepository.search(queryStringQuery(query), pageable);    }
}
