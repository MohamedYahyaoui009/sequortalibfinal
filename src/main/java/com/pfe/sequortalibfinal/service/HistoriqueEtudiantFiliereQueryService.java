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

import com.pfe.sequortalibfinal.domain.HistoriqueEtudiantFiliere;
import com.pfe.sequortalibfinal.domain.*; // for static metamodels
import com.pfe.sequortalibfinal.repository.HistoriqueEtudiantFiliereRepository;
import com.pfe.sequortalibfinal.repository.search.HistoriqueEtudiantFiliereSearchRepository;
import com.pfe.sequortalibfinal.service.dto.HistoriqueEtudiantFiliereCriteria;

/**
 * Service for executing complex queries for {@link HistoriqueEtudiantFiliere} entities in the database.
 * The main input is a {@link HistoriqueEtudiantFiliereCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HistoriqueEtudiantFiliere} or a {@link Page} of {@link HistoriqueEtudiantFiliere} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HistoriqueEtudiantFiliereQueryService extends QueryService<HistoriqueEtudiantFiliere> {

    private final Logger log = LoggerFactory.getLogger(HistoriqueEtudiantFiliereQueryService.class);

    private final HistoriqueEtudiantFiliereRepository historiqueEtudiantFiliereRepository;

    private final HistoriqueEtudiantFiliereSearchRepository historiqueEtudiantFiliereSearchRepository;

    public HistoriqueEtudiantFiliereQueryService(HistoriqueEtudiantFiliereRepository historiqueEtudiantFiliereRepository, HistoriqueEtudiantFiliereSearchRepository historiqueEtudiantFiliereSearchRepository) {
        this.historiqueEtudiantFiliereRepository = historiqueEtudiantFiliereRepository;
        this.historiqueEtudiantFiliereSearchRepository = historiqueEtudiantFiliereSearchRepository;
    }

    /**
     * Return a {@link List} of {@link HistoriqueEtudiantFiliere} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HistoriqueEtudiantFiliere> findByCriteria(HistoriqueEtudiantFiliereCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HistoriqueEtudiantFiliere> specification = createSpecification(criteria);
        return historiqueEtudiantFiliereRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link HistoriqueEtudiantFiliere} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HistoriqueEtudiantFiliere> findByCriteria(HistoriqueEtudiantFiliereCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HistoriqueEtudiantFiliere> specification = createSpecification(criteria);
        return historiqueEtudiantFiliereRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HistoriqueEtudiantFiliereCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HistoriqueEtudiantFiliere> specification = createSpecification(criteria);
        return historiqueEtudiantFiliereRepository.count(specification);
    }

    /**
     * Function to convert {@link HistoriqueEtudiantFiliereCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HistoriqueEtudiantFiliere> createSpecification(HistoriqueEtudiantFiliereCriteria criteria) {
        Specification<HistoriqueEtudiantFiliere> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HistoriqueEtudiantFiliere_.id));
            }
            if (criteria.getDatedebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatedebut(), HistoriqueEtudiantFiliere_.datedebut));
            }
            if (criteria.getDatefin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatefin(), HistoriqueEtudiantFiliere_.datefin));
            }
            if (criteria.getEtudiantId() != null) {
                specification = specification.and(buildSpecification(criteria.getEtudiantId(),
                    root -> root.join(HistoriqueEtudiantFiliere_.etudiants, JoinType.LEFT).get(Etudiant_.id)));
            }
        }
        return specification;
    }
}
