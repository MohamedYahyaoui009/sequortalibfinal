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

import com.pfe.sequortalibfinal.domain.Enseignant;
import com.pfe.sequortalibfinal.domain.*; // for static metamodels
import com.pfe.sequortalibfinal.repository.EnseignantRepository;
import com.pfe.sequortalibfinal.repository.search.EnseignantSearchRepository;
import com.pfe.sequortalibfinal.service.dto.EnseignantCriteria;

/**
 * Service for executing complex queries for {@link Enseignant} entities in the database.
 * The main input is a {@link EnseignantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Enseignant} or a {@link Page} of {@link Enseignant} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EnseignantQueryService extends QueryService<Enseignant> {

    private final Logger log = LoggerFactory.getLogger(EnseignantQueryService.class);

    private final EnseignantRepository enseignantRepository;

    private final EnseignantSearchRepository enseignantSearchRepository;

    public EnseignantQueryService(EnseignantRepository enseignantRepository, EnseignantSearchRepository enseignantSearchRepository) {
        this.enseignantRepository = enseignantRepository;
        this.enseignantSearchRepository = enseignantSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Enseignant} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Enseignant> findByCriteria(EnseignantCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Enseignant> specification = createSpecification(criteria);
        return enseignantRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Enseignant} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Enseignant> findByCriteria(EnseignantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Enseignant> specification = createSpecification(criteria);
        return enseignantRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EnseignantCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Enseignant> specification = createSpecification(criteria);
        return enseignantRepository.count(specification);
    }

    /**
     * Function to convert {@link EnseignantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Enseignant> createSpecification(EnseignantCriteria criteria) {
        Specification<Enseignant> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Enseignant_.id));
            }
            if (criteria.getGrade() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGrade(), Enseignant_.grade));
            }
            if (criteria.getHistoriqueEnseignantModuleId() != null) {
                specification = specification.and(buildSpecification(criteria.getHistoriqueEnseignantModuleId(),
                    root -> root.join(Enseignant_.historiqueEnseignantModule, JoinType.LEFT).get(HistoriqueEnseignantModule_.id)));
            }
            if (criteria.getHistoriqueEnseignantFiliereId() != null) {
                specification = specification.and(buildSpecification(criteria.getHistoriqueEnseignantFiliereId(),
                    root -> root.join(Enseignant_.historiqueEnseignantFiliere, JoinType.LEFT).get(HistoriqueEnseignantFiliere_.id)));
            }
        }
        return specification;
    }
}
