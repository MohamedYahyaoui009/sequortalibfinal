package com.pfe.sequortalibfinal.service.impl;

import com.pfe.sequortalibfinal.service.HistoriqueEnseignantModuleService;
import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantModule;
import com.pfe.sequortalibfinal.repository.HistoriqueEnseignantModuleRepository;
import com.pfe.sequortalibfinal.repository.search.HistoriqueEnseignantModuleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link HistoriqueEnseignantModule}.
 */
@Service
@Transactional
public class HistoriqueEnseignantModuleServiceImpl implements HistoriqueEnseignantModuleService {

    private final Logger log = LoggerFactory.getLogger(HistoriqueEnseignantModuleServiceImpl.class);

    private final HistoriqueEnseignantModuleRepository historiqueEnseignantModuleRepository;

    private final HistoriqueEnseignantModuleSearchRepository historiqueEnseignantModuleSearchRepository;

    public HistoriqueEnseignantModuleServiceImpl(HistoriqueEnseignantModuleRepository historiqueEnseignantModuleRepository, HistoriqueEnseignantModuleSearchRepository historiqueEnseignantModuleSearchRepository) {
        this.historiqueEnseignantModuleRepository = historiqueEnseignantModuleRepository;
        this.historiqueEnseignantModuleSearchRepository = historiqueEnseignantModuleSearchRepository;
    }

    /**
     * Save a historiqueEnseignantModule.
     *
     * @param historiqueEnseignantModule the entity to save.
     * @return the persisted entity.
     */
    @Override
    public HistoriqueEnseignantModule save(HistoriqueEnseignantModule historiqueEnseignantModule) {
        log.debug("Request to save HistoriqueEnseignantModule : {}", historiqueEnseignantModule);
        HistoriqueEnseignantModule result = historiqueEnseignantModuleRepository.save(historiqueEnseignantModule);
        historiqueEnseignantModuleSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the historiqueEnseignantModules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HistoriqueEnseignantModule> findAll(Pageable pageable) {
        log.debug("Request to get all HistoriqueEnseignantModules");
        return historiqueEnseignantModuleRepository.findAll(pageable);
    }

    /**
     * Get one historiqueEnseignantModule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HistoriqueEnseignantModule> findOne(Long id) {
        log.debug("Request to get HistoriqueEnseignantModule : {}", id);
        return historiqueEnseignantModuleRepository.findById(id);
    }

    /**
     * Delete the historiqueEnseignantModule by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete HistoriqueEnseignantModule : {}", id);
        historiqueEnseignantModuleRepository.deleteById(id);
        historiqueEnseignantModuleSearchRepository.deleteById(id);
    }

    /**
     * Search for the historiqueEnseignantModule corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HistoriqueEnseignantModule> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HistoriqueEnseignantModules for query {}", query);
        return historiqueEnseignantModuleSearchRepository.search(queryStringQuery(query), pageable);    }
}
