package com.pfe.sequortalibfinal.web.rest;

import com.pfe.sequortalibfinal.SequortalibfinalApp;
import com.pfe.sequortalibfinal.domain.Etablissement;
import com.pfe.sequortalibfinal.domain.Etudiant;
import com.pfe.sequortalibfinal.repository.EtablissementRepository;
import com.pfe.sequortalibfinal.repository.search.EtablissementSearchRepository;
import com.pfe.sequortalibfinal.service.EtablissementService;
import com.pfe.sequortalibfinal.service.dto.EtablissementCriteria;
import com.pfe.sequortalibfinal.service.EtablissementQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pfe.sequortalibfinal.domain.enumeration.TypeCycle;
/**
 * Integration tests for the {@link EtablissementResource} REST controller.
 */
@SpringBootTest(classes = SequortalibfinalApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class EtablissementResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_FILIERE = "AAAAAAAAAA";
    private static final String UPDATED_FILIERE = "BBBBBBBBBB";

    private static final TypeCycle DEFAULT_CYCLE = TypeCycle.MASTER;
    private static final TypeCycle UPDATED_CYCLE = TypeCycle.INGENIERIE;

    @Autowired
    private EtablissementRepository etablissementRepository;

    @Autowired
    private EtablissementService etablissementService;

    /**
     * This repository is mocked in the com.pfe.sequortalibfinal.repository.search test package.
     *
     * @see com.pfe.sequortalibfinal.repository.search.EtablissementSearchRepositoryMockConfiguration
     */
    @Autowired
    private EtablissementSearchRepository mockEtablissementSearchRepository;

    @Autowired
    private EtablissementQueryService etablissementQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtablissementMockMvc;

    private Etablissement etablissement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etablissement createEntity(EntityManager em) {
        Etablissement etablissement = new Etablissement()
            .nom(DEFAULT_NOM)
            .filiere(DEFAULT_FILIERE)
            .cycle(DEFAULT_CYCLE);
        return etablissement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etablissement createUpdatedEntity(EntityManager em) {
        Etablissement etablissement = new Etablissement()
            .nom(UPDATED_NOM)
            .filiere(UPDATED_FILIERE)
            .cycle(UPDATED_CYCLE);
        return etablissement;
    }

    @BeforeEach
    public void initTest() {
        etablissement = createEntity(em);
    }

    @Test
    @Transactional
    public void createEtablissement() throws Exception {
        int databaseSizeBeforeCreate = etablissementRepository.findAll().size();

        // Create the Etablissement
        restEtablissementMockMvc.perform(post("/api/etablissements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isCreated());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeCreate + 1);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEtablissement.getFiliere()).isEqualTo(DEFAULT_FILIERE);
        assertThat(testEtablissement.getCycle()).isEqualTo(DEFAULT_CYCLE);

        // Validate the Etablissement in Elasticsearch
        verify(mockEtablissementSearchRepository, times(1)).save(testEtablissement);
    }

    @Test
    @Transactional
    public void createEtablissementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = etablissementRepository.findAll().size();

        // Create the Etablissement with an existing ID
        etablissement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtablissementMockMvc.perform(post("/api/etablissements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isBadRequest());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeCreate);

        // Validate the Etablissement in Elasticsearch
        verify(mockEtablissementSearchRepository, times(0)).save(etablissement);
    }


    @Test
    @Transactional
    public void getAllEtablissements() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList
        restEtablissementMockMvc.perform(get("/api/etablissements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etablissement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].filiere").value(hasItem(DEFAULT_FILIERE)))
            .andExpect(jsonPath("$.[*].cycle").value(hasItem(DEFAULT_CYCLE.toString())));
    }
    
    @Test
    @Transactional
    public void getEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get the etablissement
        restEtablissementMockMvc.perform(get("/api/etablissements/{id}", etablissement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etablissement.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.filiere").value(DEFAULT_FILIERE))
            .andExpect(jsonPath("$.cycle").value(DEFAULT_CYCLE.toString()));
    }


    @Test
    @Transactional
    public void getEtablissementsByIdFiltering() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        Long id = etablissement.getId();

        defaultEtablissementShouldBeFound("id.equals=" + id);
        defaultEtablissementShouldNotBeFound("id.notEquals=" + id);

        defaultEtablissementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEtablissementShouldNotBeFound("id.greaterThan=" + id);

        defaultEtablissementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEtablissementShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEtablissementsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList where nom equals to DEFAULT_NOM
        defaultEtablissementShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the etablissementList where nom equals to UPDATED_NOM
        defaultEtablissementShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllEtablissementsByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList where nom not equals to DEFAULT_NOM
        defaultEtablissementShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the etablissementList where nom not equals to UPDATED_NOM
        defaultEtablissementShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllEtablissementsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultEtablissementShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the etablissementList where nom equals to UPDATED_NOM
        defaultEtablissementShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllEtablissementsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList where nom is not null
        defaultEtablissementShouldBeFound("nom.specified=true");

        // Get all the etablissementList where nom is null
        defaultEtablissementShouldNotBeFound("nom.specified=false");
    }
                @Test
    @Transactional
    public void getAllEtablissementsByNomContainsSomething() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList where nom contains DEFAULT_NOM
        defaultEtablissementShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the etablissementList where nom contains UPDATED_NOM
        defaultEtablissementShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllEtablissementsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList where nom does not contain DEFAULT_NOM
        defaultEtablissementShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the etablissementList where nom does not contain UPDATED_NOM
        defaultEtablissementShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }


    @Test
    @Transactional
    public void getAllEtablissementsByFiliereIsEqualToSomething() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList where filiere equals to DEFAULT_FILIERE
        defaultEtablissementShouldBeFound("filiere.equals=" + DEFAULT_FILIERE);

        // Get all the etablissementList where filiere equals to UPDATED_FILIERE
        defaultEtablissementShouldNotBeFound("filiere.equals=" + UPDATED_FILIERE);
    }

    @Test
    @Transactional
    public void getAllEtablissementsByFiliereIsNotEqualToSomething() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList where filiere not equals to DEFAULT_FILIERE
        defaultEtablissementShouldNotBeFound("filiere.notEquals=" + DEFAULT_FILIERE);

        // Get all the etablissementList where filiere not equals to UPDATED_FILIERE
        defaultEtablissementShouldBeFound("filiere.notEquals=" + UPDATED_FILIERE);
    }

    @Test
    @Transactional
    public void getAllEtablissementsByFiliereIsInShouldWork() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList where filiere in DEFAULT_FILIERE or UPDATED_FILIERE
        defaultEtablissementShouldBeFound("filiere.in=" + DEFAULT_FILIERE + "," + UPDATED_FILIERE);

        // Get all the etablissementList where filiere equals to UPDATED_FILIERE
        defaultEtablissementShouldNotBeFound("filiere.in=" + UPDATED_FILIERE);
    }

    @Test
    @Transactional
    public void getAllEtablissementsByFiliereIsNullOrNotNull() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList where filiere is not null
        defaultEtablissementShouldBeFound("filiere.specified=true");

        // Get all the etablissementList where filiere is null
        defaultEtablissementShouldNotBeFound("filiere.specified=false");
    }
                @Test
    @Transactional
    public void getAllEtablissementsByFiliereContainsSomething() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList where filiere contains DEFAULT_FILIERE
        defaultEtablissementShouldBeFound("filiere.contains=" + DEFAULT_FILIERE);

        // Get all the etablissementList where filiere contains UPDATED_FILIERE
        defaultEtablissementShouldNotBeFound("filiere.contains=" + UPDATED_FILIERE);
    }

    @Test
    @Transactional
    public void getAllEtablissementsByFiliereNotContainsSomething() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList where filiere does not contain DEFAULT_FILIERE
        defaultEtablissementShouldNotBeFound("filiere.doesNotContain=" + DEFAULT_FILIERE);

        // Get all the etablissementList where filiere does not contain UPDATED_FILIERE
        defaultEtablissementShouldBeFound("filiere.doesNotContain=" + UPDATED_FILIERE);
    }


    @Test
    @Transactional
    public void getAllEtablissementsByCycleIsEqualToSomething() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList where cycle equals to DEFAULT_CYCLE
        defaultEtablissementShouldBeFound("cycle.equals=" + DEFAULT_CYCLE);

        // Get all the etablissementList where cycle equals to UPDATED_CYCLE
        defaultEtablissementShouldNotBeFound("cycle.equals=" + UPDATED_CYCLE);
    }

    @Test
    @Transactional
    public void getAllEtablissementsByCycleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList where cycle not equals to DEFAULT_CYCLE
        defaultEtablissementShouldNotBeFound("cycle.notEquals=" + DEFAULT_CYCLE);

        // Get all the etablissementList where cycle not equals to UPDATED_CYCLE
        defaultEtablissementShouldBeFound("cycle.notEquals=" + UPDATED_CYCLE);
    }

    @Test
    @Transactional
    public void getAllEtablissementsByCycleIsInShouldWork() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList where cycle in DEFAULT_CYCLE or UPDATED_CYCLE
        defaultEtablissementShouldBeFound("cycle.in=" + DEFAULT_CYCLE + "," + UPDATED_CYCLE);

        // Get all the etablissementList where cycle equals to UPDATED_CYCLE
        defaultEtablissementShouldNotBeFound("cycle.in=" + UPDATED_CYCLE);
    }

    @Test
    @Transactional
    public void getAllEtablissementsByCycleIsNullOrNotNull() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList where cycle is not null
        defaultEtablissementShouldBeFound("cycle.specified=true");

        // Get all the etablissementList where cycle is null
        defaultEtablissementShouldNotBeFound("cycle.specified=false");
    }

    @Test
    @Transactional
    public void getAllEtablissementsByEtudiantIsEqualToSomething() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);
        Etudiant etudiant = EtudiantResourceIT.createEntity(em);
        em.persist(etudiant);
        em.flush();
        etablissement.addEtudiant(etudiant);
        etablissementRepository.saveAndFlush(etablissement);
        Long etudiantId = etudiant.getId();

        // Get all the etablissementList where etudiant equals to etudiantId
        defaultEtablissementShouldBeFound("etudiantId.equals=" + etudiantId);

        // Get all the etablissementList where etudiant equals to etudiantId + 1
        defaultEtablissementShouldNotBeFound("etudiantId.equals=" + (etudiantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEtablissementShouldBeFound(String filter) throws Exception {
        restEtablissementMockMvc.perform(get("/api/etablissements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etablissement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].filiere").value(hasItem(DEFAULT_FILIERE)))
            .andExpect(jsonPath("$.[*].cycle").value(hasItem(DEFAULT_CYCLE.toString())));

        // Check, that the count call also returns 1
        restEtablissementMockMvc.perform(get("/api/etablissements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEtablissementShouldNotBeFound(String filter) throws Exception {
        restEtablissementMockMvc.perform(get("/api/etablissements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEtablissementMockMvc.perform(get("/api/etablissements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEtablissement() throws Exception {
        // Get the etablissement
        restEtablissementMockMvc.perform(get("/api/etablissements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEtablissement() throws Exception {
        // Initialize the database
        etablissementService.save(etablissement);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockEtablissementSearchRepository);

        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();

        // Update the etablissement
        Etablissement updatedEtablissement = etablissementRepository.findById(etablissement.getId()).get();
        // Disconnect from session so that the updates on updatedEtablissement are not directly saved in db
        em.detach(updatedEtablissement);
        updatedEtablissement
            .nom(UPDATED_NOM)
            .filiere(UPDATED_FILIERE)
            .cycle(UPDATED_CYCLE);

        restEtablissementMockMvc.perform(put("/api/etablissements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEtablissement)))
            .andExpect(status().isOk());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEtablissement.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testEtablissement.getCycle()).isEqualTo(UPDATED_CYCLE);

        // Validate the Etablissement in Elasticsearch
        verify(mockEtablissementSearchRepository, times(1)).save(testEtablissement);
    }

    @Test
    @Transactional
    public void updateNonExistingEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();

        // Create the Etablissement

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtablissementMockMvc.perform(put("/api/etablissements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isBadRequest());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Etablissement in Elasticsearch
        verify(mockEtablissementSearchRepository, times(0)).save(etablissement);
    }

    @Test
    @Transactional
    public void deleteEtablissement() throws Exception {
        // Initialize the database
        etablissementService.save(etablissement);

        int databaseSizeBeforeDelete = etablissementRepository.findAll().size();

        // Delete the etablissement
        restEtablissementMockMvc.perform(delete("/api/etablissements/{id}", etablissement.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Etablissement in Elasticsearch
        verify(mockEtablissementSearchRepository, times(1)).deleteById(etablissement.getId());
    }

    @Test
    @Transactional
    public void searchEtablissement() throws Exception {
        // Initialize the database
        etablissementService.save(etablissement);
        when(mockEtablissementSearchRepository.search(queryStringQuery("id:" + etablissement.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(etablissement), PageRequest.of(0, 1), 1));
        // Search the etablissement
        restEtablissementMockMvc.perform(get("/api/_search/etablissements?query=id:" + etablissement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etablissement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].filiere").value(hasItem(DEFAULT_FILIERE)))
            .andExpect(jsonPath("$.[*].cycle").value(hasItem(DEFAULT_CYCLE.toString())));
    }
}
