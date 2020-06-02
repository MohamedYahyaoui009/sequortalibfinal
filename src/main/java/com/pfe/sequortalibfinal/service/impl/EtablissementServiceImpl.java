package com.pfe.sequortalibfinal.service.impl;

import com.pfe.sequortalibfinal.service.EtablissementService;
import com.pfe.sequortalibfinal.domain.Etablissement;
import com.pfe.sequortalibfinal.repository.EtablissementRepository;
import com.pfe.sequortalibfinal.repository.search.EtablissementSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Etablissement}.
 */
@Service
@Transactional
public class EtablissementServiceImpl implements EtablissementService {

    private final Logger log = LoggerFactory.getLogger(EtablissementServiceImpl.class);

    private final EtablissementRepository etablissementRepository;

    private final EtablissementSearchRepository etablissementSearchRepository;

    public EtablissementServiceImpl(EtablissementRepository etablissementRepository, EtablissementSearchRepository etablissementSearchRepository) {
        this.etablissementRepository = etablissementRepository;
        this.etablissementSearchRepository = etablissementSearchRepository;
    }

    /**
     * Save a etablissement.
     *
     * @param etablissement the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Etablissement save(Etablissement etablissement) {
        log.debug("Request to save Etablissement : {}", etablissement);
        Etablissement result = etablissementRepository.save(etablissement);
        etablissementSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the etablissements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Etablissement> findAll(Pageable pageable) {
        log.debug("Request to get all Etablissements");
        return etablissementRepository.findAll(pageable);
    }

    /**
     * Get one etablissement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Etablissement> findOne(Long id) {
        log.debug("Request to get Etablissement : {}", id);
        return etablissementRepository.findById(id);
    }

    /**
     * Delete the etablissement by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Etablissement : {}", id);
        etablissementRepository.deleteById(id);
        etablissementSearchRepository.deleteById(id);
    }

    /**
     * Search for the etablissement corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Etablissement> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Etablissements for query {}", query);
        return etablissementSearchRepository.search(queryStringQuery(query), pageable);    }
}
