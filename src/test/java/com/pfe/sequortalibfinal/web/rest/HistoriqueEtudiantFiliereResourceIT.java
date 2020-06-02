package com.pfe.sequortalibfinal.web.rest;

import com.pfe.sequortalibfinal.SequortalibfinalApp;
import com.pfe.sequortalibfinal.domain.HistoriqueEtudiantFiliere;
import com.pfe.sequortalibfinal.domain.Etudiant;
import com.pfe.sequortalibfinal.repository.HistoriqueEtudiantFiliereRepository;
import com.pfe.sequortalibfinal.repository.search.HistoriqueEtudiantFiliereSearchRepository;
import com.pfe.sequortalibfinal.service.HistoriqueEtudiantFiliereService;
import com.pfe.sequortalibfinal.service.dto.HistoriqueEtudiantFiliereCriteria;
import com.pfe.sequortalibfinal.service.HistoriqueEtudiantFiliereQueryService;

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
 * Integration tests for the {@link HistoriqueEtudiantFiliereResource} REST controller.
 */
@SpringBootTest(classes = SequortalibfinalApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class HistoriqueEtudiantFiliereResourceIT {

    private static final LocalDate DEFAULT_DATEDEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEDEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATEDEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATEFIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEFIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATEFIN = LocalDate.ofEpochDay(-1L);

    @Autowired
    private HistoriqueEtudiantFiliereRepository historiqueEtudiantFiliereRepository;

    @Autowired
    private HistoriqueEtudiantFiliereService historiqueEtudiantFiliereService;

    /**
     * This repository is mocked in the com.pfe.sequortalibfinal.repository.search test package.
     *
     * @see com.pfe.sequortalibfinal.repository.search.HistoriqueEtudiantFiliereSearchRepositoryMockConfiguration
     */
    @Autowired
    private HistoriqueEtudiantFiliereSearchRepository mockHistoriqueEtudiantFiliereSearchRepository;

    @Autowired
    private HistoriqueEtudiantFiliereQueryService historiqueEtudiantFiliereQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriqueEtudiantFiliereMockMvc;

    private HistoriqueEtudiantFiliere historiqueEtudiantFiliere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueEtudiantFiliere createEntity(EntityManager em) {
        HistoriqueEtudiantFiliere historiqueEtudiantFiliere = new HistoriqueEtudiantFiliere()
            .datedebut(DEFAULT_DATEDEBUT)
            .datefin(DEFAULT_DATEFIN);
        return historiqueEtudiantFiliere;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueEtudiantFiliere createUpdatedEntity(EntityManager em) {
        HistoriqueEtudiantFiliere historiqueEtudiantFiliere = new HistoriqueEtudiantFiliere()
            .datedebut(UPDATED_DATEDEBUT)
            .datefin(UPDATED_DATEFIN);
        return historiqueEtudiantFiliere;
    }

    @BeforeEach
    public void initTest() {
        historiqueEtudiantFiliere = createEntity(em);
    }

    @Test
    @Transactional
    public void createHistoriqueEtudiantFiliere() throws Exception {
        int databaseSizeBeforeCreate = historiqueEtudiantFiliereRepository.findAll().size();

        // Create the HistoriqueEtudiantFiliere
        restHistoriqueEtudiantFiliereMockMvc.perform(post("/api/historique-etudiant-filieres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueEtudiantFiliere)))
            .andExpect(status().isCreated());

        // Validate the HistoriqueEtudiantFiliere in the database
        List<HistoriqueEtudiantFiliere> historiqueEtudiantFiliereList = historiqueEtudiantFiliereRepository.findAll();
        assertThat(historiqueEtudiantFiliereList).hasSize(databaseSizeBeforeCreate + 1);
        HistoriqueEtudiantFiliere testHistoriqueEtudiantFiliere = historiqueEtudiantFiliereList.get(historiqueEtudiantFiliereList.size() - 1);
        assertThat(testHistoriqueEtudiantFiliere.getDatedebut()).isEqualTo(DEFAULT_DATEDEBUT);
        assertThat(testHistoriqueEtudiantFiliere.getDatefin()).isEqualTo(DEFAULT_DATEFIN);

        // Validate the HistoriqueEtudiantFiliere in Elasticsearch
        verify(mockHistoriqueEtudiantFiliereSearchRepository, times(1)).save(testHistoriqueEtudiantFiliere);
    }

    @Test
    @Transactional
    public void createHistoriqueEtudiantFiliereWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historiqueEtudiantFiliereRepository.findAll().size();

        // Create the HistoriqueEtudiantFiliere with an existing ID
        historiqueEtudiantFiliere.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriqueEtudiantFiliereMockMvc.perform(post("/api/historique-etudiant-filieres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueEtudiantFiliere)))
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueEtudiantFiliere in the database
        List<HistoriqueEtudiantFiliere> historiqueEtudiantFiliereList = historiqueEtudiantFiliereRepository.findAll();
        assertThat(historiqueEtudiantFiliereList).hasSize(databaseSizeBeforeCreate);

        // Validate the HistoriqueEtudiantFiliere in Elasticsearch
        verify(mockHistoriqueEtudiantFiliereSearchRepository, times(0)).save(historiqueEtudiantFiliere);
    }


    @Test
    @Transactional
    public void checkDatedebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = historiqueEtudiantFiliereRepository.findAll().size();
        // set the field null
        historiqueEtudiantFiliere.setDatedebut(null);

        // Create the HistoriqueEtudiantFiliere, which fails.

        restHistoriqueEtudiantFiliereMockMvc.perform(post("/api/historique-etudiant-filieres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueEtudiantFiliere)))
            .andExpect(status().isBadRequest());

        List<HistoriqueEtudiantFiliere> historiqueEtudiantFiliereList = historiqueEtudiantFiliereRepository.findAll();
        assertThat(historiqueEtudiantFiliereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieres() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList
        restHistoriqueEtudiantFiliereMockMvc.perform(get("/api/historique-etudiant-filieres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueEtudiantFiliere.getId().intValue())))
            .andExpect(jsonPath("$.[*].datedebut").value(hasItem(DEFAULT_DATEDEBUT.toString())))
            .andExpect(jsonPath("$.[*].datefin").value(hasItem(DEFAULT_DATEFIN.toString())));
    }
    
    @Test
    @Transactional
    public void getHistoriqueEtudiantFiliere() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get the historiqueEtudiantFiliere
        restHistoriqueEtudiantFiliereMockMvc.perform(get("/api/historique-etudiant-filieres/{id}", historiqueEtudiantFiliere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiqueEtudiantFiliere.getId().intValue()))
            .andExpect(jsonPath("$.datedebut").value(DEFAULT_DATEDEBUT.toString()))
            .andExpect(jsonPath("$.datefin").value(DEFAULT_DATEFIN.toString()));
    }


    @Test
    @Transactional
    public void getHistoriqueEtudiantFilieresByIdFiltering() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        Long id = historiqueEtudiantFiliere.getId();

        defaultHistoriqueEtudiantFiliereShouldBeFound("id.equals=" + id);
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("id.notEquals=" + id);

        defaultHistoriqueEtudiantFiliereShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("id.greaterThan=" + id);

        defaultHistoriqueEtudiantFiliereShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByDatedebutIsEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList where datedebut equals to DEFAULT_DATEDEBUT
        defaultHistoriqueEtudiantFiliereShouldBeFound("datedebut.equals=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEtudiantFiliereList where datedebut equals to UPDATED_DATEDEBUT
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("datedebut.equals=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByDatedebutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList where datedebut not equals to DEFAULT_DATEDEBUT
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("datedebut.notEquals=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEtudiantFiliereList where datedebut not equals to UPDATED_DATEDEBUT
        defaultHistoriqueEtudiantFiliereShouldBeFound("datedebut.notEquals=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByDatedebutIsInShouldWork() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList where datedebut in DEFAULT_DATEDEBUT or UPDATED_DATEDEBUT
        defaultHistoriqueEtudiantFiliereShouldBeFound("datedebut.in=" + DEFAULT_DATEDEBUT + "," + UPDATED_DATEDEBUT);

        // Get all the historiqueEtudiantFiliereList where datedebut equals to UPDATED_DATEDEBUT
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("datedebut.in=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByDatedebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList where datedebut is not null
        defaultHistoriqueEtudiantFiliereShouldBeFound("datedebut.specified=true");

        // Get all the historiqueEtudiantFiliereList where datedebut is null
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("datedebut.specified=false");
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByDatedebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList where datedebut is greater than or equal to DEFAULT_DATEDEBUT
        defaultHistoriqueEtudiantFiliereShouldBeFound("datedebut.greaterThanOrEqual=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEtudiantFiliereList where datedebut is greater than or equal to UPDATED_DATEDEBUT
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("datedebut.greaterThanOrEqual=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByDatedebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList where datedebut is less than or equal to DEFAULT_DATEDEBUT
        defaultHistoriqueEtudiantFiliereShouldBeFound("datedebut.lessThanOrEqual=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEtudiantFiliereList where datedebut is less than or equal to SMALLER_DATEDEBUT
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("datedebut.lessThanOrEqual=" + SMALLER_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByDatedebutIsLessThanSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList where datedebut is less than DEFAULT_DATEDEBUT
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("datedebut.lessThan=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEtudiantFiliereList where datedebut is less than UPDATED_DATEDEBUT
        defaultHistoriqueEtudiantFiliereShouldBeFound("datedebut.lessThan=" + UPDATED_DATEDEBUT);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByDatedebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList where datedebut is greater than DEFAULT_DATEDEBUT
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("datedebut.greaterThan=" + DEFAULT_DATEDEBUT);

        // Get all the historiqueEtudiantFiliereList where datedebut is greater than SMALLER_DATEDEBUT
        defaultHistoriqueEtudiantFiliereShouldBeFound("datedebut.greaterThan=" + SMALLER_DATEDEBUT);
    }


    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByDatefinIsEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList where datefin equals to DEFAULT_DATEFIN
        defaultHistoriqueEtudiantFiliereShouldBeFound("datefin.equals=" + DEFAULT_DATEFIN);

        // Get all the historiqueEtudiantFiliereList where datefin equals to UPDATED_DATEFIN
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("datefin.equals=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByDatefinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList where datefin not equals to DEFAULT_DATEFIN
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("datefin.notEquals=" + DEFAULT_DATEFIN);

        // Get all the historiqueEtudiantFiliereList where datefin not equals to UPDATED_DATEFIN
        defaultHistoriqueEtudiantFiliereShouldBeFound("datefin.notEquals=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByDatefinIsInShouldWork() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList where datefin in DEFAULT_DATEFIN or UPDATED_DATEFIN
        defaultHistoriqueEtudiantFiliereShouldBeFound("datefin.in=" + DEFAULT_DATEFIN + "," + UPDATED_DATEFIN);

        // Get all the historiqueEtudiantFiliereList where datefin equals to UPDATED_DATEFIN
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("datefin.in=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByDatefinIsNullOrNotNull() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList where datefin is not null
        defaultHistoriqueEtudiantFiliereShouldBeFound("datefin.specified=true");

        // Get all the historiqueEtudiantFiliereList where datefin is null
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("datefin.specified=false");
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByDatefinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList where datefin is greater than or equal to DEFAULT_DATEFIN
        defaultHistoriqueEtudiantFiliereShouldBeFound("datefin.greaterThanOrEqual=" + DEFAULT_DATEFIN);

        // Get all the historiqueEtudiantFiliereList where datefin is greater than or equal to UPDATED_DATEFIN
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("datefin.greaterThanOrEqual=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByDatefinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList where datefin is less than or equal to DEFAULT_DATEFIN
        defaultHistoriqueEtudiantFiliereShouldBeFound("datefin.lessThanOrEqual=" + DEFAULT_DATEFIN);

        // Get all the historiqueEtudiantFiliereList where datefin is less than or equal to SMALLER_DATEFIN
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("datefin.lessThanOrEqual=" + SMALLER_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByDatefinIsLessThanSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList where datefin is less than DEFAULT_DATEFIN
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("datefin.lessThan=" + DEFAULT_DATEFIN);

        // Get all the historiqueEtudiantFiliereList where datefin is less than UPDATED_DATEFIN
        defaultHistoriqueEtudiantFiliereShouldBeFound("datefin.lessThan=" + UPDATED_DATEFIN);
    }

    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByDatefinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);

        // Get all the historiqueEtudiantFiliereList where datefin is greater than DEFAULT_DATEFIN
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("datefin.greaterThan=" + DEFAULT_DATEFIN);

        // Get all the historiqueEtudiantFiliereList where datefin is greater than SMALLER_DATEFIN
        defaultHistoriqueEtudiantFiliereShouldBeFound("datefin.greaterThan=" + SMALLER_DATEFIN);
    }


    @Test
    @Transactional
    public void getAllHistoriqueEtudiantFilieresByEtudiantIsEqualToSomething() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);
        Etudiant etudiant = EtudiantResourceIT.createEntity(em);
        em.persist(etudiant);
        em.flush();
        historiqueEtudiantFiliere.addEtudiant(etudiant);
        historiqueEtudiantFiliereRepository.saveAndFlush(historiqueEtudiantFiliere);
        Long etudiantId = etudiant.getId();

        // Get all the historiqueEtudiantFiliereList where etudiant equals to etudiantId
        defaultHistoriqueEtudiantFiliereShouldBeFound("etudiantId.equals=" + etudiantId);

        // Get all the historiqueEtudiantFiliereList where etudiant equals to etudiantId + 1
        defaultHistoriqueEtudiantFiliereShouldNotBeFound("etudiantId.equals=" + (etudiantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHistoriqueEtudiantFiliereShouldBeFound(String filter) throws Exception {
        restHistoriqueEtudiantFiliereMockMvc.perform(get("/api/historique-etudiant-filieres?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueEtudiantFiliere.getId().intValue())))
            .andExpect(jsonPath("$.[*].datedebut").value(hasItem(DEFAULT_DATEDEBUT.toString())))
            .andExpect(jsonPath("$.[*].datefin").value(hasItem(DEFAULT_DATEFIN.toString())));

        // Check, that the count call also returns 1
        restHistoriqueEtudiantFiliereMockMvc.perform(get("/api/historique-etudiant-filieres/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHistoriqueEtudiantFiliereShouldNotBeFound(String filter) throws Exception {
        restHistoriqueEtudiantFiliereMockMvc.perform(get("/api/historique-etudiant-filieres?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHistoriqueEtudiantFiliereMockMvc.perform(get("/api/historique-etudiant-filieres/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingHistoriqueEtudiantFiliere() throws Exception {
        // Get the historiqueEtudiantFiliere
        restHistoriqueEtudiantFiliereMockMvc.perform(get("/api/historique-etudiant-filieres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistoriqueEtudiantFiliere() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereService.save(historiqueEtudiantFiliere);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockHistoriqueEtudiantFiliereSearchRepository);

        int databaseSizeBeforeUpdate = historiqueEtudiantFiliereRepository.findAll().size();

        // Update the historiqueEtudiantFiliere
        HistoriqueEtudiantFiliere updatedHistoriqueEtudiantFiliere = historiqueEtudiantFiliereRepository.findById(historiqueEtudiantFiliere.getId()).get();
        // Disconnect from session so that the updates on updatedHistoriqueEtudiantFiliere are not directly saved in db
        em.detach(updatedHistoriqueEtudiantFiliere);
        updatedHistoriqueEtudiantFiliere
            .datedebut(UPDATED_DATEDEBUT)
            .datefin(UPDATED_DATEFIN);

        restHistoriqueEtudiantFiliereMockMvc.perform(put("/api/historique-etudiant-filieres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedHistoriqueEtudiantFiliere)))
            .andExpect(status().isOk());

        // Validate the HistoriqueEtudiantFiliere in the database
        List<HistoriqueEtudiantFiliere> historiqueEtudiantFiliereList = historiqueEtudiantFiliereRepository.findAll();
        assertThat(historiqueEtudiantFiliereList).hasSize(databaseSizeBeforeUpdate);
        HistoriqueEtudiantFiliere testHistoriqueEtudiantFiliere = historiqueEtudiantFiliereList.get(historiqueEtudiantFiliereList.size() - 1);
        assertThat(testHistoriqueEtudiantFiliere.getDatedebut()).isEqualTo(UPDATED_DATEDEBUT);
        assertThat(testHistoriqueEtudiantFiliere.getDatefin()).isEqualTo(UPDATED_DATEFIN);

        // Validate the HistoriqueEtudiantFiliere in Elasticsearch
        verify(mockHistoriqueEtudiantFiliereSearchRepository, times(1)).save(testHistoriqueEtudiantFiliere);
    }

    @Test
    @Transactional
    public void updateNonExistingHistoriqueEtudiantFiliere() throws Exception {
        int databaseSizeBeforeUpdate = historiqueEtudiantFiliereRepository.findAll().size();

        // Create the HistoriqueEtudiantFiliere

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueEtudiantFiliereMockMvc.perform(put("/api/historique-etudiant-filieres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueEtudiantFiliere)))
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueEtudiantFiliere in the database
        List<HistoriqueEtudiantFiliere> historiqueEtudiantFiliereList = historiqueEtudiantFiliereRepository.findAll();
        assertThat(historiqueEtudiantFiliereList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HistoriqueEtudiantFiliere in Elasticsearch
        verify(mockHistoriqueEtudiantFiliereSearchRepository, times(0)).save(historiqueEtudiantFiliere);
    }

    @Test
    @Transactional
    public void deleteHistoriqueEtudiantFiliere() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereService.save(historiqueEtudiantFiliere);

        int databaseSizeBeforeDelete = historiqueEtudiantFiliereRepository.findAll().size();

        // Delete the historiqueEtudiantFiliere
        restHistoriqueEtudiantFiliereMockMvc.perform(delete("/api/historique-etudiant-filieres/{id}", historiqueEtudiantFiliere.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HistoriqueEtudiantFiliere> historiqueEtudiantFiliereList = historiqueEtudiantFiliereRepository.findAll();
        assertThat(historiqueEtudiantFiliereList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HistoriqueEtudiantFiliere in Elasticsearch
        verify(mockHistoriqueEtudiantFiliereSearchRepository, times(1)).deleteById(historiqueEtudiantFiliere.getId());
    }

    @Test
    @Transactional
    public void searchHistoriqueEtudiantFiliere() throws Exception {
        // Initialize the database
        historiqueEtudiantFiliereService.save(historiqueEtudiantFiliere);
        when(mockHistoriqueEtudiantFiliereSearchRepository.search(queryStringQuery("id:" + historiqueEtudiantFiliere.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(historiqueEtudiantFiliere), PageRequest.of(0, 1), 1));
        // Search the historiqueEtudiantFiliere
        restHistoriqueEtudiantFiliereMockMvc.perform(get("/api/_search/historique-etudiant-filieres?query=id:" + historiqueEtudiantFiliere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueEtudiantFiliere.getId().intValue())))
            .andExpect(jsonPath("$.[*].datedebut").value(hasItem(DEFAULT_DATEDEBUT.toString())))
            .andExpect(jsonPath("$.[*].datefin").value(hasItem(DEFAULT_DATEFIN.toString())));
    }
}
