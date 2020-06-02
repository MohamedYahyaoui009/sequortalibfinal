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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.pfe.sequortalibfinal.domain.HistoriqueEnseignantModule} entity. This class is used
 * in {@link com.pfe.sequortalibfinal.web.rest.HistoriqueEnseignantModuleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /historique-enseignant-modules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HistoriqueEnseignantModuleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter datedebut;

    private LocalDateFilter datefin;

    private LongFilter moduleId;

    private LongFilter enseignantId;

    public HistoriqueEnseignantModuleCriteria() {
    }

    public HistoriqueEnseignantModuleCriteria(HistoriqueEnseignantModuleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.datedebut = other.datedebut == null ? null : other.datedebut.copy();
        this.datefin = other.datefin == null ? null : other.datefin.copy();
        this.moduleId = other.moduleId == null ? null : other.moduleId.copy();
        this.enseignantId = other.enseignantId == null ? null : other.enseignantId.copy();
    }

    @Override
    public HistoriqueEnseignantModuleCriteria copy() {
        return new HistoriqueEnseignantModuleCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDatedebut() {
        return datedebut;
    }

    public void setDatedebut(LocalDateFilter datedebut) {
        this.datedebut = datedebut;
    }

    public LocalDateFilter getDatefin() {
        return datefin;
    }

    public void setDatefin(LocalDateFilter datefin) {
        this.datefin = datefin;
    }

    public LongFilter getModuleId() {
        return moduleId;
    }

    public void setModuleId(LongFilter moduleId) {
        this.moduleId = moduleId;
    }

    public LongFilter getEnseignantId() {
        return enseignantId;
    }

    public void setEnseignantId(LongFilter enseignantId) {
        this.enseignantId = enseignantId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HistoriqueEnseignantModuleCriteria that = (HistoriqueEnseignantModuleCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(datedebut, that.datedebut) &&
            Objects.equals(datefin, that.datefin) &&
            Objects.equals(moduleId, that.moduleId) &&
            Objects.equals(enseignantId, that.enseignantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        datedebut,
        datefin,
        moduleId,
        enseignantId
        );
    }

    @Override
    public String toString() {
        return "HistoriqueEnseignantModuleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (datedebut != null ? "datedebut=" + datedebut + ", " : "") +
                (datefin != null ? "datefin=" + datefin + ", " : "") +
                (moduleId != null ? "moduleId=" + moduleId + ", " : "") +
                (enseignantId != null ? "enseignantId=" + enseignantId + ", " : "") +
            "}";
    }

}
