package com.pfe.sequortalibfinal.web.rest;

import com.pfe.sequortalibfinal.SequortalibfinalApp;
import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantModule;
import com.pfe.sequortalibfinal.domain.Module;
import com.pfe.sequortalibfinal.domain.Enseignant;
import com.pfe.sequortalibfinal.repository.HistoriqueEnseignantModuleRepository;
import com.pfe.sequortalibfinal.repository.search.HistoriqueEnseignantModuleSearchRepository;
import com.pfe.sequortalibfinal.service.HistoriqueEnseignantModuleService;
import com.pfe.sequortalibfinal.service.dto.HistoriqueEnseignantModuleCriteria;
import com.pfe.sequortalibfinal.service.HistoriqueEnseignantModuleQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link HistoriqueEnseignantModuleResource} REST controller.
 */
@SpringBootTest(classes = SequortalibfinalApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class HistoriqueEnseignantModuleResourceIT {

    private static final LocalDate DEFAULT_DATEDEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEDEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATEDEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATEFIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEFIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATEFIN = LocalDate.ofEpochDay(-1L);

    @Autowired
    private HistoriqueEnseignantModuleRepository historiqueEnseignantModuleRepository;

    @Autowired
    private HistoriqueEnseignantModuleService historiqueEnseignantModuleService;

    /**
     * This repository is mocked in the com.pfe.sequortalibfinal.repository.search test package.
     *
     * @see com.pfe.sequortalibfinal.repository.search.HistoriqueEnseignantModuleSearchRepositoryMockConfiguration
     */
    @Autowired
    private HistoriqueEnseignantModuleSearchRepository mockHistoriqueEnseignantModuleSearchRepository;

    @Autowired
    private HistoriqueEnseignantModuleQueryService historiqueEnseignantModuleQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriqueEnseignantModuleMockMvc;

    private HistoriqueEnseignantModule historiqueEnseignantModule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueEnseignantModule createEntity(EntityManager em) {
        HistoriqueEnseignantModule historiqueEnseignantModule = new HistoriqueEnseignantModule()
            .datedebut(DEFAULT_DATEDEBUT)
            .datefin(DEFAULT_DATEFIN);
        return historiqueEnseignantModule;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueEnseignantModule createUpdatedEntity(EntityManager em) {
        HistoriqueEnseignantModule historiqueEnseignantModule = new HistoriqueEnseignantModule()
            .datedebut(UPDATED_DATEDEBUT)
            .datefin(UPDATED_DATEFIN);
        return historiqueEnseignantModule;
    }

    @BeforeEach
    public void initTest() {
        historiqueEnseignantModule = createEntity(em);
    }

    @Test
    @Transactional
    public void createHistoriqueEnseignantModule() throws Exception {
        int databaseSizeBeforeCreate = historiqueEnseignantModuleRepository.findAll().size();

        // Create the HistoriqueEnseignantModule
        restHistoriqueEnseignantModuleMockMvc.perform(post("/api/historique-enseignant-modules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueEnseignantModule)))
            .andExpect(status().isCreated());

        // Validate the HistoriqueEnseignantModule in the database
        List<HistoriqueEnseignantModule> historiqueEnseignantModuleList = historiqueEnseignantModuleRepository.findAll();
        assertThat(historiqueEnseignantModuleList).hasSize(databaseSizeBeforeCreate + 1);
        HistoriqueEnseignantModule testHistoriqueEnseignantModule = historiqueEnseignantModuleList.get(historiqueEnseignantModuleList.size() - 1);
        assertThat(testHistoriqueEnseignantModule.getDatedebut()).isEqualTo(DEFAULT_DATEDEBUT);
        assertThat(testHistoriqueEnseignantModule.getDatefin()).isEqualTo(DEFAULT_DATEFIN);

        // Validate the HistoriqueEnseignantModule in Elasticsearch
        verify(mockHistoriqueEnseignantModuleSearchRepository, times(1)).save(testHistoriqueEnseignantModule);
    }

    @Test
    @Transactional
    public void createHistoriqueEnseignantModuleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historiqueEnseignantModuleRepository.findAll().size();

        // Create the HistoriqueEnseignantModule with an existing ID
        historiqueEnseignantModule.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriqueEnseignantModuleMockMvc.perform(post("/api/historique-enseignant-modules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueEnseignantModule)))
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueEnseignantModule in the database
        List<HistoriqueEnseignantModule> historiqueEnseignantModuleList = historiqueEnseignantModuleRepository.findAll();
        assertThat(historiqueEnseignantModuleList).hasSize(databaseSizeBeforeCreate);

        // Validate the HistoriqueEnseignantModule in Elasticsearch
        verify(mockHistoriqueEnseignantModuleSearchRepository, times(0)).save(historiqueEnseignantModule);
    }


    @Test
    @Transactional
    public void checkDatedebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = historiqueEnseignantModuleRepository.findAll().size();
        // set the field null
        historiqueEnseignantModule.setDatedebut(null);

        // Create the HistoriqueEnseignantModule, which fails.

        restHistoriqueEnseignantModuleMockMvc.perform(post("/api/historique-enseignant-modules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueEnseignantModule)))
            .andExpect(status().isBadRequest());

        List<HistoriqueEnseignantModule> historiqueEnseignantModuleList = historiqueEnseignantModuleRepository.findAll();
        assertThat(historiqueEnseignantModuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModules() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList
        restHistoriqueEnseignantModuleMockMvc.perform(get("/api/historique-enseignant-modules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueEnseignantModule.getId().intValue())))
            .andExpect(jsonPath("$.[*].datedebut").value(hasItem(DEFAULT_DATEDEBUT.toString())))
            .andExpect(jsonPath("$.[*].datefin").value(hasItem(DEFAULT_DATEFIN.toString())));
    }
    
    @Test
    @Transactional
    public void getHistoriqueEnseignantModule() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get the historiqueEnseignantModule
        restHistoriqueEnseignantModuleMockMvc.perform(get("/api/historique-enseignant-modules/{id}", historiqueEnseignantModule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiqueEnseignantModule.getId().intValue()))
            .andExpect(jsonPath("$.datedebut").value(DEFAULT_DATEDEBUT.toString()))
            .andExpect(jsonPath("$.datefin").value(DEFAULT_DATEFIN.toString()));
    }


    @Test
    @Transactional
    public void getHistoriqueEnseignantModulesByIdFiltering() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        Long id = historiqueEnseignantModule.getId();

        defaultHistoriqueEnseignantModuleShouldBeFound("id.equals=" + id);
        defaultHistoriqueEnseignantModuleShouldNotBeFound("id.notEquals=" + id);

        defaultHistoriqueEnseignantModuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHistoriqueEnseignantModuleShouldNotBeFound("id.greaterThan=" + id);

        defaultHistoriqueEnseignantModuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHistoriqueEnseignantModuleShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByDatedebutIsEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList where datedebut equals to DEFAULT_DATEDEBUT
        defaultHistoriqueEnseignantModuleShouldBeFound("datedebut.equals=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEnseignantModuleList where datedebut equals to UPDATED_DATEDEBUT
        defaultHistoriqueEnseignantModuleShouldNotBeFound("datedebut.equals=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByDatedebutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList where datedebut not equals to DEFAULT_DATEDEBUT
        defaultHistoriqueEnseignantModuleShouldNotBeFound("datedebut.notEquals=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEnseignantModuleList where datedebut not equals to UPDATED_DATEDEBUT
        defaultHistoriqueEnseignantModuleShouldBeFound("datedebut.notEquals=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByDatedebutIsInShouldWork() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList where datedebut in DEFAULT_DATEDEBUT or UPDATED_DATEDEBUT
        defaultHistoriqueEnseignantModuleShouldBeFound("datedebut.in=" + DEFAULT_DATEDEBUT + "," + UPDATED_DATEDEBUT);

        // Get all the historiqueEnseignantModuleList where datedebut equals to UPDATED_DATEDEBUT
        defaultHistoriqueEnseignantModuleShouldNotBeFound("datedebut.in=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByDatedebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList where datedebut is not null
        defaultHistoriqueEnseignantModuleShouldBeFound("datedebut.specified=true");

        // Get all the historiqueEnseignantModuleList where datedebut is null
        defaultHistoriqueEnseignantModuleShouldNotBeFound("datedebut.specified=false");
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByDatedebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList where datedebut is greater than or equal to DEFAULT_DATEDEBUT
        defaultHistoriqueEnseignantModuleShouldBeFound("datedebut.greaterThanOrEqual=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEnseignantModuleList where datedebut is greater than or equal to UPDATED_DATEDEBUT
        defaultHistoriqueEnseignantModuleShouldNotBeFound("datedebut.greaterThanOrEqual=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByDatedebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList where datedebut is less than or equal to DEFAULT_DATEDEBUT
        defaultHistoriqueEnseignantModuleShouldBeFound("datedebut.lessThanOrEqual=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEnseignantModuleList where datedebut is less than or equal to SMALLER_DATEDEBUT
        defaultHistoriqueEnseignantModuleShouldNotBeFound("datedebut.lessThanOrEqual=" + SMALLER_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByDatedebutIsLessThanSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList where datedebut is less than DEFAULT_DATEDEBUT
        defaultHistoriqueEnseignantModuleShouldNotBeFound("datedebut.lessThan=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEnseignantModuleList where datedebut is less than UPDATED_DATEDEBUT
        defaultHistoriqueEnseignantModuleShouldBeFound("datedebut.lessThan=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByDatedebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList where datedebut is greater than DEFAULT_DATEDEBUT
        defaultHistoriqueEnseignantModuleShouldNotBeFound("datedebut.greaterThan=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEnseignantModuleList where datedebut is greater than SMALLER_DATEDEBUT
        defaultHistoriqueEnseignantModuleShouldBeFound("datedebut.greaterThan=" + SMALLER_DATEDEBUT);
    }


    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByDatefinIsEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList where datefin equals to DEFAULT_DATEFIN
        defaultHistoriqueEnseignantModuleShouldBeFound("datefin.equals=" + DEFAULT_DATEFIN);

        // Get all the historiqueEnseignantModuleList where datefin equals to UPDATED_DATEFIN
        defaultHistoriqueEnseignantModuleShouldNotBeFound("datefin.equals=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByDatefinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList where datefin not equals to DEFAULT_DATEFIN
        defaultHistoriqueEnseignantModuleShouldNotBeFound("datefin.notEquals=" + DEFAULT_DATEFIN);

        // Get all the historiqueEnseignantModuleList where datefin not equals to UPDATED_DATEFIN
        defaultHistoriqueEnseignantModuleShouldBeFound("datefin.notEquals=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByDatefinIsInShouldWork() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList where datefin in DEFAULT_DATEFIN or UPDATED_DATEFIN
        defaultHistoriqueEnseignantModuleShouldBeFound("datefin.in=" + DEFAULT_DATEFIN + "," + UPDATED_DATEFIN);

        // Get all the historiqueEnseignantModuleList where datefin equals to UPDATED_DATEFIN
        defaultHistoriqueEnseignantModuleShouldNotBeFound("datefin.in=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByDatefinIsNullOrNotNull() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList where datefin is not null
        defaultHistoriqueEnseignantModuleShouldBeFound("datefin.specified=true");

        // Get all the historiqueEnseignantModuleList where datefin is null
        defaultHistoriqueEnseignantModuleShouldNotBeFound("datefin.specified=false");
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByDatefinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList where datefin is greater than or equal to DEFAULT_DATEFIN
        defaultHistoriqueEnseignantModuleShouldBeFound("datefin.greaterThanOrEqual=" + DEFAULT_DATEFIN);

        // Get all the historiqueEnseignantModuleList where datefin is greater than or equal to UPDATED_DATEFIN
        defaultHistoriqueEnseignantModuleShouldNotBeFound("datefin.greaterThanOrEqual=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByDatefinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList where datefin is less than or equal to DEFAULT_DATEFIN
        defaultHistoriqueEnseignantModuleShouldBeFound("datefin.lessThanOrEqual=" + DEFAULT_DATEFIN);

        // Get all the historiqueEnseignantModuleList where datefin is less than or equal to SMALLER_DATEFIN
        defaultHistoriqueEnseignantModuleShouldNotBeFound("datefin.lessThanOrEqual=" + SMALLER_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByDatefinIsLessThanSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList where datefin is less than DEFAULT_DATEFIN
        defaultHistoriqueEnseignantModuleShouldNotBeFound("datefin.lessThan=" + DEFAULT_DATEFIN);

        // Get all the historiqueEnseignantModuleList where datefin is less than UPDATED_DATEFIN
        defaultHistoriqueEnseignantModuleShouldBeFound("datefin.lessThan=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByDatefinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);

        // Get all the historiqueEnseignantModuleList where datefin is greater than DEFAULT_DATEFIN
        defaultHistoriqueEnseignantModuleShouldNotBeFound("datefin.greaterThan=" + DEFAULT_DATEFIN);

        // Get all the historiqueEnseignantModuleList where datefin is greater than SMALLER_DATEFIN
        defaultHistoriqueEnseignantModuleShouldBeFound("datefin.greaterThan=" + SMALLER_DATEFIN);
    }


    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByModuleIsEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);
        Module module = ModuleResourceIT.createEntity(em);
        em.persist(module);
        em.flush();
        historiqueEnseignantModule.addModule(module);
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);
        Long moduleId = module.getId();

        // Get all the historiqueEnseignantModuleList where module equals to moduleId
        defaultHistoriqueEnseignantModuleShouldBeFound("moduleId.equals=" + moduleId);

        // Get all the historiqueEnseignantModuleList where module equals to moduleId + 1
        defaultHistoriqueEnseignantModuleShouldNotBeFound("moduleId.equals=" + (moduleId + 1));
    }


    @Test
    @Transactional
    public void getAllHistoriqueEnseignantModulesByEnseignantIsEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);
        Enseignant enseignant = EnseignantResourceIT.createEntity(em);
        em.persist(enseignant);
        em.flush();
        historiqueEnseignantModule.addEnseignant(enseignant);
        historiqueEnseignantModuleRepository.saveAndFlush(historiqueEnseignantModule);
        Long enseignantId = enseignant.getId();

        // Get all the historiqueEnseignantModuleList where enseignant equals to enseignantId
        defaultHistoriqueEnseignantModuleShouldBeFound("enseignantId.equals=" + enseignantId);

        // Get all the historiqueEnseignantModuleList where enseignant equals to enseignantId + 1
        defaultHistoriqueEnseignantModuleShouldNotBeFound("enseignantId.equals=" + (enseignantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHistoriqueEnseignantModuleShouldBeFound(String filter) throws Exception {
        restHistoriqueEnseignantModuleMockMvc.perform(get("/api/historique-enseignant-modules?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueEnseignantModule.getId().intValue())))
            .andExpect(jsonPath("$.[*].datedebut").value(hasItem(DEFAULT_DATEDEBUT.toString())))
            .andExpect(jsonPath("$.[*].datefin").value(hasItem(DEFAULT_DATEFIN.toString())));

        // Check, that the count call also returns 1
        restHistoriqueEnseignantModuleMockMvc.perform(get("/api/historique-enseignant-modules/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHistoriqueEnseignantModuleShouldNotBeFound(String filter) throws Exception {
        restHistoriqueEnseignantModuleMockMvc.perform(get("/api/historique-enseignant-modules?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHistoriqueEnseignantModuleMockMvc.perform(get("/api/historique-enseignant-modules/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingHistoriqueEnseignantModule() throws Exception {
        // Get the historiqueEnseignantModule
        restHistoriqueEnseignantModuleMockMvc.perform(get("/api/historique-enseignant-modules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistoriqueEnseignantModule() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleService.save(historiqueEnseignantModule);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockHistoriqueEnseignantModuleSearchRepository);

        int databaseSizeBeforeUpdate = historiqueEnseignantModuleRepository.findAll().size();

        // Update the historiqueEnseignantModule
        HistoriqueEnseignantModule updatedHistoriqueEnseignantModule = historiqueEnseignantModuleRepository.findById(historiqueEnseignantModule.getId()).get();
        // Disconnect from session so that the updates on updatedHistoriqueEnseignantModule are not directly saved in db
        em.detach(updatedHistoriqueEnseignantModule);
        updatedHistoriqueEnseignantModule
            .datedebut(UPDATED_DATEDEBUT)
            .datefin(UPDATED_DATEFIN);

        restHistoriqueEnseignantModuleMockMvc.perform(put("/api/historique-enseignant-modules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedHistoriqueEnseignantModule)))
            .andExpect(status().isOk());

        // Validate the HistoriqueEnseignantModule in the database
        List<HistoriqueEnseignantModule> historiqueEnseignantModuleList = historiqueEnseignantModuleRepository.findAll();
        assertThat(historiqueEnseignantModuleList).hasSize(databaseSizeBeforeUpdate);
        HistoriqueEnseignantModule testHistoriqueEnseignantModule = historiqueEnseignantModuleList.get(historiqueEnseignantModuleList.size() - 1);
        assertThat(testHistoriqueEnseignantModule.getDatedebut()).isEqualTo(UPDATED_DATEDEBUT);
        assertThat(testHistoriqueEnseignantModule.getDatefin()).isEqualTo(UPDATED_DATEFIN);

        // Validate the HistoriqueEnseignantModule in Elasticsearch
        verify(mockHistoriqueEnseignantModuleSearchRepository, times(1)).save(testHistoriqueEnseignantModule);
    }

    @Test
    @Transactional
    public void updateNonExistingHistoriqueEnseignantModule() throws Exception {
        int databaseSizeBeforeUpdate = historiqueEnseignantModuleRepository.findAll().size();

        // Create the HistoriqueEnseignantModule

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueEnseignantModuleMockMvc.perform(put("/api/historique-enseignant-modules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueEnseignantModule)))
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueEnseignantModule in the database
        List<HistoriqueEnseignantModule> historiqueEnseignantModuleList = historiqueEnseignantModuleRepository.findAll();
        assertThat(historiqueEnseignantModuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HistoriqueEnseignantModule in Elasticsearch
        verify(mockHistoriqueEnseignantModuleSearchRepository, times(0)).save(historiqueEnseignantModule);
    }

    @Test
    @Transactional
    public void deleteHistoriqueEnseignantModule() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleService.save(historiqueEnseignantModule);

        int databaseSizeBeforeDelete = historiqueEnseignantModuleRepository.findAll().size();

        // Delete the historiqueEnseignantModule
        restHistoriqueEnseignantModuleMockMvc.perform(delete("/api/historique-enseignant-modules/{id}", historiqueEnseignantModule.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HistoriqueEnseignantModule> historiqueEnseignantModuleList = historiqueEnseignantModuleRepository.findAll();
        assertThat(historiqueEnseignantModuleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HistoriqueEnseignantModule in Elasticsearch
        verify(mockHistoriqueEnseignantModuleSearchRepository, times(1)).deleteById(historiqueEnseignantModule.getId());
    }

    @Test
    @Transactional
    public void searchHistoriqueEnseignantModule() throws Exception {
        // Initialize the database
        historiqueEnseignantModuleService.save(historiqueEnseignantModule);
        when(mockHistoriqueEnseignantModuleSearchRepository.search(queryStringQuery("id:" + historiqueEnseignantModule.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(historiqueEnseignantModule), PageRequest.of(0, 1), 1));
        // Search the historiqueEnseignantModule
        restHistoriqueEnseignantModuleMockMvc.perform(get("/api/_search/historique-enseignant-modules?query=id:" + historiqueEnseignantModule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueEnseignantModule.getId().intValue())))
            .andExpect(jsonPath("$.[*].datedebut").value(hasItem(DEFAULT_DATEDEBUT.toString())))
            .andExpect(jsonPath("$.[*].datefin").value(hasItem(DEFAULT_DATEFIN.toString())));
    }
}
