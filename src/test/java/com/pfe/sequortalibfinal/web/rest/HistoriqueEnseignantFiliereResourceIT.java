package com.pfe.sequortalibfinal.web.rest;

import com.pfe.sequortalibfinal.SequortalibfinalApp;
import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantFiliere;
import com.pfe.sequortalibfinal.domain.Filiere;
import com.pfe.sequortalibfinal.domain.Enseignant;
import com.pfe.sequortalibfinal.repository.HistoriqueEnseignantFiliereRepository;
import com.pfe.sequortalibfinal.repository.search.HistoriqueEnseignantFiliereSearchRepository;
import com.pfe.sequortalibfinal.service.HistoriqueEnseignantFiliereService;
import com.pfe.sequortalibfinal.service.dto.HistoriqueEnseignantFiliereCriteria;
import com.pfe.sequortalibfinal.service.HistoriqueEnseignantFiliereQueryService;

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
 * Integration tests for the {@link HistoriqueEnseignantFiliereResource} REST controller.
 */
@SpringBootTest(classes = SequortalibfinalApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class HistoriqueEnseignantFiliereResourceIT {

    private static final LocalDate DEFAULT_DATEDEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEDEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATEDEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATEFIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEFIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATEFIN = LocalDate.ofEpochDay(-1L);

    @Autowired
    private HistoriqueEnseignantFiliereRepository historiqueEnseignantFiliereRepository;

    @Autowired
    private HistoriqueEnseignantFiliereService historiqueEnseignantFiliereService;

    /**
     * This repository is mocked in the com.pfe.sequortalibfinal.repository.search test package.
     *
     * @see com.pfe.sequortalibfinal.repository.search.HistoriqueEnseignantFiliereSearchRepositoryMockConfiguration
     */
    @Autowired
    private HistoriqueEnseignantFiliereSearchRepository mockHistoriqueEnseignantFiliereSearchRepository;

    @Autowired
    private HistoriqueEnseignantFiliereQueryService historiqueEnseignantFiliereQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriqueEnseignantFiliereMockMvc;

    private HistoriqueEnseignantFiliere historiqueEnseignantFiliere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueEnseignantFiliere createEntity(EntityManager em) {
        HistoriqueEnseignantFiliere historiqueEnseignantFiliere = new HistoriqueEnseignantFiliere()
            .datedebut(DEFAULT_DATEDEBUT)
            .datefin(DEFAULT_DATEFIN);
        return historiqueEnseignantFiliere;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueEnseignantFiliere createUpdatedEntity(EntityManager em) {
        HistoriqueEnseignantFiliere historiqueEnseignantFiliere = new HistoriqueEnseignantFiliere()
            .datedebut(UPDATED_DATEDEBUT)
            .datefin(UPDATED_DATEFIN);
        return historiqueEnseignantFiliere;
    }

    @BeforeEach
    public void initTest() {
        historiqueEnseignantFiliere = createEntity(em);
    }

    @Test
    @Transactional
    public void createHistoriqueEnseignantFiliere() throws Exception {
        int databaseSizeBeforeCreate = historiqueEnseignantFiliereRepository.findAll().size();

        // Create the HistoriqueEnseignantFiliere
        restHistoriqueEnseignantFiliereMockMvc.perform(post("/api/historique-enseignant-filieres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueEnseignantFiliere)))
            .andExpect(status().isCreated());

        // Validate the HistoriqueEnseignantFiliere in the database
        List<HistoriqueEnseignantFiliere> historiqueEnseignantFiliereList = historiqueEnseignantFiliereRepository.findAll();
        assertThat(historiqueEnseignantFiliereList).hasSize(databaseSizeBeforeCreate + 1);
        HistoriqueEnseignantFiliere testHistoriqueEnseignantFiliere = historiqueEnseignantFiliereList.get(historiqueEnseignantFiliereList.size() - 1);
        assertThat(testHistoriqueEnseignantFiliere.getDatedebut()).isEqualTo(DEFAULT_DATEDEBUT);
        assertThat(testHistoriqueEnseignantFiliere.getDatefin()).isEqualTo(DEFAULT_DATEFIN);

        // Validate the HistoriqueEnseignantFiliere in Elasticsearch
        verify(mockHistoriqueEnseignantFiliereSearchRepository, times(1)).save(testHistoriqueEnseignantFiliere);
    }

    @Test
    @Transactional
    public void createHistoriqueEnseignantFiliereWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historiqueEnseignantFiliereRepository.findAll().size();

        // Create the HistoriqueEnseignantFiliere with an existing ID
        historiqueEnseignantFiliere.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriqueEnseignantFiliereMockMvc.perform(post("/api/historique-enseignant-filieres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueEnseignantFiliere)))
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueEnseignantFiliere in the database
        List<HistoriqueEnseignantFiliere> historiqueEnseignantFiliereList = historiqueEnseignantFiliereRepository.findAll();
        assertThat(historiqueEnseignantFiliereList).hasSize(databaseSizeBeforeCreate);

        // Validate the HistoriqueEnseignantFiliere in Elasticsearch
        verify(mockHistoriqueEnseignantFiliereSearchRepository, times(0)).save(historiqueEnseignantFiliere);
    }


    @Test
    @Transactional
    public void checkDatedebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = historiqueEnseignantFiliereRepository.findAll().size();
        // set the field null
        historiqueEnseignantFiliere.setDatedebut(null);

        // Create the HistoriqueEnseignantFiliere, which fails.

        restHistoriqueEnseignantFiliereMockMvc.perform(post("/api/historique-enseignant-filieres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueEnseignantFiliere)))
            .andExpect(status().isBadRequest());

        List<HistoriqueEnseignantFiliere> historiqueEnseignantFiliereList = historiqueEnseignantFiliereRepository.findAll();
        assertThat(historiqueEnseignantFiliereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieres() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList
        restHistoriqueEnseignantFiliereMockMvc.perform(get("/api/historique-enseignant-filieres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueEnseignantFiliere.getId().intValue())))
            .andExpect(jsonPath("$.[*].datedebut").value(hasItem(DEFAULT_DATEDEBUT.toString())))
            .andExpect(jsonPath("$.[*].datefin").value(hasItem(DEFAULT_DATEFIN.toString())));
    }
    
    @Test
    @Transactional
    public void getHistoriqueEnseignantFiliere() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get the historiqueEnseignantFiliere
        restHistoriqueEnseignantFiliereMockMvc.perform(get("/api/historique-enseignant-filieres/{id}", historiqueEnseignantFiliere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiqueEnseignantFiliere.getId().intValue()))
            .andExpect(jsonPath("$.datedebut").value(DEFAULT_DATEDEBUT.toString()))
            .andExpect(jsonPath("$.datefin").value(DEFAULT_DATEFIN.toString()));
    }


    @Test
    @Transactional
    public void getHistoriqueEnseignantFilieresByIdFiltering() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        Long id = historiqueEnseignantFiliere.getId();

        defaultHistoriqueEnseignantFiliereShouldBeFound("id.equals=" + id);
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("id.notEquals=" + id);

        defaultHistoriqueEnseignantFiliereShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("id.greaterThan=" + id);

        defaultHistoriqueEnseignantFiliereShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByDatedebutIsEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList where datedebut equals to DEFAULT_DATEDEBUT
        defaultHistoriqueEnseignantFiliereShouldBeFound("datedebut.equals=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEnseignantFiliereList where datedebut equals to UPDATED_DATEDEBUT
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("datedebut.equals=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByDatedebutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList where datedebut not equals to DEFAULT_DATEDEBUT
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("datedebut.notEquals=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEnseignantFiliereList where datedebut not equals to UPDATED_DATEDEBUT
        defaultHistoriqueEnseignantFiliereShouldBeFound("datedebut.notEquals=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByDatedebutIsInShouldWork() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList where datedebut in DEFAULT_DATEDEBUT or UPDATED_DATEDEBUT
        defaultHistoriqueEnseignantFiliereShouldBeFound("datedebut.in=" + DEFAULT_DATEDEBUT + "," + UPDATED_DATEDEBUT);

        // Get all the historiqueEnseignantFiliereList where datedebut equals to UPDATED_DATEDEBUT
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("datedebut.in=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByDatedebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList where datedebut is not null
        defaultHistoriqueEnseignantFiliereShouldBeFound("datedebut.specified=true");

        // Get all the historiqueEnseignantFiliereList where datedebut is null
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("datedebut.specified=false");
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByDatedebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList where datedebut is greater than or equal to DEFAULT_DATEDEBUT
        defaultHistoriqueEnseignantFiliereShouldBeFound("datedebut.greaterThanOrEqual=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEnseignantFiliereList where datedebut is greater than or equal to UPDATED_DATEDEBUT
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("datedebut.greaterThanOrEqual=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByDatedebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList where datedebut is less than or equal to DEFAULT_DATEDEBUT
        defaultHistoriqueEnseignantFiliereShouldBeFound("datedebut.lessThanOrEqual=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEnseignantFiliereList where datedebut is less than or equal to SMALLER_DATEDEBUT
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("datedebut.lessThanOrEqual=" + SMALLER_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByDatedebutIsLessThanSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList where datedebut is less than DEFAULT_DATEDEBUT
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("datedebut.lessThan=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEnseignantFiliereList where datedebut is less than UPDATED_DATEDEBUT
        defaultHistoriqueEnseignantFiliereShouldBeFound("datedebut.lessThan=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByDatedebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList where datedebut is greater than DEFAULT_DATEDEBUT
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("datedebut.greaterThan=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEnseignantFiliereList where datedebut is greater than SMALLER_DATEDEBUT
        defaultHistoriqueEnseignantFiliereShouldBeFound("datedebut.greaterThan=" + SMALLER_DATEDEBUT);
    }


    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByDatefinIsEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList where datefin equals to DEFAULT_DATEFIN
        defaultHistoriqueEnseignantFiliereShouldBeFound("datefin.equals=" + DEFAULT_DATEFIN);

        // Get all the historiqueEnseignantFiliereList where datefin equals to UPDATED_DATEFIN
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("datefin.equals=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByDatefinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList where datefin not equals to DEFAULT_DATEFIN
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("datefin.notEquals=" + DEFAULT_DATEFIN);

        // Get all the historiqueEnseignantFiliereList where datefin not equals to UPDATED_DATEFIN
        defaultHistoriqueEnseignantFiliereShouldBeFound("datefin.notEquals=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByDatefinIsInShouldWork() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList where datefin in DEFAULT_DATEFIN or UPDATED_DATEFIN
        defaultHistoriqueEnseignantFiliereShouldBeFound("datefin.in=" + DEFAULT_DATEFIN + "," + UPDATED_DATEFIN);

        // Get all the historiqueEnseignantFiliereList where datefin equals to UPDATED_DATEFIN
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("datefin.in=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByDatefinIsNullOrNotNull() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList where datefin is not null
        defaultHistoriqueEnseignantFiliereShouldBeFound("datefin.specified=true");

        // Get all the historiqueEnseignantFiliereList where datefin is null
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("datefin.specified=false");
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByDatefinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList where datefin is greater than or equal to DEFAULT_DATEFIN
        defaultHistoriqueEnseignantFiliereShouldBeFound("datefin.greaterThanOrEqual=" + DEFAULT_DATEFIN);

        // Get all the historiqueEnseignantFiliereList where datefin is greater than or equal to UPDATED_DATEFIN
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("datefin.greaterThanOrEqual=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByDatefinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList where datefin is less than or equal to DEFAULT_DATEFIN
        defaultHistoriqueEnseignantFiliereShouldBeFound("datefin.lessThanOrEqual=" + DEFAULT_DATEFIN);

        // Get all the historiqueEnseignantFiliereList where datefin is less than or equal to SMALLER_DATEFIN
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("datefin.lessThanOrEqual=" + SMALLER_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByDatefinIsLessThanSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList where datefin is less than DEFAULT_DATEFIN
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("datefin.lessThan=" + DEFAULT_DATEFIN);

        // Get all the historiqueEnseignantFiliereList where datefin is less than UPDATED_DATEFIN
        defaultHistoriqueEnseignantFiliereShouldBeFound("datefin.lessThan=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByDatefinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);

        // Get all the historiqueEnseignantFiliereList where datefin is greater than DEFAULT_DATEFIN
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("datefin.greaterThan=" + DEFAULT_DATEFIN);

        // Get all the historiqueEnseignantFiliereList where datefin is greater than SMALLER_DATEFIN
        defaultHistoriqueEnseignantFiliereShouldBeFound("datefin.greaterThan=" + SMALLER_DATEFIN);
    }


    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByFiliereIsEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);
        Filiere filiere = FiliereResourceIT.createEntity(em);
        em.persist(filiere);
        em.flush();
        historiqueEnseignantFiliere.addFiliere(filiere);
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);
        Long filiereId = filiere.getId();

        // Get all the historiqueEnseignantFiliereList where filiere equals to filiereId
        defaultHistoriqueEnseignantFiliereShouldBeFound("filiereId.equals=" + filiereId);

        // Get all the historiqueEnseignantFiliereList where filiere equals to filiereId + 1
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("filiereId.equals=" + (filiereId + 1));
    }


    @Test
    @Transactional
    public void getAllHistoriqueEnseignantFilieresByEnseignantIsEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);
        Enseignant enseignant = EnseignantResourceIT.createEntity(em);
        em.persist(enseignant);
        em.flush();
        historiqueEnseignantFiliere.addEnseignant(enseignant);
        historiqueEnseignantFiliereRepository.saveAndFlush(historiqueEnseignantFiliere);
        Long enseignantId = enseignant.getId();

        // Get all the historiqueEnseignantFiliereList where enseignant equals to enseignantId
        defaultHistoriqueEnseignantFiliereShouldBeFound("enseignantId.equals=" + enseignantId);

        // Get all the historiqueEnseignantFiliereList where enseignant equals to enseignantId + 1
        defaultHistoriqueEnseignantFiliereShouldNotBeFound("enseignantId.equals=" + (enseignantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHistoriqueEnseignantFiliereShouldBeFound(String filter) throws Exception {
        restHistoriqueEnseignantFiliereMockMvc.perform(get("/api/historique-enseignant-filieres?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueEnseignantFiliere.getId().intValue())))
            .andExpect(jsonPath("$.[*].datedebut").value(hasItem(DEFAULT_DATEDEBUT.toString())))
            .andExpect(jsonPath("$.[*].datefin").value(hasItem(DEFAULT_DATEFIN.toString())));

        // Check, that the count call also returns 1
        restHistoriqueEnseignantFiliereMockMvc.perform(get("/api/historique-enseignant-filieres/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHistoriqueEnseignantFiliereShouldNotBeFound(String filter) throws Exception {
        restHistoriqueEnseignantFiliereMockMvc.perform(get("/api/historique-enseignant-filieres?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHistoriqueEnseignantFiliereMockMvc.perform(get("/api/historique-enseignant-filieres/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingHistoriqueEnseignantFiliere() throws Exception {
        // Get the historiqueEnseignantFiliere
        restHistoriqueEnseignantFiliereMockMvc.perform(get("/api/historique-enseignant-filieres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistoriqueEnseignantFiliere() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereService.save(historiqueEnseignantFiliere);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockHistoriqueEnseignantFiliereSearchRepository);

        int databaseSizeBeforeUpdate = historiqueEnseignantFiliereRepository.findAll().size();

        // Update the historiqueEnseignantFiliere
        HistoriqueEnseignantFiliere updatedHistoriqueEnseignantFiliere = historiqueEnseignantFiliereRepository.findById(historiqueEnseignantFiliere.getId()).get();
        // Disconnect from session so that the updates on updatedHistoriqueEnseignantFiliere are not directly saved in db
        em.detach(updatedHistoriqueEnseignantFiliere);
        updatedHistoriqueEnseignantFiliere
            .datedebut(UPDATED_DATEDEBUT)
            .datefin(UPDATED_DATEFIN);

        restHistoriqueEnseignantFiliereMockMvc.perform(put("/api/historique-enseignant-filieres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedHistoriqueEnseignantFiliere)))
            .andExpect(status().isOk());

        // Validate the HistoriqueEnseignantFiliere in the database
        List<HistoriqueEnseignantFiliere> historiqueEnseignantFiliereList = historiqueEnseignantFiliereRepository.findAll();
        assertThat(historiqueEnseignantFiliereList).hasSize(databaseSizeBeforeUpdate);
        HistoriqueEnseignantFiliere testHistoriqueEnseignantFiliere = historiqueEnseignantFiliereList.get(historiqueEnseignantFiliereList.size() - 1);
        assertThat(testHistoriqueEnseignantFiliere.getDatedebut()).isEqualTo(UPDATED_DATEDEBUT);
        assertThat(testHistoriqueEnseignantFiliere.getDatefin()).isEqualTo(UPDATED_DATEFIN);

        // Validate the HistoriqueEnseignantFiliere in Elasticsearch
        verify(mockHistoriqueEnseignantFiliereSearchRepository, times(1)).save(testHistoriqueEnseignantFiliere);
    }

    @Test
    @Transactional
    public void updateNonExistingHistoriqueEnseignantFiliere() throws Exception {
        int databaseSizeBeforeUpdate = historiqueEnseignantFiliereRepository.findAll().size();

        // Create the HistoriqueEnseignantFiliere

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueEnseignantFiliereMockMvc.perform(put("/api/historique-enseignant-filieres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueEnseignantFiliere)))
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueEnseignantFiliere in the database
        List<HistoriqueEnseignantFiliere> historiqueEnseignantFiliereList = historiqueEnseignantFiliereRepository.findAll();
        assertThat(historiqueEnseignantFiliereList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HistoriqueEnseignantFiliere in Elasticsearch
        verify(mockHistoriqueEnseignantFiliereSearchRepository, times(0)).save(historiqueEnseignantFiliere);
    }

    @Test
    @Transactional
    public void deleteHistoriqueEnseignantFiliere() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereService.save(historiqueEnseignantFiliere);

        int databaseSizeBeforeDelete = historiqueEnseignantFiliereRepository.findAll().size();

        // Delete the historiqueEnseignantFiliere
        restHistoriqueEnseignantFiliereMockMvc.perform(delete("/api/historique-enseignant-filieres/{id}", historiqueEnseignantFiliere.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HistoriqueEnseignantFiliere> historiqueEnseignantFiliereList = historiqueEnseignantFiliereRepository.findAll();
        assertThat(historiqueEnseignantFiliereList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HistoriqueEnseignantFiliere in Elasticsearch
        verify(mockHistoriqueEnseignantFiliereSearchRepository, times(1)).deleteById(historiqueEnseignantFiliere.getId());
    }

    @Test
    @Transactional
    public void searchHistoriqueEnseignantFiliere() throws Exception {
        // Initialize the database
        historiqueEnseignantFiliereService.save(historiqueEnseignantFiliere);
        when(mockHistoriqueEnseignantFiliereSearchRepository.search(queryStringQuery("id:" + historiqueEnseignantFiliere.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(historiqueEnseignantFiliere), PageRequest.of(0, 1), 1));
        // Search the historiqueEnseignantFiliere
        restHistoriqueEnseignantFiliereMockMvc.perform(get("/api/_search/historique-enseignant-filieres?query=id:" + historiqueEnseignantFiliere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueEnseignantFiliere.getId().intValue())))
            .andExpect(jsonPath("$.[*].datedebut").value(hasItem(DEFAULT_DATEDEBUT.toString())))
            .andExpect(jsonPath("$.[*].datefin").value(hasItem(DEFAULT_DATEFIN.toString())));
    }
}
