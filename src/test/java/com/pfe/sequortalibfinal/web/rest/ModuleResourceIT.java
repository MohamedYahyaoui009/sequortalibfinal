package com.pfe.sequortalibfinal.web.rest;

import com.pfe.sequortalibfinal.SequortalibfinalApp;
import com.pfe.sequortalibfinal.domain.Module;
import com.pfe.sequortalibfinal.domain.Filiere;
import com.pfe.sequortalibfinal.domain.HistoriqueEtudiantModule;
import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantModule;
import com.pfe.sequortalibfinal.repository.ModuleRepository;
import com.pfe.sequortalibfinal.repository.search.ModuleSearchRepository;
import com.pfe.sequortalibfinal.service.ModuleService;
import com.pfe.sequortalibfinal.service.dto.ModuleCriteria;
import com.pfe.sequortalibfinal.service.ModuleQueryService;

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
 * Integration tests for the {@link ModuleResource} REST controller.
 */
@SpringBootTest(classes = SequortalibfinalApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ModuleResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEMESTRE = 1;
    private static final Integer UPDATED_SEMESTRE = 2;
    private static final Integer SMALLER_SEMESTRE = 1 - 1;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ModuleService moduleService;

    /**
     * This repository is mocked in the com.pfe.sequortalibfinal.repository.search test package.
     *
     * @see com.pfe.sequortalibfinal.repository.search.ModuleSearchRepositoryMockConfiguration
     */
    @Autowired
    private ModuleSearchRepository mockModuleSearchRepository;

    @Autowired
    private ModuleQueryService moduleQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModuleMockMvc;

    private Module module;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Module createEntity(EntityManager em) {
        Module module = new Module()
            .nom(DEFAULT_NOM)
            .semestre(DEFAULT_SEMESTRE);
        return module;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Module createUpdatedEntity(EntityManager em) {
        Module module = new Module()
            .nom(UPDATED_NOM)
            .semestre(UPDATED_SEMESTRE);
        return module;
    }

    @BeforeEach
    public void initTest() {
        module = createEntity(em);
    }

    @Test
    @Transactional
    public void createModule() throws Exception {
        int databaseSizeBeforeCreate = moduleRepository.findAll().size();

        // Create the Module
        restModuleMockMvc.perform(post("/api/modules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(module)))
            .andExpect(status().isCreated());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeCreate + 1);
        Module testModule = moduleList.get(moduleList.size() - 1);
        assertThat(testModule.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testModule.getSemestre()).isEqualTo(DEFAULT_SEMESTRE);

        // Validate the Module in Elasticsearch
        verify(mockModuleSearchRepository, times(1)).save(testModule);
    }

    @Test
    @Transactional
    public void createModuleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = moduleRepository.findAll().size();

        // Create the Module with an existing ID
        module.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModuleMockMvc.perform(post("/api/modules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(module)))
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeCreate);

        // Validate the Module in Elasticsearch
        verify(mockModuleSearchRepository, times(0)).save(module);
    }


    @Test
    @Transactional
    public void getAllModules() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList
        restModuleMockMvc.perform(get("/api/modules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(module.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].semestre").value(hasItem(DEFAULT_SEMESTRE)));
    }
    
    @Test
    @Transactional
    public void getModule() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get the module
        restModuleMockMvc.perform(get("/api/modules/{id}", module.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(module.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.semestre").value(DEFAULT_SEMESTRE));
    }


    @Test
    @Transactional
    public void getModulesByIdFiltering() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        Long id = module.getId();

        defaultModuleShouldBeFound("id.equals=" + id);
        defaultModuleShouldNotBeFound("id.notEquals=" + id);

        defaultModuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultModuleShouldNotBeFound("id.greaterThan=" + id);

        defaultModuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultModuleShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllModulesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList where nom equals to DEFAULT_NOM
        defaultModuleShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the moduleList where nom equals to UPDATED_NOM
        defaultModuleShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllModulesByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList where nom not equals to DEFAULT_NOM
        defaultModuleShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the moduleList where nom not equals to UPDATED_NOM
        defaultModuleShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllModulesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultModuleShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the moduleList where nom equals to UPDATED_NOM
        defaultModuleShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllModulesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList where nom is not null
        defaultModuleShouldBeFound("nom.specified=true");

        // Get all the moduleList where nom is null
        defaultModuleShouldNotBeFound("nom.specified=false");
    }
                @Test
    @Transactional
    public void getAllModulesByNomContainsSomething() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList where nom contains DEFAULT_NOM
        defaultModuleShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the moduleList where nom contains UPDATED_NOM
        defaultModuleShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllModulesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList where nom does not contain DEFAULT_NOM
        defaultModuleShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the moduleList where nom does not contain UPDATED_NOM
        defaultModuleShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }


    @Test
    @Transactional
    public void getAllModulesBySemestreIsEqualToSomething() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList where semestre equals to DEFAULT_SEMESTRE
        defaultModuleShouldBeFound("semestre.equals=" + DEFAULT_SEMESTRE);

        // Get all the moduleList where semestre equals to UPDATED_SEMESTRE
        defaultModuleShouldNotBeFound("semestre.equals=" + UPDATED_SEMESTRE);
    }

    @Test
    @Transactional
    public void getAllModulesBySemestreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList where semestre not equals to DEFAULT_SEMESTRE
        defaultModuleShouldNotBeFound("semestre.notEquals=" + DEFAULT_SEMESTRE);

        // Get all the moduleList where semestre not equals to UPDATED_SEMESTRE
        defaultModuleShouldBeFound("semestre.notEquals=" + UPDATED_SEMESTRE);
    }

    @Test
    @Transactional
    public void getAllModulesBySemestreIsInShouldWork() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList where semestre in DEFAULT_SEMESTRE or UPDATED_SEMESTRE
        defaultModuleShouldBeFound("semestre.in=" + DEFAULT_SEMESTRE + "," + UPDATED_SEMESTRE);

        // Get all the moduleList where semestre equals to UPDATED_SEMESTRE
        defaultModuleShouldNotBeFound("semestre.in=" + UPDATED_SEMESTRE);
    }

    @Test
    @Transactional
    public void getAllModulesBySemestreIsNullOrNotNull() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList where semestre is not null
        defaultModuleShouldBeFound("semestre.specified=true");

        // Get all the moduleList where semestre is null
        defaultModuleShouldNotBeFound("semestre.specified=false");
    }

    @Test
    @Transactional
    public void getAllModulesBySemestreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList where semestre is greater than or equal to DEFAULT_SEMESTRE
        defaultModuleShouldBeFound("semestre.greaterThanOrEqual=" + DEFAULT_SEMESTRE);

        // Get all the moduleList where semestre is greater than or equal to UPDATED_SEMESTRE
        defaultModuleShouldNotBeFound("semestre.greaterThanOrEqual=" + UPDATED_SEMESTRE);
    }

    @Test
    @Transactional
    public void getAllModulesBySemestreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList where semestre is less than or equal to DEFAULT_SEMESTRE
        defaultModuleShouldBeFound("semestre.lessThanOrEqual=" + DEFAULT_SEMESTRE);

        // Get all the moduleList where semestre is less than or equal to SMALLER_SEMESTRE
        defaultModuleShouldNotBeFound("semestre.lessThanOrEqual=" + SMALLER_SEMESTRE);
    }

    @Test
    @Transactional
    public void getAllModulesBySemestreIsLessThanSomething() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList where semestre is less than DEFAULT_SEMESTRE
        defaultModuleShouldNotBeFound("semestre.lessThan=" + DEFAULT_SEMESTRE);

        // Get all the moduleList where semestre is less than UPDATED_SEMESTRE
        defaultModuleShouldBeFound("semestre.lessThan=" + UPDATED_SEMESTRE);
    }

    @Test
    @Transactional
    public void getAllModulesBySemestreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList where semestre is greater than DEFAULT_SEMESTRE
        defaultModuleShouldNotBeFound("semestre.greaterThan=" + DEFAULT_SEMESTRE);

        // Get all the moduleList where semestre is greater than SMALLER_SEMESTRE
        defaultModuleShouldBeFound("semestre.greaterThan=" + SMALLER_SEMESTRE);
    }


    @Test
    @Transactional
    public void getAllModulesByFiliereIsEqualToSomething() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);
        Filiere filiere = FiliereResourceIT.createEntity(em);
        em.persist(filiere);
        em.flush();
        module.addFiliere(filiere);
        moduleRepository.saveAndFlush(module);
        Long filiereId = filiere.getId();

        // Get all the moduleList where filiere equals to filiereId
        defaultModuleShouldBeFound("filiereId.equals=" + filiereId);

        // Get all the moduleList where filiere equals to filiereId + 1
        defaultModuleShouldNotBeFound("filiereId.equals=" + (filiereId + 1));
    }


    @Test
    @Transactional
    public void getAllModulesByHistoriqueEtudiantModuleIsEqualToSomething() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);
        HistoriqueEtudiantModule historiqueEtudiantModule = HistoriqueEtudiantModuleResourceIT.createEntity(em);
        em.persist(historiqueEtudiantModule);
        em.flush();
        module.setHistoriqueEtudiantModule(historiqueEtudiantModule);
        moduleRepository.saveAndFlush(module);
        Long historiqueEtudiantModuleId = historiqueEtudiantModule.getId();

        // Get all the moduleList where historiqueEtudiantModule equals to historiqueEtudiantModuleId
        defaultModuleShouldBeFound("historiqueEtudiantModuleId.equals=" + historiqueEtudiantModuleId);

        // Get all the moduleList where historiqueEtudiantModule equals to historiqueEtudiantModuleId + 1
        defaultModuleShouldNotBeFound("historiqueEtudiantModuleId.equals=" + (historiqueEtudiantModuleId + 1));
    }


    @Test
    @Transactional
    public void getAllModulesByHistoriqueEnseignantModuleIsEqualToSomething() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);
        HistoriqueEnseignantModule historiqueEnseignantModule = HistoriqueEnseignantModuleResourceIT.createEntity(em);
        em.persist(historiqueEnseignantModule);
        em.flush();
        module.setHistoriqueEnseignantModule(historiqueEnseignantModule);
        moduleRepository.saveAndFlush(module);
        Long historiqueEnseignantModuleId = historiqueEnseignantModule.getId();

        // Get all the moduleList where historiqueEnseignantModule equals to historiqueEnseignantModuleId
        defaultModuleShouldBeFound("historiqueEnseignantModuleId.equals=" + historiqueEnseignantModuleId);

        // Get all the moduleList where historiqueEnseignantModule equals to historiqueEnseignantModuleId + 1
        defaultModuleShouldNotBeFound("historiqueEnseignantModuleId.equals=" + (historiqueEnseignantModuleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultModuleShouldBeFound(String filter) throws Exception {
        restModuleMockMvc.perform(get("/api/modules?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(module.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].semestre").value(hasItem(DEFAULT_SEMESTRE)));

        // Check, that the count call also returns 1
        restModuleMockMvc.perform(get("/api/modules/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultModuleShouldNotBeFound(String filter) throws Exception {
        restModuleMockMvc.perform(get("/api/modules?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restModuleMockMvc.perform(get("/api/modules/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingModule() throws Exception {
        // Get the module
        restModuleMockMvc.perform(get("/api/modules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModule() throws Exception {
        // Initialize the database
        moduleService.save(module);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockModuleSearchRepository);

        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();

        // Update the module
        Module updatedModule = moduleRepository.findById(module.getId()).get();
        // Disconnect from session so that the updates on updatedModule are not directly saved in db
        em.detach(updatedModule);
        updatedModule
            .nom(UPDATED_NOM)
            .semestre(UPDATED_SEMESTRE);

        restModuleMockMvc.perform(put("/api/modules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedModule)))
            .andExpect(status().isOk());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
        Module testModule = moduleList.get(moduleList.size() - 1);
        assertThat(testModule.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testModule.getSemestre()).isEqualTo(UPDATED_SEMESTRE);

        // Validate the Module in Elasticsearch
        verify(mockModuleSearchRepository, times(1)).save(testModule);
    }

    @Test
    @Transactional
    public void updateNonExistingModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();

        // Create the Module

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModuleMockMvc.perform(put("/api/modules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(module)))
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Module in Elasticsearch
        verify(mockModuleSearchRepository, times(0)).save(module);
    }

    @Test
    @Transactional
    public void deleteModule() throws Exception {
        // Initialize the database
        moduleService.save(module);

        int databaseSizeBeforeDelete = moduleRepository.findAll().size();

        // Delete the module
        restModuleMockMvc.perform(delete("/api/modules/{id}", module.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Module in Elasticsearch
        verify(mockModuleSearchRepository, times(1)).deleteById(module.getId());
    }

    @Test
    @Transactional
    public void searchModule() throws Exception {
        // Initialize the database
        moduleService.save(module);
        when(mockModuleSearchRepository.search(queryStringQuery("id:" + module.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(module), PageRequest.of(0, 1), 1));
        // Search the module
        restModuleMockMvc.perform(get("/api/_search/modules?query=id:" + module.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(module.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].semestre").value(hasItem(DEFAULT_SEMESTRE)));
    }
}
