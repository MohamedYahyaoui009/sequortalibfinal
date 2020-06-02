package com.pfe.sequortalibfinal.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.pfe.sequortalibfinal.domain.enumeration.TypeCycle;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.pfe.sequortalibfinal.domain.Etablissement} entity. This class is used
 * in {@link com.pfe.sequortalibfinal.web.rest.EtablissementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /etablissements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EtablissementCriteria implements Serializable, Criteria {
    /**
     * Class for filtering TypeCycle
     */
    public static class TypeCycleFilter extends Filter<TypeCycle> {

        public TypeCycleFilter() {
        }

        public TypeCycleFilter(TypeCycleFilter filter) {
            super(filter);
        }

        @Override
        public TypeCycleFilter copy() {
            return new TypeCycleFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter filiere;

    private TypeCycleFilter cycle;

    private LongFilter etudiantId;

    public EtablissementCriteria() {
    }

    public EtablissementCriteria(EtablissementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.filiere = other.filiere == null ? null : other.filiere.copy();
        this.cycle = other.cycle == null ? null : other.cycle.copy();
        this.etudiantId = other.etudiantId == null ? null : other.etudiantId.copy();
    }

    @Override
    public EtablissementCriteria copy() {
        return new EtablissementCriteria(this);
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

    public StringFilter getFiliere() {
        return filiere;
    }

    public void setFiliere(StringFilter filiere) {
        this.filiere = filiere;
    }

    public TypeCycleFilter getCycle() {
        return cycle;
    }

    public void setCycle(TypeCycleFilter cycle) {
        this.cycle = cycle;
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
        final EtablissementCriteria that = (EtablissementCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(filiere, that.filiere) &&
            Objects.equals(cycle, that.cycle) &&
            Objects.equals(etudiantId, that.etudiantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nom,
        filiere,
        cycle,
        etudiantId
        );
    }

    @Override
    public String toString() {
        return "EtablissementCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (filiere != null ? "filiere=" + filiere + ", " : "") +
                (cycle != null ? "cycle=" + cycle + ", " : "") +
                (etudiantId != null ? "etudiantId=" + etudiantId + ", " : "") +
            "}";
    }

}
