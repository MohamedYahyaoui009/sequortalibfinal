package com.pfe.sequortalibfinal.service.impl;

import com.pfe.sequortalibfinal.service.EtudiantService;
import com.pfe.sequortalibfinal.domain.Etudiant;
import com.pfe.sequortalibfinal.repository.EtudiantRepository;
import com.pfe.sequortalibfinal.repository.search.EtudiantSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Etudiant}.
 */
@Service
@Transactional
public class EtudiantServiceImpl implements EtudiantService {

    private final Logger log = LoggerFactory.getLogger(EtudiantServiceImpl.class);

    private final EtudiantRepository etudiantRepository;

    private final EtudiantSearchRepository etudiantSearchRepository;

    public EtudiantServiceImpl(EtudiantRepository etudiantRepository, EtudiantSearchRepository etudiantSearchRepository) {
        this.etudiantRepository = etudiantRepository;
        this.etudiantSearchRepository = etudiantSearchRepository;
    }

    /**
     * Save a etudiant.
     *
     * @param etudiant the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Etudiant save(Etudiant etudiant) {
        log.debug("Request to save Etudiant : {}", etudiant);
        Etudiant result = etudiantRepository.save(etudiant);
        etudiantSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the etudiants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Etudiant> findAll(Pageable pageable) {
        log.debug("Request to get all Etudiants");
        return etudiantRepository.findAll(pageable);
    }

    /**
     * Get one etudiant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Etudiant> findOne(Long id) {
        log.debug("Request to get Etudiant : {}", id);
        return etudiantRepository.findById(id);
    }

    /**
     * Delete the etudiant by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Etudiant : {}", id);
        etudiantRepository.deleteById(id);
        etudiantSearchRepository.deleteById(id);
    }

    /**
     * Search for the etudiant corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Etudiant> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Etudiants for query {}", query);
        return etudiantSearchRepository.search(queryStringQuery(query), pageable);    }
}
