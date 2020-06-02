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
 * Criteria class for the {@link com.pfe.sequortalibfinal.domain.Enseignant} entity. This class is used
 * in {@link com.pfe.sequortalibfinal.web.rest.EnseignantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /enseignants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EnseignantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter grade;

    private LongFilter historiqueEnseignantModuleId;

    private LongFilter historiqueEnseignantFiliereId;

    public EnseignantCriteria() {
    }

    public EnseignantCriteria(EnseignantCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.grade = other.grade == null ? null : other.grade.copy();
        this.historiqueEnseignantModuleId = other.historiqueEnseignantModuleId == null ? null : other.historiqueEnseignantModuleId.copy();
        this.historiqueEnseignantFiliereId = other.historiqueEnseignantFiliereId == null ? null : other.historiqueEnseignantFiliereId.copy();
    }

    @Override
    public EnseignantCriteria copy() {
        return new EnseignantCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getGrade() {
        return grade;
    }

    public void setGrade(StringFilter grade) {
        this.grade = grade;
    }

    public LongFilter getHistoriqueEnseignantModuleId() {
        return historiqueEnseignantModuleId;
    }

    public void setHistoriqueEnseignantModuleId(LongFilter historiqueEnseignantModuleId) {
        this.historiqueEnseignantModuleId = historiqueEnseignantModuleId;
    }

    public LongFilter getHistoriqueEnseignantFiliereId() {
        return historiqueEnseignantFiliereId;
    }

    public void setHistoriqueEnseignantFiliereId(LongFilter historiqueEnseignantFiliereId) {
        this.historiqueEnseignantFiliereId = historiqueEnseignantFiliereId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EnseignantCriteria that = (EnseignantCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(grade, that.grade) &&
            Objects.equals(historiqueEnseignantModuleId, that.historiqueEnseignantModuleId) &&
            Objects.equals(historiqueEnseignantFiliereId, that.historiqueEnseignantFiliereId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        grade,
        historiqueEnseignantModuleId,
        historiqueEnseignantFiliereId
        );
    }

    @Override
    public String toString() {
        return "EnseignantCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (grade != null ? "grade=" + grade + ", " : "") +
                (historiqueEnseignantModuleId != null ? "historiqueEnseignantModuleId=" + historiqueEnseignantModuleId + ", " : "") +
                (historiqueEnseignantFiliereId != null ? "historiqueEnseignantFiliereId=" + historiqueEnseignantFiliereId + ", " : "") +
            "}";
    }

}
