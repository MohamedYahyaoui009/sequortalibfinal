package com.pfe.sequortalibfinal.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.pfe.sequortalibfinal.domain.Filiere} entity. This class is used
 * in {@link com.pfe.sequortalibfinal.web.rest.FiliereResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /filieres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FiliereCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private LongFilter moduleId;

    private LongFilter departementId;

    private LongFilter historiqueEnseignantFiliereId;

    private LongFilter historiqueEtudiantFiliereId;

    public FiliereCriteria() {
    }

    public FiliereCriteria(FiliereCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.moduleId = other.moduleId == null ? null : other.moduleId.copy();
        this.departementId = other.departementId == null ? null : other.departementId.copy();
        this.historiqueEnseignantFiliereId = other.historiqueEnseignantFiliereId == null ? null : other.historiqueEnseignantFiliereId.copy();
        this.historiqueEtudiantFiliereId = other.historiqueEtudiantFiliereId == null ? null : other.historiqueEtudiantFiliereId.copy();
    }

    @Override
    public FiliereCriteria copy() {
        return new FiliereCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNom() {
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public LongFilter getModuleId() {
        return moduleId;
    }

    public void setModuleId(LongFilter moduleId) {
        this.moduleId = moduleId;
    }

    public LongFilter getDepartementId() {
        return departementId;
    }

    public void setDepartementId(LongFilter departementId) {
        this.departementId = departementId;
    }

    public LongFilter getHistoriqueEnseignantFiliereId() {
        return historiqueEnseignantFiliereId;
    }

    public void setHistoriqueEnseignantFiliereId(LongFilter historiqueEnseignantFiliereId) {
        this.historiqueEnseignantFiliereId = historiqueEnseignantFiliereId;
    }

    public LongFilter getHistoriqueEtudiantFiliereId() {
        return historiqueEtudiantFiliereId;
    }

    public void setHistoriqueEtudiantFiliereId(LongFilter historiqueEtudiantFiliereId) {
        this.historiqueEtudiantFiliereId = historiqueEtudiantFiliereId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FiliereCriteria that = (FiliereCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(moduleId, that.moduleId) &&
            Objects.equals(departementId, that.departementId) &&
            Objects.equals(historiqueEnseignantFiliereId, that.historiqueEnseignantFiliereId) &&
            Objects.equals(historiqueEtudiantFiliereId, that.historiqueEtudiantFiliereId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nom,
        moduleId,
        departementId,
        historiqueEnseignantFiliereId,
        historiqueEtudiantFiliereId
        );
    }

    @Override
    public String toString() {
        return "FiliereCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (moduleId != null ? "moduleId=" + moduleId + ", " : "") +
                (departementId != null ? "departementId=" + departementId + ", " : "") +
                (historiqueEnseignantFiliereId != null ? "historiqueEnseignantFiliereId=" + historiqueEnseignantFiliereId + ", " : "") +
                (historiqueEtudiantFiliereId != null ? "historiqueEtudiantFiliereId=" + historiqueEtudiantFiliereId + ", " : "") +
            "}";
    }

}
