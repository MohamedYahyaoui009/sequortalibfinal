package com.pfe.sequortalibfinal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Enseignant.
 */
@Entity
@Table(name = "enseignant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "enseignant")
public class Enseignant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "grade", nullable = false)
    private String grade;

    @ManyToOne
    @JsonIgnoreProperties("enseignants")
    private HistoriqueEnseignantModule historiqueEnseignantModule;

    @ManyToOne
    @JsonIgnoreProperties("enseignants")
    private HistoriqueEnseignantFiliere historiqueEnseignantFiliere;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public Enseignant grade(String grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public HistoriqueEnseignantModule getHistoriqueEnseignantModule() {
        return historiqueEnseignantModule;
    }

    public Enseignant historiqueEnseignantModule(HistoriqueEnseignantModule historiqueEnseignantModule) {
        this.historiqueEnseignantModule = historiqueEnseignantModule;
        return this;
    }

    public void setHistoriqueEnseignantModule(HistoriqueEnseignantModule historiqueEnseignantModule) {
        this.historiqueEnseignantModule = historiqueEnseignantModule;
    }

    public HistoriqueEnseignantFiliere getHistoriqueEnseignantFiliere() {
        return historiqueEnseignantFiliere;
    }

    public Enseignant historiqueEnseignantFiliere(HistoriqueEnseignantFiliere historiqueEnseignantFiliere) {
        this.historiqueEnseignantFiliere = historiqueEnseignantFiliere;
        return this;
    }

    public void setHistoriqueEnseignantFiliere(HistoriqueEnseignantFiliere historiqueEnseignantFiliere) {
        this.historiqueEnseignantFiliere = historiqueEnseignantFiliere;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enseignant)) {
            return false;
        }
        return id != null && id.equals(((Enseignant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Enseignant{" +
            "id=" + getId() +
            ", grade='" + getGrade() + "'" +
            "}";
    }
}
