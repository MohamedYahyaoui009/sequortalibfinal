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
 * Criteria class for the {@link com.pfe.sequortalibfinal.domain.HistoriqueEtudiantFiliere} entity. This class is used
 * in {@link com.pfe.sequortalibfinal.web.rest.HistoriqueEtudiantFiliereResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /historique-etudiant-filieres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HistoriqueEtudiantFiliereCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter datedebut;

    private LocalDateFilter datefin;

    private LongFilter etudiantId;

    public HistoriqueEtudiantFiliereCriteria() {
    }

    public HistoriqueEtudiantFiliereCriteria(HistoriqueEtudiantFiliereCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.datedebut = other.datedebut == null ? null : other.datedebut.copy();
        this.datefin = other.datefin == null ? null : other.datefin.copy();
        this.etudiantId = other.etudiantId == null ? null : other.etudiantId.copy();
    }

    @Override
    public HistoriqueEtudiantFiliereCriteria copy() {
        return new HistoriqueEtudiantFiliereCriteria(this);
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
        final HistoriqueEtudiantFiliereCriteria that = (HistoriqueEtudiantFiliereCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(datedebut, that.datedebut) &&
            Objects.equals(datefin, that.datefin) &&
            Objects.equals(etudiantId, that.etudiantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        datedebut,
        datefin,
        etudiantId
        );
    }

    @Override
    public String toString() {
        return "HistoriqueEtudiantFiliereCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (datedebut != null ? "datedebut=" + datedebut + ", " : "") +
                (datefin != null ? "datefin=" + datefin + ", " : "") +
                (etudiantId != null ? "etudiantId=" + etudiantId + ", " : "") +
            "}";
    }

}
