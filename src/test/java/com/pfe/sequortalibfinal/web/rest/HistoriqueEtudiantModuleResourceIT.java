package com.pfe.sequortalibfinal.web.rest;

import com.pfe.sequortalibfinal.SequortalibfinalApp;
import com.pfe.sequortalibfinal.domain.HistoriqueEtudiantModule;
import com.pfe.sequortalibfinal.domain.Module;
import com.pfe.sequortalibfinal.domain.Etudiant;
import com.pfe.sequortalibfinal.repository.HistoriqueEtudiantModuleRepository;
import com.pfe.sequortalibfinal.repository.search.HistoriqueEtudiantModuleSearchRepository;
import com.pfe.sequortalibfinal.service.HistoriqueEtudiantModuleService;
import com.pfe.sequortalibfinal.service.dto.HistoriqueEtudiantModuleCriteria;
import com.pfe.sequortalibfinal.service.HistoriqueEtudiantModuleQueryService;

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
 * Integration tests for the {@link HistoriqueEtudiantModuleResource} REST controller.
 */
@SpringBootTest(classes = SequortalibfinalApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class HistoriqueEtudiantModuleResourceIT {

    private static final LocalDate DEFAULT_DATEDEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEDEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATEDEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATEFIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEFIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATEFIN = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_NOTE = 1D;
    private static final Double UPDATED_NOTE = 2D;
    private static final Double SMALLER_NOTE = 1D - 1D;

    @Autowired
    private HistoriqueEtudiantModuleRepository historiqueEtudiantModuleRepository;

    @Autowired
    private HistoriqueEtudiantModuleService historiqueEtudiantModuleService;

    /**
     * This repository is mocked in the com.pfe.sequortalibfinal.repository.search test package.
     *
     * @see com.pfe.sequortalibfinal.repository.search.HistoriqueEtudiantModuleSearchRepositoryMockConfiguration
     */
    @Autowired
    private HistoriqueEtudiantModuleSearchRepository mockHistoriqueEtudiantModuleSearchRepository;

    @Autowired
    private HistoriqueEtudiantModuleQueryService historiqueEtudiantModuleQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriqueEtudiantModuleMockMvc;

    private HistoriqueEtudiantModule historiqueEtudiantModule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueEtudiantModule createEntity(EntityManager em) {
        HistoriqueEtudiantModule historiqueEtudiantModule = new HistoriqueEtudiantModule()
            .datedebut(DEFAULT_DATEDEBUT)
            .datefin(DEFAULT_DATEFIN)
            .note(DEFAULT_NOTE);
        return historiqueEtudiantModule;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueEtudiantModule createUpdatedEntity(EntityManager em) {
        HistoriqueEtudiantModule historiqueEtudiantModule = new HistoriqueEtudiantModule()
            .datedebut(UPDATED_DATEDEBUT)
            .datefin(UPDATED_DATEFIN)
            .note(UPDATED_NOTE);
        return historiqueEtudiantModule;
    }

    @BeforeEach
    public void initTest() {
        historiqueEtudiantModule = createEntity(em);
    }

    @Test
    @Transactional
    public void createHistoriqueEtudiantModule() throws Exception {
        int databaseSizeBeforeCreate = historiqueEtudiantModuleRepository.findAll().size();

        // Create the HistoriqueEtudiantModule
        restHistoriqueEtudiantModuleMockMvc.perform(post("/api/historique-etudiant-modules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueEtudiantModule)))
            .andExpect(status().isCreated());

        // Validate the HistoriqueEtudiantModule in the database
        List<HistoriqueEtudiantModule> historiqueEtudiantModuleList = historiqueEtudiantModuleRepository.findAll();
        assertThat(historiqueEtudiantModuleList).hasSize(databaseSizeBeforeCreate + 1);
        HistoriqueEtudiantModule testHistoriqueEtudiantModule = historiqueEtudiantModuleList.get(historiqueEtudiantModuleList.size() - 1);
        assertThat(testHistoriqueEtudiantModule.getDatedebut()).isEqualTo(DEFAULT_DATEDEBUT);
        assertThat(testHistoriqueEtudiantModule.getDatefin()).isEqualTo(DEFAULT_DATEFIN);
        assertThat(testHistoriqueEtudiantModule.getNote()).isEqualTo(DEFAULT_NOTE);

        // Validate the HistoriqueEtudiantModule in Elasticsearch
        verify(mockHistoriqueEtudiantModuleSearchRepository, times(1)).save(testHistoriqueEtudiantModule);
    }

    @Test
    @Transactional
    public void createHistoriqueEtudiantModuleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historiqueEtudiantModuleRepository.findAll().size();

        // Create the HistoriqueEtudiantModule with an existing ID
        historiqueEtudiantModule.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriqueEtudiantModuleMockMvc.perform(post("/api/historique-etudiant-modules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueEtudiantModule)))
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueEtudiantModule in the database
        List<HistoriqueEtudiantModule> historiqueEtudiantModuleList = historiqueEtudiantModuleRepository.findAll();
        assertThat(historiqueEtudiantModuleList).hasSize(databaseSizeBeforeCreate);

        // Validate the HistoriqueEtudiantModule in Elasticsearch
        verify(mockHistoriqueEtudiantModuleSearchRepository, times(0)).save(historiqueEtudiantModule);
    }


    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModules() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList
        restHistoriqueEtudiantModuleMockMvc.perform(get("/api/historique-etudiant-modules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueEtudiantModule.getId().intValue())))
            .andExpect(jsonPath("$.[*].datedebut").value(hasItem(DEFAULT_DATEDEBUT.toString())))
            .andExpect(jsonPath("$.[*].datefin").value(hasItem(DEFAULT_DATEFIN.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getHistoriqueEtudiantModule() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get the historiqueEtudiantModule
        restHistoriqueEtudiantModuleMockMvc.perform(get("/api/historique-etudiant-modules/{id}", historiqueEtudiantModule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiqueEtudiantModule.getId().intValue()))
            .andExpect(jsonPath("$.datedebut").value(DEFAULT_DATEDEBUT.toString()))
            .andExpect(jsonPath("$.datefin").value(DEFAULT_DATEFIN.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.doubleValue()));
    }


    @Test
    @Transactional
    public void getHistoriqueEtudiantModulesByIdFiltering() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        Long id = historiqueEtudiantModule.getId();

        defaultHistoriqueEtudiantModuleShouldBeFound("id.equals=" + id);
        defaultHistoriqueEtudiantModuleShouldNotBeFound("id.notEquals=" + id);

        defaultHistoriqueEtudiantModuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHistoriqueEtudiantModuleShouldNotBeFound("id.greaterThan=" + id);

        defaultHistoriqueEtudiantModuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHistoriqueEtudiantModuleShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByDatedebutIsEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where datedebut equals to DEFAULT_DATEDEBUT
        defaultHistoriqueEtudiantModuleShouldBeFound("datedebut.equals=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEtudiantModuleList where datedebut equals to UPDATED_DATEDEBUT
        defaultHistoriqueEtudiantModuleShouldNotBeFound("datedebut.equals=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByDatedebutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where datedebut not equals to DEFAULT_DATEDEBUT
        defaultHistoriqueEtudiantModuleShouldNotBeFound("datedebut.notEquals=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEtudiantModuleList where datedebut not equals to UPDATED_DATEDEBUT
        defaultHistoriqueEtudiantModuleShouldBeFound("datedebut.notEquals=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByDatedebutIsInShouldWork() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where datedebut in DEFAULT_DATEDEBUT or UPDATED_DATEDEBUT
        defaultHistoriqueEtudiantModuleShouldBeFound("datedebut.in=" + DEFAULT_DATEDEBUT + "," + UPDATED_DATEDEBUT);

        // Get all the historiqueEtudiantModuleList where datedebut equals to UPDATED_DATEDEBUT
        defaultHistoriqueEtudiantModuleShouldNotBeFound("datedebut.in=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByDatedebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where datedebut is not null
        defaultHistoriqueEtudiantModuleShouldBeFound("datedebut.specified=true");

        // Get all the historiqueEtudiantModuleList where datedebut is null
        defaultHistoriqueEtudiantModuleShouldNotBeFound("datedebut.specified=false");
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByDatedebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where datedebut is greater than or equal to DEFAULT_DATEDEBUT
        defaultHistoriqueEtudiantModuleShouldBeFound("datedebut.greaterThanOrEqual=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEtudiantModuleList where datedebut is greater than or equal to UPDATED_DATEDEBUT
        defaultHistoriqueEtudiantModuleShouldNotBeFound("datedebut.greaterThanOrEqual=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByDatedebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where datedebut is less than or equal to DEFAULT_DATEDEBUT
        defaultHistoriqueEtudiantModuleShouldBeFound("datedebut.lessThanOrEqual=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEtudiantModuleList where datedebut is less than or equal to SMALLER_DATEDEBUT
        defaultHistoriqueEtudiantModuleShouldNotBeFound("datedebut.lessThanOrEqual=" + SMALLER_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByDatedebutIsLessThanSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where datedebut is less than DEFAULT_DATEDEBUT
        defaultHistoriqueEtudiantModuleShouldNotBeFound("datedebut.lessThan=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEtudiantModuleList where datedebut is less than UPDATED_DATEDEBUT
        defaultHistoriqueEtudiantModuleShouldBeFound("datedebut.lessThan=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByDatedebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where datedebut is greater than DEFAULT_DATEDEBUT
        defaultHistoriqueEtudiantModuleShouldNotBeFound("datedebut.greaterThan=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEtudiantModuleList where datedebut is greater than SMALLER_DATEDEBUT
        defaultHistoriqueEtudiantModuleShouldBeFound("datedebut.greaterThan=" + SMALLER_DATEDEBUT);
    }


    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByDatefinIsEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where datefin equals to DEFAULT_DATEFIN
        defaultHistoriqueEtudiantModuleShouldBeFound("datefin.equals=" + DEFAULT_DATEFIN);

        // Get all the historiqueEtudiantModuleList where datefin equals to UPDATED_DATEFIN
        defaultHistoriqueEtudiantModuleShouldNotBeFound("datefin.equals=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByDatefinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where datefin not equals to DEFAULT_DATEFIN
        defaultHistoriqueEtudiantModuleShouldNotBeFound("datefin.notEquals=" + DEFAULT_DATEFIN);

        // Get all the historiqueEtudiantModuleList where datefin not equals to UPDATED_DATEFIN
        defaultHistoriqueEtudiantModuleShouldBeFound("datefin.notEquals=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByDatefinIsInShouldWork() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where datefin in DEFAULT_DATEFIN or UPDATED_DATEFIN
        defaultHistoriqueEtudiantModuleShouldBeFound("datefin.in=" + DEFAULT_DATEFIN + "," + UPDATED_DATEFIN);

        // Get all the historiqueEtudiantModuleList where datefin equals to UPDATED_DATEFIN
        defaultHistoriqueEtudiantModuleShouldNotBeFound("datefin.in=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByDatefinIsNullOrNotNull() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where datefin is not null
        defaultHistoriqueEtudiantModuleShouldBeFound("datefin.specified=true");

        // Get all the historiqueEtudiantModuleList where datefin is null
        defaultHistoriqueEtudiantModuleShouldNotBeFound("datefin.specified=false");
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByDatefinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where datefin is greater than or equal to DEFAULT_DATEFIN
        defaultHistoriqueEtudiantModuleShouldBeFound("datefin.greaterThanOrEqual=" + DEFAULT_DATEFIN);

        // Get all the historiqueEtudiantModuleList where datefin is greater than or equal to UPDATED_DATEFIN
        defaultHistoriqueEtudiantModuleShouldNotBeFound("datefin.greaterThanOrEqual=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByDatefinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where datefin is less than or equal to DEFAULT_DATEFIN
        defaultHistoriqueEtudiantModuleShouldBeFound("datefin.lessThanOrEqual=" + DEFAULT_DATEFIN);

        // Get all the historiqueEtudiantModuleList where datefin is less than or equal to SMALLER_DATEFIN
        defaultHistoriqueEtudiantModuleShouldNotBeFound("datefin.lessThanOrEqual=" + SMALLER_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByDatefinIsLessThanSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where datefin is less than DEFAULT_DATEFIN
        defaultHistoriqueEtudiantModuleShouldNotBeFound("datefin.lessThan=" + DEFAULT_DATEFIN);

        // Get all the historiqueEtudiantModuleList where datefin is less than UPDATED_DATEFIN
        defaultHistoriqueEtudiantModuleShouldBeFound("datefin.lessThan=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByDatefinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where datefin is greater than DEFAULT_DATEFIN
        defaultHistoriqueEtudiantModuleShouldNotBeFound("datefin.greaterThan=" + DEFAULT_DATEFIN);

        // Get all the historiqueEtudiantModuleList where datefin is greater than SMALLER_DATEFIN
        defaultHistoriqueEtudiantModuleShouldBeFound("datefin.greaterThan=" + SMALLER_DATEFIN);
    }


    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where note equals to DEFAULT_NOTE
        defaultHistoriqueEtudiantModuleShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the historiqueEtudiantModuleList where note equals to UPDATED_NOTE
        defaultHistoriqueEtudiantModuleShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where note not equals to DEFAULT_NOTE
        defaultHistoriqueEtudiantModuleShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the historiqueEtudiantModuleList where note not equals to UPDATED_NOTE
        defaultHistoriqueEtudiantModuleShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultHistoriqueEtudiantModuleShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the historiqueEtudiantModuleList where note equals to UPDATED_NOTE
        defaultHistoriqueEtudiantModuleShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where note is not null
        defaultHistoriqueEtudiantModuleShouldBeFound("note.specified=true");

        // Get all the historiqueEtudiantModuleList where note is null
        defaultHistoriqueEtudiantModuleShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByNoteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where note is greater than or equal to DEFAULT_NOTE
        defaultHistoriqueEtudiantModuleShouldBeFound("note.greaterThanOrEqual=" + DEFAULT_NOTE);

        // Get all the historiqueEtudiantModuleList where note is greater than or equal to UPDATED_NOTE
        defaultHistoriqueEtudiantModuleShouldNotBeFound("note.greaterThanOrEqual=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByNoteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where note is less than or equal to DEFAULT_NOTE
        defaultHistoriqueEtudiantModuleShouldBeFound("note.lessThanOrEqual=" + DEFAULT_NOTE);

        // Get all the historiqueEtudiantModuleList where note is less than or equal to SMALLER_NOTE
        defaultHistoriqueEtudiantModuleShouldNotBeFound("note.lessThanOrEqual=" + SMALLER_NOTE);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByNoteIsLessThanSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where note is less than DEFAULT_NOTE
        defaultHistoriqueEtudiantModuleShouldNotBeFound("note.lessThan=" + DEFAULT_NOTE);

        // Get all the historiqueEtudiantModuleList where note is less than UPDATED_NOTE
        defaultHistoriqueEtudiantModuleShouldBeFound("note.lessThan=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByNoteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);

        // Get all the historiqueEtudiantModuleList where note is greater than DEFAULT_NOTE
        defaultHistoriqueEtudiantModuleShouldNotBeFound("note.greaterThan=" + DEFAULT_NOTE);

        // Get all the historiqueEtudiantModuleList where note is greater than SMALLER_NOTE
        defaultHistoriqueEtudiantModuleShouldBeFound("note.greaterThan=" + SMALLER_NOTE);
    }


    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByModuleIsEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);
        Module module = ModuleResourceIT.createEntity(em);
        em.persist(module);
        em.flush();
        historiqueEtudiantModule.addModule(module);
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);
        Long moduleId = module.getId();

        // Get all the historiqueEtudiantModuleList where module equals to moduleId
        defaultHistoriqueEtudiantModuleShouldBeFound("moduleId.equals=" + moduleId);

        // Get all the historiqueEtudiantModuleList where module equals to moduleId + 1
        defaultHistoriqueEtudiantModuleShouldNotBeFound("moduleId.equals=" + (moduleId + 1));
    }


    @Test
    @Transactional
    public void getAllHistoriqueEtudiantModulesByEtudiantIsEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);
        Etudiant etudiant = EtudiantResourceIT.createEntity(em);
        em.persist(etudiant);
        em.flush();
        historiqueEtudiantModule.addEtudiant(etudiant);
        historiqueEtudiantModuleRepository.saveAndFlush(historiqueEtudiantModule);
        Long etudiantId = etudiant.getId();

        // Get all the historiqueEtudiantModuleList where etudiant equals to etudiantId
        defaultHistoriqueEtudiantModuleShouldBeFound("etudiantId.equals=" + etudiantId);

        // Get all the historiqueEtudiantModuleList where etudiant equals to etudiantId + 1
        defaultHistoriqueEtudiantModuleShouldNotBeFound("etudiantId.equals=" + (etudiantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHistoriqueEtudiantModuleShouldBeFound(String filter) throws Exception {
        restHistoriqueEtudiantModuleMockMvc.perform(get("/api/historique-etudiant-modules?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueEtudiantModule.getId().intValue())))
            .andExpect(jsonPath("$.[*].datedebut").value(hasItem(DEFAULT_DATEDEBUT.toString())))
            .andExpect(jsonPath("$.[*].datefin").value(hasItem(DEFAULT_DATEFIN.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())));

        // Check, that the count call also returns 1
        restHistoriqueEtudiantModuleMockMvc.perform(get("/api/historique-etudiant-modules/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHistoriqueEtudiantModuleShouldNotBeFound(String filter) throws Exception {
        restHistoriqueEtudiantModuleMockMvc.perform(get("/api/historique-etudiant-modules?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHistoriqueEtudiantModuleMockMvc.perform(get("/api/historique-etudiant-modules/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingHistoriqueEtudiantModule() throws Exception {
        // Get the historiqueEtudiantModule
        restHistoriqueEtudiantModuleMockMvc.perform(get("/api/historique-etudiant-modules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistoriqueEtudiantModule() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleService.save(historiqueEtudiantModule);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockHistoriqueEtudiantModuleSearchRepository);

        int databaseSizeBeforeUpdate = historiqueEtudiantModuleRepository.findAll().size();

        // Update the historiqueEtudiantModule
        HistoriqueEtudiantModule updatedHistoriqueEtudiantModule = historiqueEtudiantModuleRepository.findById(historiqueEtudiantModule.getId()).get();
        // Disconnect from session so that the updates on updatedHistoriqueEtudiantModule are not directly saved in db
        em.detach(updatedHistoriqueEtudiantModule);
        updatedHistoriqueEtudiantModule
            .datedebut(UPDATED_DATEDEBUT)
            .datefin(UPDATED_DATEFIN)
            .note(UPDATED_NOTE);

        restHistoriqueEtudiantModuleMockMvc.perform(put("/api/historique-etudiant-modules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedHistoriqueEtudiantModule)))
            .andExpect(status().isOk());

        // Validate the HistoriqueEtudiantModule in the database
        List<HistoriqueEtudiantModule> historiqueEtudiantModuleList = historiqueEtudiantModuleRepository.findAll();
        assertThat(historiqueEtudiantModuleList).hasSize(databaseSizeBeforeUpdate);
        HistoriqueEtudiantModule testHistoriqueEtudiantModule = historiqueEtudiantModuleList.get(historiqueEtudiantModuleList.size() - 1);
        assertThat(testHistoriqueEtudiantModule.getDatedebut()).isEqualTo(UPDATED_DATEDEBUT);
        assertThat(testHistoriqueEtudiantModule.getDatefin()).isEqualTo(UPDATED_DATEFIN);
        assertThat(testHistoriqueEtudiantModule.getNote()).isEqualTo(UPDATED_NOTE);

        // Validate the HistoriqueEtudiantModule in Elasticsearch
        verify(mockHistoriqueEtudiantModuleSearchRepository, times(1)).save(testHistoriqueEtudiantModule);
    }

    @Test
    @Transactional
    public void updateNonExistingHistoriqueEtudiantModule() throws Exception {
        int databaseSizeBeforeUpdate = historiqueEtudiantModuleRepository.findAll().size();

        // Create the HistoriqueEtudiantModule

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueEtudiantModuleMockMvc.perform(put("/api/historique-etudiant-modules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueEtudiantModule)))
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueEtudiantModule in the database
        List<HistoriqueEtudiantModule> historiqueEtudiantModuleList = historiqueEtudiantModuleRepository.findAll();
        assertThat(historiqueEtudiantModuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HistoriqueEtudiantModule in Elasticsearch
        verify(mockHistoriqueEtudiantModuleSearchRepository, times(0)).save(historiqueEtudiantModule);
    }

    @Test
    @Transactional
    public void deleteHistoriqueEtudiantModule() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleService.save(historiqueEtudiantModule);

        int databaseSizeBeforeDelete = historiqueEtudiantModuleRepository.findAll().size();

        // Delete the historiqueEtudiantModule
        restHistoriqueEtudiantModuleMockMvc.perform(delete("/api/historique-etudiant-modules/{id}", historiqueEtudiantModule.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HistoriqueEtudiantModule> historiqueEtudiantModuleList = historiqueEtudiantModuleRepository.findAll();
        assertThat(historiqueEtudiantModuleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HistoriqueEtudiantModule in Elasticsearch
        verify(mockHistoriqueEtudiantModuleSearchRepository, times(1)).deleteById(historiqueEtudiantModule.getId());
    }

    @Test
    @Transactional
    public void searchHistoriqueEtudiantModule() throws Exception {
        // Initialize the database
        historiqueEtudiantModuleService.save(historiqueEtudiantModule);
        when(mockHistoriqueEtudiantModuleSearchRepository.search(queryStringQuery("id:" + historiqueEtudiantModule.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(historiqueEtudiantModule), PageRequest.of(0, 1), 1));
        // Search the historiqueEtudiantModule
        restHistoriqueEtudiantModuleMockMvc.perform(get("/api/_search/historique-etudiant-modules?query=id:" + historiqueEtudiantModule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueEtudiantModule.getId().intValue())))
            .andExpect(jsonPath("$.[*].datedebut").value(hasItem(DEFAULT_DATEDEBUT.toString())))
            .andExpect(jsonPath("$.[*].datefin").value(hasItem(DEFAULT_DATEFIN.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())));
    }
}
