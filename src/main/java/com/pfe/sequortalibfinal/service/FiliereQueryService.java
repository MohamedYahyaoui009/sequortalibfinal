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

import com.pfe.sequortalibfinal.domain.Filiere;
import com.pfe.sequortalibfinal.domain.*; // for static metamodels
import com.pfe.sequortalibfinal.repository.FiliereRepository;
import com.pfe.sequortalibfinal.repository.search.FiliereSearchRepository;
import com.pfe.sequortalibfinal.service.dto.FiliereCriteria;

/**
 * Service for executing complex queries for {@link Filiere} entities in the database.
 * The main input is a {@link FiliereCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Filiere} or a {@link Page} of {@link Filiere} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FiliereQueryService extends QueryService<Filiere> {

    private final Logger log = LoggerFactory.getLogger(FiliereQueryService.class);

    private final FiliereRepository filiereRepository;

    private final FiliereSearchRepository filiereSearchRepository;

    public FiliereQueryService(FiliereRepository filiereRepository, FiliereSearchRepository filiereSearchRepository) {
        this.filiereRepository = filiereRepository;
        this.filiereSearchRepository = filiereSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Filiere} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Filiere> findByCriteria(FiliereCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Filiere> specification = createSpecification(criteria);
        return filiereRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Filiere} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Filiere> findByCriteria(FiliereCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Filiere> specification = createSpecification(criteria);
        return filiereRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FiliereCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Filiere> specification = createSpecification(criteria);
        return filiereRepository.count(specification);
    }

    /**
     * Function to convert {@link FiliereCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Filiere> createSpecification(FiliereCriteria criteria) {
        Specification<Filiere> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Filiere_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Filiere_.nom));
            }
            if (criteria.getModuleId() != null) {
                specification = specification.and(buildSpecification(criteria.getModuleId(),
                    root -> root.join(Filiere_.modules, JoinType.LEFT).get(Module_.id)));
            }
            if (criteria.getDepartementId() != null) {
                specification = specification.and(buildSpecification(criteria.getDepartementId(),
                    root -> root.join(Filiere_.departement, JoinType.LEFT).get(Departement_.id)));
            }
            if (criteria.getHistoriqueEnseignantFiliereId() != null) {
                specification = specification.and(buildSpecification(criteria.getHistoriqueEnseignantFiliereId(),
                    root -> root.join(Filiere_.historiqueEnseignantFiliere, JoinType.LEFT).get(HistoriqueEnseignantFiliere_.id)));
            }
            if (criteria.getHistoriqueEtudiantFiliereId() != null) {
                specification = specification.and(buildSpecification(criteria.getHistoriqueEtudiantFiliereId(),
                    root -> root.join(Filiere_.historiqueEtudiantFiliere, JoinType.LEFT).get(HistoriqueEtudiantFiliere_.id)));
            }
        }
        return specification;
    }
}
