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

import com.pfe.sequortalibfinal.domain.HistoriqueEtudiantModule;
import com.pfe.sequortalibfinal.domain.*; // for static metamodels
import com.pfe.sequortalibfinal.repository.HistoriqueEtudiantModuleRepository;
import com.pfe.sequortalibfinal.repository.search.HistoriqueEtudiantModuleSearchRepository;
import com.pfe.sequortalibfinal.service.dto.HistoriqueEtudiantModuleCriteria;

/**
 * Service for executing complex queries for {@link HistoriqueEtudiantModule} entities in the database.
 * The main input is a {@link HistoriqueEtudiantModuleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HistoriqueEtudiantModule} or a {@link Page} of {@link HistoriqueEtudiantModule} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HistoriqueEtudiantModuleQueryService extends QueryService<HistoriqueEtudiantModule> {

    private final Logger log = LoggerFactory.getLogger(HistoriqueEtudiantModuleQueryService.class);

    private final HistoriqueEtudiantModuleRepository historiqueEtudiantModuleRepository;

    private final HistoriqueEtudiantModuleSearchRepository historiqueEtudiantModuleSearchRepository;

    public HistoriqueEtudiantModuleQueryService(HistoriqueEtudiantModuleRepository historiqueEtudiantModuleRepository, HistoriqueEtudiantModuleSearchRepository historiqueEtudiantModuleSearchRepository) {
        this.historiqueEtudiantModuleRepository = historiqueEtudiantModuleRepository;
        this.historiqueEtudiantModuleSearchRepository = historiqueEtudiantModuleSearchRepository;
    }

    /**
     * Return a {@link List} of {@link HistoriqueEtudiantModule} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HistoriqueEtudiantModule> findByCriteria(HistoriqueEtudiantModuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HistoriqueEtudiantModule> specification = createSpecification(criteria);
        return historiqueEtudiantModuleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link HistoriqueEtudiantModule} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HistoriqueEtudiantModule> findByCriteria(HistoriqueEtudiantModuleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HistoriqueEtudiantModule> specification = createSpecification(criteria);
        return historiqueEtudiantModuleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HistoriqueEtudiantModuleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HistoriqueEtudiantModule> specification = createSpecification(criteria);
        return historiqueEtudiantModuleRepository.count(specification);
    }

    /**
     * Function to convert {@link HistoriqueEtudiantModuleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HistoriqueEtudiantModule> createSpecification(HistoriqueEtudiantModuleCriteria criteria) {
        Specification<HistoriqueEtudiantModule> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HistoriqueEtudiantModule_.id));
            }
            if (criteria.getDatedebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatedebut(), HistoriqueEtudiantModule_.datedebut));
            }
            if (criteria.getDatefin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatefin(), HistoriqueEtudiantModule_.datefin));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNote(), HistoriqueEtudiantModule_.note));
            }
            if (criteria.getModuleId() != null) {
                specification = specification.and(buildSpecification(criteria.getModuleId(),
                    root -> root.join(HistoriqueEtudiantModule_.modules, JoinType.LEFT).get(Module_.id)));
            }
            if (criteria.getEtudiantId() != null) {
                specification = specification.and(buildSpecification(criteria.getEtudiantId(),
                    root -> root.join(HistoriqueEtudiantModule_.etudiants, JoinType.LEFT).get(Etudiant_.id)));
            }
        }
        return specification;
    }
}
