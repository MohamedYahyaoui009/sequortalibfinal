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

import com.pfe.sequortalibfinal.domain.Module;
import com.pfe.sequortalibfinal.domain.*; // for static metamodels
import com.pfe.sequortalibfinal.repository.ModuleRepository;
import com.pfe.sequortalibfinal.repository.search.ModuleSearchRepository;
import com.pfe.sequortalibfinal.service.dto.ModuleCriteria;

/**
 * Service for executing complex queries for {@link Module} entities in the database.
 * The main input is a {@link ModuleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Module} or a {@link Page} of {@link Module} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ModuleQueryService extends QueryService<Module> {

    private final Logger log = LoggerFactory.getLogger(ModuleQueryService.class);

    private final ModuleRepository moduleRepository;

    private final ModuleSearchRepository moduleSearchRepository;

    public ModuleQueryService(ModuleRepository moduleRepository, ModuleSearchRepository moduleSearchRepository) {
        this.moduleRepository = moduleRepository;
        this.moduleSearchRepository = moduleSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Module} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Module> findByCriteria(ModuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Module> specification = createSpecification(criteria);
        return moduleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Module} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Module> findByCriteria(ModuleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Module> specification = createSpecification(criteria);
        return moduleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ModuleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Module> specification = createSpecification(criteria);
        return moduleRepository.count(specification);
    }

    /**
     * Function to convert {@link ModuleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Module> createSpecification(ModuleCriteria criteria) {
        Specification<Module> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Module_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Module_.nom));
            }
            if (criteria.getSemestre() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSemestre(), Module_.semestre));
            }
            if (criteria.getFiliereId() != null) {
                specification = specification.and(buildSpecification(criteria.getFiliereId(),
                    root -> root.join(Module_.filieres, JoinType.LEFT).get(Filiere_.id)));
            }
            if (criteria.getHistoriqueEtudiantModuleId() != null) {
                specification = specification.and(buildSpecification(criteria.getHistoriqueEtudiantModuleId(),
                    root -> root.join(Module_.historiqueEtudiantModule, JoinType.LEFT).get(HistoriqueEtudiantModule_.id)));
            }
            if (criteria.getHistoriqueEnseignantModuleId() != null) {
                specification = specification.and(buildSpecification(criteria.getHistoriqueEnseignantModuleId(),
                    root -> root.join(Module_.historiqueEnseignantModule, JoinType.LEFT).get(HistoriqueEnseignantModule_.id)));
            }
        }
        return specification;
    }
}
