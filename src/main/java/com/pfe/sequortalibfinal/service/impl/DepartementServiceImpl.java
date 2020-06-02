package com.pfe.sequortalibfinal.service.impl;

import com.pfe.sequortalibfinal.service.DepartementService;
import com.pfe.sequortalibfinal.domain.Departement;
import com.pfe.sequortalibfinal.repository.DepartementRepository;
import com.pfe.sequortalibfinal.repository.search.DepartementSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Departement}.
 */
@Service
@Transactional
public class DepartementServiceImpl implements DepartementService {

    private final Logger log = LoggerFactory.getLogger(DepartementServiceImpl.class);

    private final DepartementRepository departementRepository;

    private final DepartementSearchRepository departementSearchRepository;

    public DepartementServiceImpl(DepartementRepository departementRepository, DepartementSearchRepository departementSearchRepository) {
        this.departementRepository = departementRepository;
        this.departementSearchRepository = departementSearchRepository;
    }

    /**
     * Save a departement.
     *
     * @param departement the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Departement save(Departement departement) {
        log.debug("Request to save Departement : {}", departement);
        Departement result = departementRepository.save(departement);
        departementSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the departements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Departement> findAll(Pageable pageable) {
        log.debug("Request to get all Departements");
        return departementRepository.findAll(pageable);
    }

    /**
     * Get one departement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Departement> findOne(Long id) {
        log.debug("Request to get Departement : {}", id);
        return departementRepository.findById(id);
    }

    /**
     * Delete the departement by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Departement : {}", id);
        departementRepository.deleteById(id);
        departementSearchRepository.deleteById(id);
    }

    /**
     * Search for the departement corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Departement> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Departements for query {}", query);
        return departementSearchRepository.search(queryStringQuery(query), pageable);    }
}
