package com.pfe.sequortalibfinal.web.rest;

import com.pfe.sequortalibfinal.SequortalibfinalApp;
import com.pfe.sequortalibfinal.domain.Enseignant;
import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantModule;
import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantFiliere;
import com.pfe.sequortalibfinal.repository.EnseignantRepository;
import com.pfe.sequortalibfinal.repository.search.EnseignantSearchRepository;
import com.pfe.sequortalibfinal.service.EnseignantService;
import com.pfe.sequortalibfinal.service.dto.EnseignantCriteria;
import com.pfe.sequortalibfinal.service.EnseignantQueryService;

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

/**
 * Integration tests for the {@link EnseignantResource} REST controller.
 */
@SpringBootTest(classes = SequortalibfinalApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class EnseignantResourceIT {

    private static final String DEFAULT_GRADE = "AAAAAAAAAA";
    private static final String UPDATED_GRADE = "BBBBBBBBBB";

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private EnseignantService enseignantService;

    /**
     * This repository is mocked in the com.pfe.sequortalibfinal.repository.search test package.
     *
     * @see com.pfe.sequortalibfinal.repository.search.EnseignantSearchRepositoryMockConfiguration
     */
    @Autowired
    private EnseignantSearchRepository mockEnseignantSearchRepository;

    @Autowired
    private EnseignantQueryService enseignantQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnseignantMockMvc;

    private Enseignant enseignant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enseignant createEntity(EntityManager em) {
        Enseignant enseignant = new Enseignant()
            .grade(DEFAULT_GRADE);
        return enseignant;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enseignant createUpdatedEntity(EntityManager em) {
        Enseignant enseignant = new Enseignant()
            .grade(UPDATED_GRADE);
        return enseignant;
    }

    @BeforeEach
    public void initTest() {
        enseignant = createEntity(em);
    }

    @Test
    @Transactional
    public void createEnseignant() throws Exception {
        int databaseSizeBeforeCreate = enseignantRepository.findAll().size();

        // Create the Enseignant
        restEnseignantMockMvc.perform(post("/api/enseignants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(enseignant)))
            .andExpect(status().isCreated());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeCreate + 1);
        Enseignant testEnseignant = enseignantList.get(enseignantList.size() - 1);
        assertThat(testEnseignant.getGrade()).isEqualTo(DEFAULT_GRADE);

        // Validate the Enseignant in Elasticsearch
        verify(mockEnseignantSearchRepository, times(1)).save(testEnseignant);
    }

    @Test
    @Transactional
    public void createEnseignantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = enseignantRepository.findAll().size();

        // Create the Enseignant with an existing ID
        enseignant.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnseignantMockMvc.perform(post("/api/enseignants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(enseignant)))
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeCreate);

        // Validate the Enseignant in Elasticsearch
        verify(mockEnseignantSearchRepository, times(0)).save(enseignant);
    }


    @Test
    @Transactional
    public void checkGradeIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        // set the field null
        enseignant.setGrade(null);

        // Create the Enseignant, which fails.

        restEnseignantMockMvc.perform(post("/api/enseignants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(enseignant)))
            .andExpect(status().isBadRequest());

        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEnseignants() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList
        restEnseignantMockMvc.perform(get("/api/enseignants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enseignant.getId().intValue())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE)));
    }
    
    @Test
    @Transactional
    public void getEnseignant() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        // Get the enseignant
        restEnseignantMockMvc.perform(get("/api/enseignants/{id}", enseignant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enseignant.getId().intValue()))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE));
    }


    @Test
    @Transactional
    public void getEnseignantsByIdFiltering() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        Long id = enseignant.getId();

        defaultEnseignantShouldBeFound("id.equals=" + id);
        defaultEnseignantShouldNotBeFound("id.notEquals=" + id);

        defaultEnseignantShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEnseignantShouldNotBeFound("id.greaterThan=" + id);

        defaultEnseignantShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEnseignantShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEnseignantsByGradeIsEqualToSomething() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where grade equals to DEFAULT_GRADE
        defaultEnseignantShouldBeFound("grade.equals=" + DEFAULT_GRADE);

        // Get all the enseignantList where grade equals to UPDATED_GRADE
        defaultEnseignantShouldNotBeFound("grade.equals=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    public void getAllEnseignantsByGradeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where grade not equals to DEFAULT_GRADE
        defaultEnseignantShouldNotBeFound("grade.notEquals=" + DEFAULT_GRADE);

        // Get all the enseignantList where grade not equals to UPDATED_GRADE
        defaultEnseignantShouldBeFound("grade.notEquals=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    public void getAllEnseignantsByGradeIsInShouldWork() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where grade in DEFAULT_GRADE or UPDATED_GRADE
        defaultEnseignantShouldBeFound("grade.in=" + DEFAULT_GRADE + "," + UPDATED_GRADE);

        // Get all the enseignantList where grade equals to UPDATED_GRADE
        defaultEnseignantShouldNotBeFound("grade.in=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    public void getAllEnseignantsByGradeIsNullOrNotNull() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where grade is not null
        defaultEnseignantShouldBeFound("grade.specified=true");

        // Get all the enseignantList where grade is null
        defaultEnseignantShouldNotBeFound("grade.specified=false");
    }
                @Test
    @Transactional
    public void getAllEnseignantsByGradeContainsSomething() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where grade contains DEFAULT_GRADE
        defaultEnseignantShouldBeFound("grade.contains=" + DEFAULT_GRADE);

        // Get all the enseignantList where grade contains UPDATED_GRADE
        defaultEnseignantShouldNotBeFound("grade.contains=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    public void getAllEnseignantsByGradeNotContainsSomething() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where grade does not contain DEFAULT_GRADE
        defaultEnseignantShouldNotBeFound("grade.doesNotContain=" + DEFAULT_GRADE);

        // Get all the enseignantList where grade does not contain UPDATED_GRADE
        defaultEnseignantShouldBeFound("grade.doesNotContain=" + UPDATED_GRADE);
    }


    @Test
    @Transactional
    public void getAllEnseignantsByHistoriqueEnseignantModuleIsEqualToSomething() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);
        HistoriqueEnseignantModule historiqueEnseignantModule = HistoriqueEnseignantModuleResourceIT.createEntity(em);
        em.persist(historiqueEnseignantModule);
        em.flush();
        enseignant.setHistoriqueEnseignantModule(historiqueEnseignantModule);
        enseignantRepository.saveAndFlush(enseignant);
        Long historiqueEnseignantModuleId = historiqueEnseignantModule.getId();

        // Get all the enseignantList where historiqueEnseignantModule equals to historiqueEnseignantModuleId
        defaultEnseignantShouldBeFound("historiqueEnseignantModuleId.equals=" + historiqueEnseignantModuleId);

        // Get all the enseignantList where historiqueEnseignantModule equals to historiqueEnseignantModuleId + 1
        defaultEnseignantShouldNotBeFound("historiqueEnseignantModuleId.equals=" + (historiqueEnseignantModuleId + 1));
    }


    @Test
    @Transactional
    public void getAllEnseignantsByHistoriqueEnseignantFiliereIsEqualToSomething() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);
        HistoriqueEnseignantFiliere historiqueEnseignantFiliere = HistoriqueEnseignantFiliereResourceIT.createEntity(em);
        em.persist(historiqueEnseignantFiliere);
        em.flush();
        enseignant.setHistoriqueEnseignantFiliere(historiqueEnseignantFiliere);
        enseignantRepository.saveAndFlush(enseignant);
        Long historiqueEnseignantFiliereId = historiqueEnseignantFiliere.getId();

        // Get all the enseignantList where historiqueEnseignantFiliere equals to historiqueEnseignantFiliereId
        defaultEnseignantShouldBeFound("historiqueEnseignantFiliereId.equals=" + historiqueEnseignantFiliereId);

        // Get all the enseignantList where historiqueEnseignantFiliere equals to historiqueEnseignantFiliereId + 1
        defaultEnseignantShouldNotBeFound("historiqueEnseignantFiliereId.equals=" + (historiqueEnseignantFiliereId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEnseignantShouldBeFound(String filter) throws Exception {
        restEnseignantMockMvc.perform(get("/api/enseignants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enseignant.getId().intValue())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE)));

        // Check, that the count call also returns 1
        restEnseignantMockMvc.perform(get("/api/enseignants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEnseignantShouldNotBeFound(String filter) throws Exception {
        restEnseignantMockMvc.perform(get("/api/enseignants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEnseignantMockMvc.perform(get("/api/enseignants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEnseignant() throws Exception {
        // Get the enseignant
        restEnseignantMockMvc.perform(get("/api/enseignants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEnseignant() throws Exception {
        // Initialize the database
        enseignantService.save(enseignant);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockEnseignantSearchRepository);

        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();

        // Update the enseignant
        Enseignant updatedEnseignant = enseignantRepository.findById(enseignant.getId()).get();
        // Disconnect from session so that the updates on updatedEnseignant are not directly saved in db
        em.detach(updatedEnseignant);
        updatedEnseignant
            .grade(UPDATED_GRADE);

        restEnseignantMockMvc.perform(put("/api/enseignants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEnseignant)))
            .andExpect(status().isOk());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
        Enseignant testEnseignant = enseignantList.get(enseignantList.size() - 1);
        assertThat(testEnseignant.getGrade()).isEqualTo(UPDATED_GRADE);

        // Validate the Enseignant in Elasticsearch
        verify(mockEnseignantSearchRepository, times(1)).save(testEnseignant);
    }

    @Test
    @Transactional
    public void updateNonExistingEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();

        // Create the Enseignant

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnseignantMockMvc.perform(put("/api/enseignants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(enseignant)))
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Enseignant in Elasticsearch
        verify(mockEnseignantSearchRepository, times(0)).save(enseignant);
    }

    @Test
    @Transactional
    public void deleteEnseignant() throws Exception {
        // Initialize the database
        enseignantService.save(enseignant);

        int databaseSizeBeforeDelete = enseignantRepository.findAll().size();

        // Delete the enseignant
        restEnseignantMockMvc.perform(delete("/api/enseignants/{id}", enseignant.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Enseignant in Elasticsearch
        verify(mockEnseignantSearchRepository, times(1)).deleteById(enseignant.getId());
    }

    @Test
    @Transactional
    public void searchEnseignant() throws Exception {
        // Initialize the database
        enseignantService.save(enseignant);
        when(mockEnseignantSearchRepository.search(queryStringQuery("id:" + enseignant.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(enseignant), PageRequest.of(0, 1), 1));
        // Search the enseignant
        restEnseignantMockMvc.perform(get("/api/_search/enseignants?query=id:" + enseignant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enseignant.getId().intValue())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE)));
    }
}
