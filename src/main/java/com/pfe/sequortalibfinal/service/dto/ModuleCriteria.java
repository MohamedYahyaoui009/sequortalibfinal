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
 * Criteria class for the {@link com.pfe.sequortalibfinal.domain.Module} entity. This class is used
 * in {@link com.pfe.sequortalibfinal.web.rest.ModuleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /modules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ModuleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private IntegerFilter semestre;

    private LongFilter filiereId;

    private LongFilter historiqueEtudiantModuleId;

    private LongFilter historiqueEnseignantModuleId;

    public ModuleCriteria() {
    }

    public ModuleCriteria(ModuleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.semestre = other.semestre == null ? null : other.semestre.copy();
        this.filiereId = other.filiereId == null ? null : other.filiereId.copy();
        this.historiqueEtudiantModuleId = other.historiqueEtudiantModuleId == null ? null : other.historiqueEtudiantModuleId.copy();
        this.historiqueEnseignantModuleId = other.historiqueEnseignantModuleId == null ? null : other.historiqueEnseignantModuleId.copy();
    }

    @Override
    public ModuleCriteria copy() {
        return new ModuleCriteria(this);
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

    public IntegerFilter getSemestre() {
        return semestre;
    }

    public void setSemestre(IntegerFilter semestre) {
        this.semestre = semestre;
    }

    public LongFilter getFiliereId() {
        return filiereId;
    }

    public void setFiliereId(LongFilter filiereId) {
        this.filiereId = filiereId;
    }

    public LongFilter getHistoriqueEtudiantModuleId() {
        return historiqueEtudiantModuleId;
    }

    public void setHistoriqueEtudiantModuleId(LongFilter historiqueEtudiantModuleId) {
        this.historiqueEtudiantModuleId = historiqueEtudiantModuleId;
    }

    public LongFilter getHistoriqueEnseignantModuleId() {
        return historiqueEnseignantModuleId;
    }

    public void setHistoriqueEnseignantModuleId(LongFilter historiqueEnseignantModuleId) {
        this.historiqueEnseignantModuleId = historiqueEnseignantModuleId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ModuleCriteria that = (ModuleCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(semestre, that.semestre) &&
            Objects.equals(filiereId, that.filiereId) &&
            Objects.equals(historiqueEtudiantModuleId, that.historiqueEtudiantModuleId) &&
            Objects.equals(historiqueEnseignantModuleId, that.historiqueEnseignantModuleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nom,
        semestre,
        filiereId,
        historiqueEtudiantModuleId,
        historiqueEnseignantModuleId
        );
    }

    @Override
    public String toString() {
        return "ModuleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (semestre != null ? "semestre=" + semestre + ", " : "") +
                (filiereId != null ? "filiereId=" + filiereId + ", " : "") +
                (historiqueEtudiantModuleId != null ? "historiqueEtudiantModuleId=" + historiqueEtudiantModuleId + ", " : "") +
                (historiqueEnseignantModuleId != null ? "historiqueEnseignantModuleId=" + historiqueEnseignantModuleId + ", " : "") +
            "}";
    }

}
