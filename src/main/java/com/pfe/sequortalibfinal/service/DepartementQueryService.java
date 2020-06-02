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

import com.pfe.sequortalibfinal.domain.Departement;
import com.pfe.sequortalibfinal.domain.*; // for static metamodels
import com.pfe.sequortalibfinal.repository.DepartementRepository;
import com.pfe.sequortalibfinal.repository.search.DepartementSearchRepository;
import com.pfe.sequortalibfinal.service.dto.DepartementCriteria;

/**
 * Service for executing complex queries for {@link Departement} entities in the database.
 * The main input is a {@link DepartementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Departement} or a {@link Page} of {@link Departement} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepartementQueryService extends QueryService<Departement> {

    private final Logger log = LoggerFactory.getLogger(DepartementQueryService.class);

    private final DepartementRepository departementRepository;

    private final DepartementSearchRepository departementSearchRepository;

    public DepartementQueryService(DepartementRepository departementRepository, DepartementSearchRepository departementSearchRepository) {
        this.departementRepository = departementRepository;
        this.departementSearchRepository = departementSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Departement} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Departement> findByCriteria(DepartementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Departement> specification = createSpecification(criteria);
        return departementRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Departement} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Departement> findByCriteria(DepartementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Departement> specification = createSpecification(criteria);
        return departementRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepartementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Departement> specification = createSpecification(criteria);
        return departementRepository.count(specification);
    }

    /**
     * Function to convert {@link DepartementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Departement> createSpecification(DepartementCriteria criteria) {
        Specification<Departement> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Departement_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Departement_.nom));
            }
            if (criteria.getFiliereId() != null) {
                specification = specification.and(buildSpecification(criteria.getFiliereId(),
                    root -> root.join(Departement_.filieres, JoinType.LEFT).get(Filiere_.id)));
            }
        }
        return specification;
    }
}
