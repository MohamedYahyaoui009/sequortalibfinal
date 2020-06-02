package com.pfe.sequortalibfinal.service.impl;

import com.pfe.sequortalibfinal.service.FiliereService;
import com.pfe.sequortalibfinal.domain.Filiere;
import com.pfe.sequortalibfinal.repository.FiliereRepository;
import com.pfe.sequortalibfinal.repository.search.FiliereSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Filiere}.
 */
@Service
@Transactional
public class FiliereServiceImpl implements FiliereService {

    private final Logger log = LoggerFactory.getLogger(FiliereServiceImpl.class);

    private final FiliereRepository filiereRepository;

    private final FiliereSearchRepository filiereSearchRepository;

    public FiliereServiceImpl(FiliereRepository filiereRepository, FiliereSearchRepository filiereSearchRepository) {
        this.filiereRepository = filiereRepository;
        this.filiereSearchRepository = filiereSearchRepository;
    }

    /**
     * Save a filiere.
     *
     * @param filiere the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Filiere save(Filiere filiere) {
        log.debug("Request to save Filiere : {}", filiere);
        Filiere result = filiereRepository.save(filiere);
        filiereSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the filieres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Filiere> findAll(Pageable pageable) {
        log.debug("Request to get all Filieres");
        return filiereRepository.findAll(pageable);
    }

    /**
     * Get all the filieres with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Filiere> findAllWithEagerRelationships(Pageable pageable) {
        return filiereRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one filiere by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Filiere> findOne(Long id) {
        log.debug("Request to get Filiere : {}", id);
        return filiereRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the filiere by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Filiere : {}", id);
        filiereRepository.deleteById(id);
        filiereSearchRepository.deleteById(id);
    }

    /**
     * Search for the filiere corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Filiere> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Filieres for query {}", query);
        return filiereSearchRepository.search(queryStringQuery(query), pageable);    }
}
