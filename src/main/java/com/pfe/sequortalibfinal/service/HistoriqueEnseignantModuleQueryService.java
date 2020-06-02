package com.pfe.sequortalibfinal.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantModule;
import com.pfe.sequortalibfinal.domain.*; // for static metamodels
import com.pfe.sequortalibfinal.repository.HistoriqueEnseignantModuleRepository;
import com.pfe.sequortalibfinal.repository.search.HistoriqueEnseignantModuleSearchRepository;
import com.pfe.sequortalibfinal.service.dto.HistoriqueEnseignantModuleCriteria;

/**
 * Service for executing complex queries for {@link HistoriqueEnseignantModule} entities in the database.
 * The main input is a {@link HistoriqueEnseignantModuleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HistoriqueEnseignantModule} or a {@link Page} of {@link HistoriqueEnseignantModule} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HistoriqueEnseignantModuleQueryService extends QueryService<HistoriqueEnseignantModule> {

    private final Logger log = LoggerFactory.getLogger(HistoriqueEnseignantModuleQueryService.class);

    private final HistoriqueEnseignantModuleRepository historiqueEnseignantModuleRepository;

    private final HistoriqueEnseignantModuleSearchRepository historiqueEnseignantModuleSearchRepository;

    public HistoriqueEnseignantModuleQueryService(HistoriqueEnseignantModuleRepository historiqueEnseignantModuleRepository, HistoriqueEnseignantModuleSearchRepository historiqueEnseignantModuleSearchRepository) {
        this.historiqueEnseignantModuleRepository = historiqueEnseignantModuleRepository;
        this.historiqueEnseignantModuleSearchRepository = historiqueEnseignantModuleSearchRepository;
    }

    /**
     * Return a {@link List} of {@link HistoriqueEnseignantModule} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HistoriqueEnseignantModule> findByCriteria(HistoriqueEnseignantModuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HistoriqueEnseignantModule> specification = createSpecification(criteria);
        return historiqueEnseignantModuleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link HistoriqueEnseignantModule} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HistoriqueEnseignantModule> findByCriteria(HistoriqueEnseignantModuleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HistoriqueEnseignantModule> specification = createSpecification(criteria);
        return historiqueEnseignantModuleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HistoriqueEnseignantModuleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HistoriqueEnseignantModule> specification = createSpecification(criteria);
        return historiqueEnseignantModuleRepository.count(specification);
    }

    /**
     * Function to convert {@link HistoriqueEnseignantModuleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HistoriqueEnseignantModule> createSpecification(HistoriqueEnseignantModuleCriteria criteria) {
        Specification<HistoriqueEnseignantModule> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HistoriqueEnseignantModule_.id));
            }
            if (criteria.getDatedebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatedebut(), HistoriqueEnseignantModule_.datedebut));
            }
            if (criteria.getDatefin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatefin(), HistoriqueEnseignantModule_.datefin));
            }
            if (criteria.getModuleId() != null) {
                specification = specification.and(buildSpecification(criteria.getModuleId(),
                    root -> root.join(HistoriqueEnseignantModule_.modules, JoinType.LEFT).get(Module_.id)));
            }
            if (criteria.getEnseignantId() != null) {
                specification = specification.and(buildSpecification(criteria.getEnseignantId(),
                    root -> root.join(HistoriqueEnseignantModule_.enseignants, JoinType.LEFT).get(Enseignant_.id)));
            }
        }
        return specification;
    }
}
