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
 * Criteria class for the {@link com.pfe.sequortalibfinal.domain.HistoriqueEtudiantModule} entity. This class is used
 * in {@link com.pfe.sequortalibfinal.web.rest.HistoriqueEtudiantModuleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /historique-etudiant-modules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HistoriqueEtudiantModuleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter datedebut;

    private LocalDateFilter datefin;

    private DoubleFilter note;

    private LongFilter moduleId;

    private LongFilter etudiantId;

    public HistoriqueEtudiantModuleCriteria() {
    }

    public HistoriqueEtudiantModuleCriteria(HistoriqueEtudiantModuleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.datedebut = other.datedebut == null ? null : other.datedebut.copy();
        this.datefin = other.datefin == null ? null : other.datefin.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.moduleId = other.moduleId == null ? null : other.moduleId.copy();
        this.etudiantId = other.etudiantId == null ? null : other.etudiantId.copy();
    }

    @Override
    public HistoriqueEtudiantModuleCriteria copy() {
        return new HistoriqueEtudiantModuleCriteria(this);
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

    public DoubleFilter getNote() {
        return note;
    }

    public void setNote(DoubleFilter note) {
        this.note = note;
    }

    public LongFilter getModuleId() {
        return moduleId;
    }

    public void setModuleId(LongFilter moduleId) {
        this.moduleId = moduleId;
    }

    public LongFilter getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(LongFilter etudiantId) {
        this.etudiantId = etudiantId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HistoriqueEtudiantModuleCriteria that = (HistoriqueEtudiantModuleCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(datedebut, that.datedebut) &&
            Objects.equals(datefin, that.datefin) &&
            Objects.equals(note, that.note) &&
            Objects.equals(moduleId, that.moduleId) &&
            Objects.equals(etudiantId, that.etudiantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        datedebut,
        datefin,
        note,
        moduleId,
        etudiantId
        );
    }

    @Override
    public String toString() {
        return "HistoriqueEtudiantModuleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (datedebut != null ? "datedebut=" + datedebut + ", " : "") +
                (datefin != null ? "datefin=" + datefin + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (moduleId != null ? "moduleId=" + moduleId + ", " : "") +
                (etudiantId != null ? "etudiantId=" + etudiantId + ", " : "") +
            "}";
    }

}
