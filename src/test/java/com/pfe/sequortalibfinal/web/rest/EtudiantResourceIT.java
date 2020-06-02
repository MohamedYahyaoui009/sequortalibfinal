package com.pfe.sequortalibfinal.web.rest;

import com.pfe.sequortalibfinal.SequortalibfinalApp;
import com.pfe.sequortalibfinal.domain.Etudiant;
import com.pfe.sequortalibfinal.domain.HistoriqueEtudiantModule;
import com.pfe.sequortalibfinal.domain.HistoriqueEtudiantFiliere;
import com.pfe.sequortalibfinal.domain.Etablissement;
import com.pfe.sequortalibfinal.repository.EtudiantRepository;
import com.pfe.sequortalibfinal.repository.search.EtudiantSearchRepository;
import com.pfe.sequortalibfinal.service.EtudiantService;
import com.pfe.sequortalibfinal.service.dto.EtudiantCriteria;
import com.pfe.sequortalibfinal.service.EtudiantQueryService;

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

import com.pfe.sequortalibfinal.domain.enumeration.Status;
/**
 * Integration tests for the {@link EtudiantResource} REST controller.
 */
@SpringBootTest(classes = SequortalibfinalApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class EtudiantResourceIT {

    private static final Integer DEFAULT_SEMESTRE = 1;
    private static final Integer UPDATED_SEMESTRE = 2;
    private static final Integer SMALLER_SEMESTRE = 1 - 1;

    private static final String DEFAULT_SECTION = "AAAAAAAAAA";
    private static final String UPDATED_SECTION = "BBBBBBBBBB";

    private static final Status DEFAULT_ETAT = Status.DIPLOME;
    private static final Status UPDATED_ETAT = Status.NONDIPLOME;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private EtudiantService etudiantService;

    /**
     * This repository is mocked in the com.pfe.sequortalibfinal.repository.search test package.
     *
     * @see com.pfe.sequortalibfinal.repository.search.EtudiantSearchRepositoryMockConfiguration
     */
    @Autowired
    private EtudiantSearchRepository mockEtudiantSearchRepository;

    @Autowired
    private EtudiantQueryService etudiantQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtudiantMockMvc;

    private Etudiant etudiant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etudiant createEntity(EntityManager em) {
        Etudiant etudiant = new Etudiant()
            .semestre(DEFAULT_SEMESTRE)
            .section(DEFAULT_SECTION)
            .etat(DEFAULT_ETAT);
        return etudiant;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etudiant createUpdatedEntity(EntityManager em) {
        Etudiant etudiant = new Etudiant()
            .semestre(UPDATED_SEMESTRE)
            .section(UPDATED_SECTION)
            .etat(UPDATED_ETAT);
        return etudiant;
    }

    @BeforeEach
    public void initTest() {
        etudiant = createEntity(em);
    }

    @Test
    @Transactional
    public void createEtudiant() throws Exception {
        int databaseSizeBeforeCreate = etudiantRepository.findAll().size();

        // Create the Etudiant
        restEtudiantMockMvc.perform(post("/api/etudiants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(etudiant)))
            .andExpect(status().isCreated());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeCreate + 1);
        Etudiant testEtudiant = etudiantList.get(etudiantList.size() - 1);
        assertThat(testEtudiant.getSemestre()).isEqualTo(DEFAULT_SEMESTRE);
        assertThat(testEtudiant.getSection()).isEqualTo(DEFAULT_SECTION);
        assertThat(testEtudiant.getEtat()).isEqualTo(DEFAULT_ETAT);

        // Validate the Etudiant in Elasticsearch
        verify(mockEtudiantSearchRepository, times(1)).save(testEtudiant);
    }

    @Test
    @Transactional
    public void createEtudiantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = etudiantRepository.findAll().size();

        // Create the Etudiant with an existing ID
        etudiant.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtudiantMockMvc.perform(post("/api/etudiants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(etudiant)))
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeCreate);

        // Validate the Etudiant in Elasticsearch
        verify(mockEtudiantSearchRepository, times(0)).save(etudiant);
    }


    @Test
    @Transactional
    public void checkSemestreIsRequired() throws Exception {
        int databaseSizeBeforeTest = etudiantRepository.findAll().size();
        // set the field null
        etudiant.setSemestre(null);

        // Create the Etudiant, which fails.

        restEtudiantMockMvc.perform(post("/api/etudiants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(etudiant)))
            .andExpect(status().isBadRequest());

        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = etudiantRepository.findAll().size();
        // set the field null
        etudiant.setSection(null);

        // Create the Etudiant, which fails.

        restEtudiantMockMvc.perform(post("/api/etudiants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(etudiant)))
            .andExpect(status().isBadRequest());

        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEtatIsRequired() throws Exception {
        int databaseSizeBeforeTest = etudiantRepository.findAll().size();
        // set the field null
        etudiant.setEtat(null);

        // Create the Etudiant, which fails.

        restEtudiantMockMvc.perform(post("/api/etudiants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(etudiant)))
            .andExpect(status().isBadRequest());

        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEtudiants() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList
        restEtudiantMockMvc.perform(get("/api/etudiants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].semestre").value(hasItem(DEFAULT_SEMESTRE)))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }
    
    @Test
    @Transactional
    public void getEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get the etudiant
        restEtudiantMockMvc.perform(get("/api/etudiants/{id}", etudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etudiant.getId().intValue()))
            .andExpect(jsonPath("$.semestre").value(DEFAULT_SEMESTRE))
            .andExpect(jsonPath("$.section").value(DEFAULT_SECTION))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()));
    }


    @Test
    @Transactional
    public void getEtudiantsByIdFiltering() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        Long id = etudiant.getId();

        defaultEtudiantShouldBeFound("id.equals=" + id);
        defaultEtudiantShouldNotBeFound("id.notEquals=" + id);

        defaultEtudiantShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEtudiantShouldNotBeFound("id.greaterThan=" + id);

        defaultEtudiantShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEtudiantShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEtudiantsBySemestreIsEqualToSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where semestre equals to DEFAULT_SEMESTRE
        defaultEtudiantShouldBeFound("semestre.equals=" + DEFAULT_SEMESTRE);

        // Get all the etudiantList where semestre equals to UPDATED_SEMESTRE
        defaultEtudiantShouldNotBeFound("semestre.equals=" + UPDATED_SEMESTRE);
    }

    @Test
    @Transactional
    public void getAllEtudiantsBySemestreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where semestre not equals to DEFAULT_SEMESTRE
        defaultEtudiantShouldNotBeFound("semestre.notEquals=" + DEFAULT_SEMESTRE);

        // Get all the etudiantList where semestre not equals to UPDATED_SEMESTRE
        defaultEtudiantShouldBeFound("semestre.notEquals=" + UPDATED_SEMESTRE);
    }

    @Test
    @Transactional
    public void getAllEtudiantsBySemestreIsInShouldWork() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where semestre in DEFAULT_SEMESTRE or UPDATED_SEMESTRE
        defaultEtudiantShouldBeFound("semestre.in=" + DEFAULT_SEMESTRE + "," + UPDATED_SEMESTRE);

        // Get all the etudiantList where semestre equals to UPDATED_SEMESTRE
        defaultEtudiantShouldNotBeFound("semestre.in=" + UPDATED_SEMESTRE);
    }

    @Test
    @Transactional
    public void getAllEtudiantsBySemestreIsNullOrNotNull() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where semestre is not null
        defaultEtudiantShouldBeFound("semestre.specified=true");

        // Get all the etudiantList where semestre is null
        defaultEtudiantShouldNotBeFound("semestre.specified=false");
    }

    @Test
    @Transactional
    public void getAllEtudiantsBySemestreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where semestre is greater than or equal to DEFAULT_SEMESTRE
        defaultEtudiantShouldBeFound("semestre.greaterThanOrEqual=" + DEFAULT_SEMESTRE);

        // Get all the etudiantList where semestre is greater than or equal to UPDATED_SEMESTRE
        defaultEtudiantShouldNotBeFound("semestre.greaterThanOrEqual=" + UPDATED_SEMESTRE);
    }

    @Test
    @Transactional
    public void getAllEtudiantsBySemestreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where semestre is less than or equal to DEFAULT_SEMESTRE
        defaultEtudiantShouldBeFound("semestre.lessThanOrEqual=" + DEFAULT_SEMESTRE);

        // Get all the etudiantList where semestre is less than or equal to SMALLER_SEMESTRE
        defaultEtudiantShouldNotBeFound("semestre.lessThanOrEqual=" + SMALLER_SEMESTRE);
    }

    @Test
    @Transactional
    public void getAllEtudiantsBySemestreIsLessThanSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where semestre is less than DEFAULT_SEMESTRE
        defaultEtudiantShouldNotBeFound("semestre.lessThan=" + DEFAULT_SEMESTRE);

        // Get all the etudiantList where semestre is less than UPDATED_SEMESTRE
        defaultEtudiantShouldBeFound("semestre.lessThan=" + UPDATED_SEMESTRE);
    }

    @Test
    @Transactional
    public void getAllEtudiantsBySemestreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where semestre is greater than DEFAULT_SEMESTRE
        defaultEtudiantShouldNotBeFound("semestre.greaterThan=" + DEFAULT_SEMESTRE);

        // Get all the etudiantList where semestre is greater than SMALLER_SEMESTRE
        defaultEtudiantShouldBeFound("semestre.greaterThan=" + SMALLER_SEMESTRE);
    }


    @Test
    @Transactional
    public void getAllEtudiantsBySectionIsEqualToSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where section equals to DEFAULT_SECTION
        defaultEtudiantShouldBeFound("section.equals=" + DEFAULT_SECTION);

        // Get all the etudiantList where section equals to UPDATED_SECTION
        defaultEtudiantShouldNotBeFound("section.equals=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    public void getAllEtudiantsBySectionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where section not equals to DEFAULT_SECTION
        defaultEtudiantShouldNotBeFound("section.notEquals=" + DEFAULT_SECTION);

        // Get all the etudiantList where section not equals to UPDATED_SECTION
        defaultEtudiantShouldBeFound("section.notEquals=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    public void getAllEtudiantsBySectionIsInShouldWork() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where section in DEFAULT_SECTION or UPDATED_SECTION
        defaultEtudiantShouldBeFound("section.in=" + DEFAULT_SECTION + "," + UPDATED_SECTION);

        // Get all the etudiantList where section equals to UPDATED_SECTION
        defaultEtudiantShouldNotBeFound("section.in=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    public void getAllEtudiantsBySectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where section is not null
        defaultEtudiantShouldBeFound("section.specified=true");

        // Get all the etudiantList where section is null
        defaultEtudiantShouldNotBeFound("section.specified=false");
    }
                @Test
    @Transactional
    public void getAllEtudiantsBySectionContainsSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where section contains DEFAULT_SECTION
        defaultEtudiantShouldBeFound("section.contains=" + DEFAULT_SECTION);

        // Get all the etudiantList where section contains UPDATED_SECTION
        defaultEtudiantShouldNotBeFound("section.contains=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    public void getAllEtudiantsBySectionNotContainsSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where section does not contain DEFAULT_SECTION
        defaultEtudiantShouldNotBeFound("section.doesNotContain=" + DEFAULT_SECTION);

        // Get all the etudiantList where section does not contain UPDATED_SECTION
        defaultEtudiantShouldBeFound("section.doesNotContain=" + UPDATED_SECTION);
    }


    @Test
    @Transactional
    public void getAllEtudiantsByEtatIsEqualToSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where etat equals to DEFAULT_ETAT
        defaultEtudiantShouldBeFound("etat.equals=" + DEFAULT_ETAT);

        // Get all the etudiantList where etat equals to UPDATED_ETAT
        defaultEtudiantShouldNotBeFound("etat.equals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void getAllEtudiantsByEtatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where etat not equals to DEFAULT_ETAT
        defaultEtudiantShouldNotBeFound("etat.notEquals=" + DEFAULT_ETAT);

        // Get all the etudiantList where etat not equals to UPDATED_ETAT
        defaultEtudiantShouldBeFound("etat.notEquals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void getAllEtudiantsByEtatIsInShouldWork() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where etat in DEFAULT_ETAT or UPDATED_ETAT
        defaultEtudiantShouldBeFound("etat.in=" + DEFAULT_ETAT + "," + UPDATED_ETAT);

        // Get all the etudiantList where etat equals to UPDATED_ETAT
        defaultEtudiantShouldNotBeFound("etat.in=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void getAllEtudiantsByEtatIsNullOrNotNull() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where etat is not null
        defaultEtudiantShouldBeFound("etat.specified=true");

        // Get all the etudiantList where etat is null
        defaultEtudiantShouldNotBeFound("etat.specified=false");
    }

    @Test
    @Transactional
    public void getAllEtudiantsByHistoriqueEtudiantModuleIsEqualToSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);
        HistoriqueEtudiantModule historiqueEtudiantModule = HistoriqueEtudiantModuleResourceIT.createEntity(em);
        em.persist(historiqueEtudiantModule);
        em.flush();
        etudiant.setHistoriqueEtudiantModule(historiqueEtudiantModule);
        etudiantRepository.saveAndFlush(etudiant);
        Long historiqueEtudiantModuleId = historiqueEtudiantModule.getId();

        // Get all the etudiantList where historiqueEtudiantModule equals to historiqueEtudiantModuleId
        defaultEtudiantShouldBeFound("historiqueEtudiantModuleId.equals=" + historiqueEtudiantModuleId);

        // Get all the etudiantList where historiqueEtudiantModule equals to historiqueEtudiantModuleId + 1
        defaultEtudiantShouldNotBeFound("historiqueEtudiantModuleId.equals=" + (historiqueEtudiantModuleId + 1));
    }


    @Test
    @Transactional
    public void getAllEtudiantsByHistoriqueEtudiantFiliereIsEqualToSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);
        HistoriqueEtudiantFiliere historiqueEtudiantFiliere = HistoriqueEtudiantFiliereResourceIT.createEntity(em);
        em.persist(historiqueEtudiantFiliere);
        em.flush();
        etudiant.setHistoriqueEtudiantFiliere(historiqueEtudiantFiliere);
        etudiantRepository.saveAndFlush(etudiant);
        Long historiqueEtudiantFiliereId = historiqueEtudiantFiliere.getId();

        // Get all the etudiantList where historiqueEtudiantFiliere equals to historiqueEtudiantFiliereId
        defaultEtudiantShouldBeFound("historiqueEtudiantFiliereId.equals=" + historiqueEtudiantFiliereId);

        // Get all the etudiantList where historiqueEtudiantFiliere equals to historiqueEtudiantFiliereId + 1
        defaultEtudiantShouldNotBeFound("historiqueEtudiantFiliereId.equals=" + (historiqueEtudiantFiliereId + 1));
    }


    @Test
    @Transactional
    public void getAllEtudiantsByEtablissementIsEqualToSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);
        Etablissement etablissement = EtablissementResourceIT.createEntity(em);
        em.persist(etablissement);
        em.flush();
        etudiant.setEtablissement(etablissement);
        etudiantRepository.saveAndFlush(etudiant);
        Long etablissementId = etablissement.getId();

        // Get all the etudiantList where etablissement equals to etablissementId
        defaultEtudiantShouldBeFound("etablissementId.equals=" + etablissementId);

        // Get all the etudiantList where etablissement equals to etablissementId + 1
        defaultEtudiantShouldNotBeFound("etablissementId.equals=" + (etablissementId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEtudiantShouldBeFound(String filter) throws Exception {
        restEtudiantMockMvc.perform(get("/api/etudiants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].semestre").value(hasItem(DEFAULT_SEMESTRE)))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));

        // Check, that the count call also returns 1
        restEtudiantMockMvc.perform(get("/api/etudiants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEtudiantShouldNotBeFound(String filter) throws Exception {
        restEtudiantMockMvc.perform(get("/api/etudiants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEtudiantMockMvc.perform(get("/api/etudiants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEtudiant() throws Exception {
        // Get the etudiant
        restEtudiantMockMvc.perform(get("/api/etudiants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEtudiant() throws Exception {
        // Initialize the database
        etudiantService.save(etudiant);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockEtudiantSearchRepository);

        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();

        // Update the etudiant
        Etudiant updatedEtudiant = etudiantRepository.findById(etudiant.getId()).get();
        // Disconnect from session so that the updates on updatedEtudiant are not directly saved in db
        em.detach(updatedEtudiant);
        updatedEtudiant
            .semestre(UPDATED_SEMESTRE)
            .section(UPDATED_SECTION)
            .etat(UPDATED_ETAT);

        restEtudiantMockMvc.perform(put("/api/etudiants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEtudiant)))
            .andExpect(status().isOk());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
        Etudiant testEtudiant = etudiantList.get(etudiantList.size() - 1);
        assertThat(testEtudiant.getSemestre()).isEqualTo(UPDATED_SEMESTRE);
        assertThat(testEtudiant.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testEtudiant.getEtat()).isEqualTo(UPDATED_ETAT);

        // Validate the Etudiant in Elasticsearch
        verify(mockEtudiantSearchRepository, times(1)).save(testEtudiant);
    }

    @Test
    @Transactional
    public void updateNonExistingEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();

        // Create the Etudiant

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtudiantMockMvc.perform(put("/api/etudiants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(etudiant)))
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Etudiant in Elasticsearch
        verify(mockEtudiantSearchRepository, times(0)).save(etudiant);
    }

    @Test
    @Transactional
    public void deleteEtudiant() throws Exception {
        // Initialize the database
        etudiantService.save(etudiant);

        int databaseSizeBeforeDelete = etudiantRepository.findAll().size();

        // Delete the etudiant
        restEtudiantMockMvc.perform(delete("/api/etudiants/{id}", etudiant.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Etudiant in Elasticsearch
        verify(mockEtudiantSearchRepository, times(1)).deleteById(etudiant.getId());
    }

    @Test
    @Transactional
    public void searchEtudiant() throws Exception {
        // Initialize the database
        etudiantService.save(etudiant);
        when(mockEtudiantSearchRepository.search(queryStringQuery("id:" + etudiant.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(etudiant), PageRequest.of(0, 1), 1));
        // Search the etudiant
        restEtudiantMockMvc.perform(get("/api/_search/etudiants?query=id:" + etudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].semestre").value(hasItem(DEFAULT_SEMESTRE)))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }
}
