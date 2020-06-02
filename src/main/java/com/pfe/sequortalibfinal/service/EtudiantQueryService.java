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

import com.pfe.sequortalibfinal.domain.Etudiant;
import com.pfe.sequortalibfinal.domain.*; // for static metamodels
import com.pfe.sequortalibfinal.repository.EtudiantRepository;
import com.pfe.sequortalibfinal.repository.search.EtudiantSearchRepository;
import com.pfe.sequortalibfinal.service.dto.EtudiantCriteria;

/**
 * Service for executing complex queries for {@link Etudiant} entities in the database.
 * The main input is a {@link EtudiantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Etudiant} or a {@link Page} of {@link Etudiant} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EtudiantQueryService extends QueryService<Etudiant> {

    private final Logger log = LoggerFactory.getLogger(EtudiantQueryService.class);

    private final EtudiantRepository etudiantRepository;

    private final EtudiantSearchRepository etudiantSearchRepository;

    public EtudiantQueryService(EtudiantRepository etudiantRepository, EtudiantSearchRepository etudiantSearchRepository) {
        this.etudiantRepository = etudiantRepository;
        this.etudiantSearchRepository = etudiantSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Etudiant} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Etudiant> findByCriteria(EtudiantCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Etudiant> specification = createSpecification(criteria);
        return etudiantRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Etudiant} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Etudiant> findByCriteria(EtudiantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Etudiant> specification = createSpecification(criteria);
        return etudiantRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EtudiantCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Etudiant> specification = createSpecification(criteria);
        return etudiantRepository.count(specification);
    }

    /**
     * Function to convert {@link EtudiantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Etudiant> createSpecification(EtudiantCriteria criteria) {
        Specification<Etudiant> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Etudiant_.id));
            }
            if (criteria.getSemestre() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSemestre(), Etudiant_.semestre));
            }
            if (criteria.getSection() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSection(), Etudiant_.section));
            }
            if (criteria.getEtat() != null) {
                specification = specification.and(buildSpecification(criteria.getEtat(), Etudiant_.etat));
            }
            if (criteria.getHistoriqueEtudiantModuleId() != null) {
                specification = specification.and(buildSpecification(criteria.getHistoriqueEtudiantModuleId(),
                    root -> root.join(Etudiant_.historiqueEtudiantModule, JoinType.LEFT).get(HistoriqueEtudiantModule_.id)));
            }
            if (criteria.getHistoriqueEtudiantFiliereId() != null) {
                specification = specification.and(buildSpecification(criteria.getHistoriqueEtudiantFiliereId(),
                    root -> root.join(Etudiant_.historiqueEtudiantFiliere, JoinType.LEFT).get(HistoriqueEtudiantFiliere_.id)));
            }
            if (criteria.getEtablissementId() != null) {
                specification = specification.and(buildSpecification(criteria.getEtablissementId(),
                    root -> root.join(Etudiant_.etablissement, JoinType.LEFT).get(Etablissement_.id)));
            }
        }
        return specification;
    }
}
