package com.pfe.sequortalibfinal.service.impl;

import com.pfe.sequortalibfinal.service.HistoriqueEtudiantModuleService;
import com.pfe.sequortalibfinal.domain.HistoriqueEtudiantModule;
import com.pfe.sequortalibfinal.repository.HistoriqueEtudiantModuleRepository;
import com.pfe.sequortalibfinal.repository.search.HistoriqueEtudiantModuleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link HistoriqueEtudiantModule}.
 */
@Service
@Transactional
public class HistoriqueEtudiantModuleServiceImpl implements HistoriqueEtudiantModuleService {

    private final Logger log = LoggerFactory.getLogger(HistoriqueEtudiantModuleServiceImpl.class);

    private final HistoriqueEtudiantModuleRepository historiqueEtudiantModuleRepository;

    private final HistoriqueEtudiantModuleSearchRepository historiqueEtudiantModuleSearchRepository;

    public HistoriqueEtudiantModuleServiceImpl(HistoriqueEtudiantModuleRepository historiqueEtudiantModuleRepository, HistoriqueEtudiantModuleSearchRepository historiqueEtudiantModuleSearchRepository) {
        this.historiqueEtudiantModuleRepository = historiqueEtudiantModuleRepository;
        this.historiqueEtudiantModuleSearchRepository = historiqueEtudiantModuleSearchRepository;
    }

    /**
     * Save a historiqueEtudiantModule.
     *
     * @param historiqueEtudiantModule the entity to save.
     * @return the persisted entity.
     */
    @Override
    public HistoriqueEtudiantModule save(HistoriqueEtudiantModule historiqueEtudiantModule) {
        log.debug("Request to save HistoriqueEtudiantModule : {}", historiqueEtudiantModule);
        HistoriqueEtudiantModule result = historiqueEtudiantModuleRepository.save(historiqueEtudiantModule);
        historiqueEtudiantModuleSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the historiqueEtudiantModules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HistoriqueEtudiantModule> findAll(Pageable pageable) {
        log.debug("Request to get all HistoriqueEtudiantModules");
        return historiqueEtudiantModuleRepository.findAll(pageable);
    }

    /**
     * Get one historiqueEtudiantModule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HistoriqueEtudiantModule> findOne(Long id) {
        log.debug("Request to get HistoriqueEtudiantModule : {}", id);
        return historiqueEtudiantModuleRepository.findById(id);
    }

    /**
     * Delete the historiqueEtudiantModule by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete HistoriqueEtudiantModule : {}", id);
        historiqueEtudiantModuleRepository.deleteById(id);
        historiqueEtudiantModuleSearchRepository.deleteById(id);
    }

    /**
     * Search for the historiqueEtudiantModule corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HistoriqueEtudiantModule> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HistoriqueEtudiantModules for query {}", query);
        return historiqueEtudiantModuleSearchRepository.search(queryStringQuery(query), pageable);    }
}
