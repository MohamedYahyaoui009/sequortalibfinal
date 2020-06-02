package com.pfe.sequortalibfinal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

import com.pfe.sequortalibfinal.domain.enumeration.Status;

/**
 * A Etudiant.
 */
@Entity
@Table(name = "etudiant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "etudiant")
public class Etudiant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "semestre", nullable = false)
    private Integer semestre;

    @NotNull
    @Column(name = "section", nullable = false)
    private String section;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "etat", nullable = false)
    private Status etat;

    @ManyToOne
    @JsonIgnoreProperties("etudiants")
    private HistoriqueEtudiantModule historiqueEtudiantModule;

    @ManyToOne
    @JsonIgnoreProperties("etudiants")
    private HistoriqueEtudiantFiliere historiqueEtudiantFiliere;

    @ManyToOne
    @JsonIgnoreProperties("etudiants")
    private Etablissement etablissement;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public Etudiant semestre(Integer semestre) {
        this.semestre = semestre;
        return this;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public String getSection() {
        return section;
    }

    public Etudiant section(String section) {
        this.section = section;
        return this;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Status getEtat() {
        return etat;
    }

    public Etudiant etat(Status etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(Status etat) {
        this.etat = etat;
    }

    public HistoriqueEtudiantModule getHistoriqueEtudiantModule() {
        return historiqueEtudiantModule;
    }

    public Etudiant historiqueEtudiantModule(HistoriqueEtudiantModule historiqueEtudiantModule) {
        this.historiqueEtudiantModule = historiqueEtudiantModule;
        return this;
    }

    public void setHistoriqueEtudiantModule(HistoriqueEtudiantModule historiqueEtudiantModule) {
        this.historiqueEtudiantModule = historiqueEtudiantModule;
    }

    public HistoriqueEtudiantFiliere getHistoriqueEtudiantFiliere() {
        return historiqueEtudiantFiliere;
    }

    public Etudiant historiqueEtudiantFiliere(HistoriqueEtudiantFiliere historiqueEtudiantFiliere) {
        this.historiqueEtudiantFiliere = historiqueEtudiantFiliere;
        return this;
    }

    public void setHistoriqueEtudiantFiliere(HistoriqueEtudiantFiliere historiqueEtudiantFiliere) {
        this.historiqueEtudiantFiliere = historiqueEtudiantFiliere;
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }

    public Etudiant etablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
        return this;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etudiant)) {
            return false;
        }
        return id != null && id.equals(((Etudiant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Etudiant{" +
            "id=" + getId() +
            ", semestre=" + getSemestre() +
            ", section='" + getSection() + "'" +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
