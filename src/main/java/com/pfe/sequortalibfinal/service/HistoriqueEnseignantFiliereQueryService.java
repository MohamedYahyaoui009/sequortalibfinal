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

import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantFiliere;
import com.pfe.sequortalibfinal.domain.*; // for static metamodels
import com.pfe.sequortalibfinal.repository.HistoriqueEnseignantFiliereRepository;
import com.pfe.sequortalibfinal.repository.search.HistoriqueEnseignantFiliereSearchRepository;
import com.pfe.sequortalibfinal.service.dto.HistoriqueEnseignantFiliereCriteria;

/**
 * Service for executing complex queries for {@link HistoriqueEnseignantFiliere} entities in the database.
 * The main input is a {@link HistoriqueEnseignantFiliereCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HistoriqueEnseignantFiliere} or a {@link Page} of {@link HistoriqueEnseignantFiliere} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HistoriqueEnseignantFiliereQueryService extends QueryService<HistoriqueEnseignantFiliere> {

    private final Logger log = LoggerFactory.getLogger(HistoriqueEnseignantFiliereQueryService.class);

    private final HistoriqueEnseignantFiliereRepository historiqueEnseignantFiliereRepository;

    private final HistoriqueEnseignantFiliereSearchRepository historiqueEnseignantFiliereSearchRepository;

    public HistoriqueEnseignantFiliereQueryService(HistoriqueEnseignantFiliereRepository historiqueEnseignantFiliereRepository, HistoriqueEnseignantFiliereSearchRepository historiqueEnseignantFiliereSearchRepository) {
        this.historiqueEnseignantFiliereRepository = historiqueEnseignantFiliereRepository;
        this.historiqueEnseignantFiliereSearchRepository = historiqueEnseignantFiliereSearchRepository;
    }

    /**
     * Return a {@link List} of {@link HistoriqueEnseignantFiliere} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HistoriqueEnseignantFiliere> findByCriteria(HistoriqueEnseignantFiliereCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HistoriqueEnseignantFiliere> specification = createSpecification(criteria);
        return historiqueEnseignantFiliereRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link HistoriqueEnseignantFiliere} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HistoriqueEnseignantFiliere> findByCriteria(HistoriqueEnseignantFiliereCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HistoriqueEnseignantFiliere> specification = createSpecification(criteria);
        return historiqueEnseignantFiliereRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HistoriqueEnseignantFiliereCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HistoriqueEnseignantFiliere> specification = createSpecification(criteria);
        return historiqueEnseignantFiliereRepository.count(specification);
    }

    /**
     * Function to convert {@link HistoriqueEnseignantFiliereCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HistoriqueEnseignantFiliere> createSpecification(HistoriqueEnseignantFiliereCriteria criteria) {
        Specification<HistoriqueEnseignantFiliere> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HistoriqueEnseignantFiliere_.id));
            }
            if (criteria.getDatedebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatedebut(), HistoriqueEnseignantFiliere_.datedebut));
            }
            if (criteria.getDatefin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatefin(), HistoriqueEnseignantFiliere_.datefin));
            }
            if (criteria.getFiliereId() != null) {
                specification = specification.and(buildSpecification(criteria.getFiliereId(),
                    root -> root.join(HistoriqueEnseignantFiliere_.filieres, JoinType.LEFT).get(Filiere_.id)));
            }
            if (criteria.getEnseignantId() != null) {
                specification = specification.and(buildSpecification(criteria.getEnseignantId(),
                    root -> root.join(HistoriqueEnseignantFiliere_.enseignants, JoinType.LEFT).get(Enseignant_.id)));
            }
        }
        return specification;
    }
}
