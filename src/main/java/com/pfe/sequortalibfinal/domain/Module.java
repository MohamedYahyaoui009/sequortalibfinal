package com.pfe.sequortalibfinal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Module.
 */
@Entity
@Table(name = "module")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "module")
public class Module implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "semestre")
    private Integer semestre;

    @ManyToMany(mappedBy = "modules")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Filiere> filieres = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("modules")
    private HistoriqueEtudiantModule historiqueEtudiantModule;

    @ManyToOne
    @JsonIgnoreProperties("modules")
    private HistoriqueEnseignantModule historiqueEnseignantModule;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Module nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public Module semestre(Integer semestre) {
        this.semestre = semestre;
        return this;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public Set<Filiere> getFilieres() {
        return filieres;
    }

    public Module filieres(Set<Filiere> filieres) {
        this.filieres = filieres;
        return this;
    }

    public Module addFiliere(Filiere filiere) {
        this.filieres.add(filiere);
        filiere.getModules().add(this);
        return this;
    }

    public Module removeFiliere(Filiere filiere) {
        this.filieres.remove(filiere);
        filiere.getModules().remove(this);
        return this;
    }

    public void setFilieres(Set<Filiere> filieres) {
        this.filieres = filieres;
    }

    public HistoriqueEtudiantModule getHistoriqueEtudiantModule() {
        return historiqueEtudiantModule;
    }

    public Module historiqueEtudiantModule(HistoriqueEtudiantModule historiqueEtudiantModule) {
        this.historiqueEtudiantModule = historiqueEtudiantModule;
        return this;
    }

    public void setHistoriqueEtudiantModule(HistoriqueEtudiantModule historiqueEtudiantModule) {
        this.historiqueEtudiantModule = historiqueEtudiantModule;
    }

    public HistoriqueEnseignantModule getHistoriqueEnseignantModule() {
        return historiqueEnseignantModule;
    }

    public Module historiqueEnseignantModule(HistoriqueEnseignantModule historiqueEnseignantModule) {
        this.historiqueEnseignantModule = historiqueEnseignantModule;
        return this;
    }

    public void setHistoriqueEnseignantModule(HistoriqueEnseignantModule historiqueEnseignantModule) {
        this.historiqueEnseignantModule = historiqueEnseignantModule;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Module)) {
            return false;
        }
        return id != null && id.equals(((Module) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Module{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", semestre=" + getSemestre() +
            "}";
    }
}
