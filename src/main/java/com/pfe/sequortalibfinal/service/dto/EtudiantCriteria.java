package com.pfe.sequortalibfinal.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.pfe.sequortalibfinal.domain.enumeration.Status;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.pfe.sequortalibfinal.domain.Etudiant} entity. This class is used
 * in {@link com.pfe.sequortalibfinal.web.rest.EtudiantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /etudiants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EtudiantCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {
        }

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter semestre;

    private StringFilter section;

    private StatusFilter etat;

    private LongFilter historiqueEtudiantModuleId;

    private LongFilter historiqueEtudiantFiliereId;

    private LongFilter etablissementId;

    public EtudiantCriteria() {
    }

    public EtudiantCriteria(EtudiantCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.semestre = other.semestre == null ? null : other.semestre.copy();
        this.section = other.section == null ? null : other.section.copy();
        this.etat = other.etat == null ? null : other.etat.copy();
        this.historiqueEtudiantModuleId = other.historiqueEtudiantModuleId == null ? null : other.historiqueEtudiantModuleId.copy();
        this.historiqueEtudiantFiliereId = other.historiqueEtudiantFiliereId == null ? null : other.historiqueEtudiantFiliereId.copy();
        this.etablissementId = other.etablissementId == null ? null : other.etablissementId.copy();
    }

    @Override
    public EtudiantCriteria copy() {
        return new EtudiantCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getSemestre() {
        return semestre;
    }

    public void setSemestre(IntegerFilter semestre) {
        this.semestre = semestre;
    }

    public StringFilter getSection() {
        return section;
    }

    public void setSection(StringFilter section) {
        this.section = section;
    }

    public StatusFilter getEtat() {
        return etat;
    }

    public void setEtat(StatusFilter etat) {
        this.etat = etat;
    }

    public LongFilter getHistoriqueEtudiantModuleId() {
        return historiqueEtudiantModuleId;
    }

    public void setHistoriqueEtudiantModuleId(LongFilter historiqueEtudiantModuleId) {
        this.historiqueEtudiantModuleId = historiqueEtudiantModuleId;
    }

    public LongFilter getHistoriqueEtudiantFiliereId() {
        return historiqueEtudiantFiliereId;
    }

    public void setHistoriqueEtudiantFiliereId(LongFilter historiqueEtudiantFiliereId) {
        this.historiqueEtudiantFiliereId = historiqueEtudiantFiliereId;
    }

    public LongFilter getEtablissementId() {
        return etablissementId;
    }

    public void setEtablissementId(LongFilter etablissementId) {
        this.etablissementId = etablissementId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EtudiantCriteria that = (EtudiantCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(semestre, that.semestre) &&
            Objects.equals(section, that.section) &&
            Objects.equals(etat, that.etat) &&
            Objects.equals(historiqueEtudiantModuleId, that.historiqueEtudiantModuleId) &&
            Objects.equals(historiqueEtudiantFiliereId, that.historiqueEtudiantFiliereId) &&
            Objects.equals(etablissementId, that.etablissementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        semestre,
        section,
        etat,
        historiqueEtudiantModuleId,
        historiqueEtudiantFiliereId,
        etablissementId
        );
    }

    @Override
    public String toString() {
        return "EtudiantCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (semestre != null ? "semestre=" + semestre + ", " : "") +
                (section != null ? "section=" + section + ", " : "") +
                (etat != null ? "etat=" + etat + ", " : "") +
                (historiqueEtudiantModuleId != null ? "historiqueEtudiantModuleId=" + historiqueEtudiantModuleId + ", " : "") +
                (historiqueEtudiantFiliereId != null ? "historiqueEtudiantFiliereId=" + historiqueEtudiantFiliereId + ", " : "") +
                (etablissementId != null ? "etablissementId=" + etablissementId + ", " : "") +
            "}";
    }

}
