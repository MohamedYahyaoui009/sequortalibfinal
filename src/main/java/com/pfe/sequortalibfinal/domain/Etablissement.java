package com.pfe.sequortalibfinal.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

import com.pfe.sequortalibfinal.domain.enumeration.TypeCycle;

/**
 * A Etablissement.
 */
@Entity
@Table(name = "etablissement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "etablissement")
public class Etablissement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "filiere")
    private String filiere;

    @Enumerated(EnumType.STRING)
    @Column(name = "cycle")
    private TypeCycle cycle;

    @OneToMany(mappedBy = "etablissement")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Etudiant> etudiants = new HashSet<>();

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

    public Etablissement nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getFiliere() {
        return filiere;
    }

    public Etablissement filiere(String filiere) {
        this.filiere = filiere;
        return this;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public TypeCycle getCycle() {
        return cycle;
    }

    public Etablissement cycle(TypeCycle cycle) {
        this.cycle = cycle;
        return this;
    }

    public void setCycle(TypeCycle cycle) {
        this.cycle = cycle;
    }

    public Set<Etudiant> getEtudiants() {
        return etudiants;
    }

    public Etablissement etudiants(Set<Etudiant> etudiants) {
        this.etudiants = etudiants;
        return this;
    }

    public Etablissement addEtudiant(Etudiant etudiant) {
        this.etudiants.add(etudiant);
        etudiant.setEtablissement(this);
        return this;
    }

    public Etablissement removeEtudiant(Etudiant etudiant) {
        this.etudiants.remove(etudiant);
        etudiant.setEtablissement(null);
        return this;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etablissement)) {
            return false;
        }
        return id != null && id.equals(((Etablissement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Etablissement{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", filiere='" + getFiliere() + "'" +
            ", cycle='" + getCycle() + "'" +
            "}";
    }
}
