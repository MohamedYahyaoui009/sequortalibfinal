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

import com.pfe.sequortalibfinal.domain.Etablissement;
import com.pfe.sequortalibfinal.domain.*; // for static metamodels
import com.pfe.sequortalibfinal.repository.EtablissementRepository;
import com.pfe.sequortalibfinal.repository.search.EtablissementSearchRepository;
import com.pfe.sequortalibfinal.service.dto.EtablissementCriteria;

/**
 * Service for executing complex queries for {@link Etablissement} entities in the database.
 * The main input is a {@link EtablissementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Etablissement} or a {@link Page} of {@link Etablissement} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EtablissementQueryService extends QueryService<Etablissement> {

    private final Logger log = LoggerFactory.getLogger(EtablissementQueryService.class);

    private final EtablissementRepository etablissementRepository;

    private final EtablissementSearchRepository etablissementSearchRepository;

    public EtablissementQueryService(EtablissementRepository etablissementRepository, EtablissementSearchRepository etablissementSearchRepository) {
        this.etablissementRepository = etablissementRepository;
        this.etablissementSearchRepository = etablissementSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Etablissement} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Etablissement> findByCriteria(EtablissementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Etablissement> specification = createSpecification(criteria);
        return etablissementRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Etablissement} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Etablissement> findByCriteria(EtablissementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Etablissement> specification = createSpecification(criteria);
        return etablissementRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EtablissementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Etablissement> specification = createSpecification(criteria);
        return etablissementRepository.count(specification);
    }

    /**
     * Function to convert {@link EtablissementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Etablissement> createSpecification(EtablissementCriteria criteria) {
        Specification<Etablissement> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Etablissement_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Etablissement_.nom));
            }
            if (criteria.getFiliere() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFiliere(), Etablissement_.filiere));
            }
            if (criteria.getCycle() != null) {
                specification = specification.and(buildSpecification(criteria.getCycle(), Etablissement_.cycle));
            }
            if (criteria.getEtudiantId() != null) {
                specification = specification.and(buildSpecification(criteria.getEtudiantId(),
                    root -> root.join(Etablissement_.etudiants, JoinType.LEFT).get(Etudiant_.id)));
            }
        }
        return specification;
    }
}
