package com.pfe.sequortalibfinal.service.impl;

import com.pfe.sequortalibfinal.service.HistoriqueEtudiantFiliereService;
import com.pfe.sequortalibfinal.domain.HistoriqueEtudiantFiliere;
import com.pfe.sequortalibfinal.repository.HistoriqueEtudiantFiliereRepository;
import com.pfe.sequortalibfinal.repository.search.HistoriqueEtudiantFiliereSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link HistoriqueEtudiantFiliere}.
 */
@Service
@Transactional
public class HistoriqueEtudiantFiliereServiceImpl implements HistoriqueEtudiantFiliereService {

    private final Logger log = LoggerFactory.getLogger(HistoriqueEtudiantFiliereServiceImpl.class);

    private final HistoriqueEtudiantFiliereRepository historiqueEtudiantFiliereRepository;

    private final HistoriqueEtudiantFiliereSearchRepository historiqueEtudiantFiliereSearchRepository;

    public HistoriqueEtudiantFiliereServiceImpl(HistoriqueEtudiantFiliereRepository historiqueEtudiantFiliereRepository, HistoriqueEtudiantFiliereSearchRepository historiqueEtudiantFiliereSearchRepository) {
        this.historiqueEtudiantFiliereRepository = historiqueEtudiantFiliereRepository;
        this.historiqueEtudiantFiliereSearchRepository = historiqueEtudiantFiliereSearchRepository;
    }

    /**
     * Save a historiqueEtudiantFiliere.
     *
     * @param historiqueEtudiantFiliere the entity to save.
     * @return the persisted entity.
     */
    @Override
    public HistoriqueEtudiantFiliere save(HistoriqueEtudiantFiliere historiqueEtudiantFiliere) {
        log.debug("Request to save HistoriqueEtudiantFiliere : {}", historiqueEtudiantFiliere);
        HistoriqueEtudiantFiliere result = historiqueEtudiantFiliereRepository.save(historiqueEtudiantFiliere);
        historiqueEtudiantFiliereSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the historiqueEtudiantFilieres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HistoriqueEtudiantFiliere> findAll(Pageable pageable) {
        log.debug("Request to get all HistoriqueEtudiantFilieres");
        return historiqueEtudiantFiliereRepository.findAll(pageable);
    }

    /**
     * Get one historiqueEtudiantFiliere by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HistoriqueEtudiantFiliere> findOne(Long id) {
        log.debug("Request to get HistoriqueEtudiantFiliere : {}", id);
        return historiqueEtudiantFiliereRepository.findById(id);
    }

    /**
     * Delete the historiqueEtudiantFiliere by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete HistoriqueEtudiantFiliere : {}", id);
        historiqueEtudiantFiliereRepository.deleteById(id);
        historiqueEtudiantFiliereSearchRepository.deleteById(id);
    }

    /**
     * Search for the historiqueEtudiantFiliere corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HistoriqueEtudiantFiliere> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HistoriqueEtudiantFilieres for query {}", query);
        return historiqueEtudiantFiliereSearchRepository.search(queryStringQuery(query), pageable);    }
}
