package com.pfe.sequortalibfinal.web.rest;

import com.pfe.sequortalibfinal.SequortalibfinalApp;
import com.pfe.sequortalibfinal.domain.Filiere;
import com.pfe.sequortalibfinal.domain.Module;
import com.pfe.sequortalibfinal.domain.Departement;
import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantFiliere;
import com.pfe.sequortalibfinal.domain.HistoriqueEtudiantFiliere;
import com.pfe.sequortalibfinal.repository.FiliereRepository;
import com.pfe.sequortalibfinal.repository.search.FiliereSearchRepository;
import com.pfe.sequortalibfinal.service.FiliereService;
import com.pfe.sequortalibfinal.service.dto.FiliereCriteria;
import com.pfe.sequortalibfinal.service.FiliereQueryService;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FiliereResource} REST controller.
 */
@SpringBootTest(classes = SequortalibfinalApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class FiliereResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Autowired
    private FiliereRepository filiereRepository;

    @Mock
    private FiliereRepository filiereRepositoryMock;

    @Mock
    private FiliereService filiereServiceMock;

    @Autowired
    private FiliereService filiereService;

    /**
     * This repository is mocked in the com.pfe.sequortalibfinal.repository.search test package.
     *
     * @see com.pfe.sequortalibfinal.repository.search.FiliereSearchRepositoryMockConfiguration
     */
    @Autowired
    private FiliereSearchRepository mockFiliereSearchRepository;

    @Autowired
    private FiliereQueryService filiereQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFiliereMockMvc;

    private Filiere filiere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filiere createEntity(EntityManager em) {
        Filiere filiere = new Filiere()
            .nom(DEFAULT_NOM);
        // Add required entity
        HistoriqueEnseignantFiliere historiqueEnseignantFiliere;
        if (TestUtil.findAll(em, HistoriqueEnseignantFiliere.class).isEmpty()) {
            historiqueEnseignantFiliere = HistoriqueEnseignantFiliereResourceIT.createEntity(em);
            em.persist(historiqueEnseignantFiliere);
            em.flush();
        } else {
            historiqueEnseignantFiliere = TestUtil.findAll(em, HistoriqueEnseignantFiliere.class).get(0);
        }
        filiere.setHistoriqueEnseignantFiliere(historiqueEnseignantFiliere);
        return filiere;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filiere createUpdatedEntity(EntityManager em) {
        Filiere filiere = new Filiere()
            .nom(UPDATED_NOM);
        // Add required entity
        HistoriqueEnseignantFiliere historiqueEnseignantFiliere;
        if (TestUtil.findAll(em, HistoriqueEnseignantFiliere.class).isEmpty()) {
            historiqueEnseignantFiliere = HistoriqueEnseignantFiliereResourceIT.createUpdatedEntity(em);
            em.persist(historiqueEnseignantFiliere);
            em.flush();
        } else {
            historiqueEnseignantFiliere = TestUtil.findAll(em, HistoriqueEnseignantFiliere.class).get(0);
        }
        filiere.setHistoriqueEnseignantFiliere(historiqueEnseignantFiliere);
        return filiere;
    }

    @BeforeEach
    public void initTest() {
        filiere = createEntity(em);
    }

    @Test
    @Transactional
    public void createFiliere() throws Exception {
        int databaseSizeBeforeCreate = filiereRepository.findAll().size();

        // Create the Filiere
        restFiliereMockMvc.perform(post("/api/filieres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(filiere)))
            .andExpect(status().isCreated());

        // Validate the Filiere in the database
        List<Filiere> filiereList = filiereRepository.findAll();
        assertThat(filiereList).hasSize(databaseSizeBeforeCreate + 1);
        Filiere testFiliere = filiereList.get(filiereList.size() - 1);
        assertThat(testFiliere.getNom()).isEqualTo(DEFAULT_NOM);

        // Validate the Filiere in Elasticsearch
        verify(mockFiliereSearchRepository, times(1)).save(testFiliere);
    }

    @Test
    @Transactional
    public void createFiliereWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = filiereRepository.findAll().size();

        // Create the Filiere with an existing ID
        filiere.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFiliereMockMvc.perform(post("/api/filieres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(filiere)))
            .andExpect(status().isBadRequest());

        // Validate the Filiere in the database
        List<Filiere> filiereList = filiereRepository.findAll();
        assertThat(filiereList).hasSize(databaseSizeBeforeCreate);

        // Validate the Filiere in Elasticsearch
        verify(mockFiliereSearchRepository, times(0)).save(filiere);
    }


    @Test
    @Transactional
    public void getAllFilieres() throws Exception {
        // Initialize the database
        filiereRepository.saveAndFlush(filiere);

        // Get all the filiereList
        restFiliereMockMvc.perform(get("/api/filieres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filiere.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllFilieresWithEagerRelationshipsIsEnabled() throws Exception {
        when(filiereServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiliereMockMvc.perform(get("/api/filieres?eagerload=true"))
            .andExpect(status().isOk());

        verify(filiereServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllFilieresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(filiereServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiliereMockMvc.perform(get("/api/filieres?eagerload=true"))
            .andExpect(status().isOk());

        verify(filiereServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getFiliere() throws Exception {
        // Initialize the database
        filiereRepository.saveAndFlush(filiere);

        // Get the filiere
        restFiliereMockMvc.perform(get("/api/filieres/{id}", filiere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filiere.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }


    @Test
    @Transactional
    public void getFilieresByIdFiltering() throws Exception {
        // Initialize the database
        filiereRepository.saveAndFlush(filiere);

        Long id = filiere.getId();

        defaultFiliereShouldBeFound("id.equals=" + id);
        defaultFiliereShouldNotBeFound("id.notEquals=" + id);

        defaultFiliereShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFiliereShouldNotBeFound("id.greaterThan=" + id);

        defaultFiliereShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFiliereShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFilieresByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        filiereRepository.saveAndFlush(filiere);

        // Get all the filiereList where nom equals to DEFAULT_NOM
        defaultFiliereShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the filiereList where nom equals to UPDATED_NOM
        defaultFiliereShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllFilieresByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        filiereRepository.saveAndFlush(filiere);

        // Get all the filiereList where nom not equals to DEFAULT_NOM
        defaultFiliereShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the filiereList where nom not equals to UPDATED_NOM
        defaultFiliereShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllFilieresByNomIsInShouldWork() throws Exception {
        // Initialize the database
        filiereRepository.saveAndFlush(filiere);

        // Get all the filiereList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultFiliereShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the filiereList where nom equals to UPDATED_NOM
        defaultFiliereShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllFilieresByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        filiereRepository.saveAndFlush(filiere);

        // Get all the filiereList where nom is not null
        defaultFiliereShouldBeFound("nom.specified=true");

        // Get all the filiereList where nom is null
        defaultFiliereShouldNotBeFound("nom.specified=false");
    }
                @Test
    @Transactional
    public void getAllFilieresByNomContainsSomething() throws Exception {
        // Initialize the database
        filiereRepository.saveAndFlush(filiere);

        // Get all the filiereList where nom contains DEFAULT_NOM
        defaultFiliereShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the filiereList where nom contains UPDATED_NOM
        defaultFiliereShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllFilieresByNomNotContainsSomething() throws Exception {
        // Initialize the database
        filiereRepository.saveAndFlush(filiere);

        // Get all the filiereList where nom does not contain DEFAULT_NOM
        defaultFiliereShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the filiereList where nom does not contain UPDATED_NOM
        defaultFiliereShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }


    @Test
    @Transactional
    public void getAllFilieresByModuleIsEqualToSomething() throws Exception {
        // Initialize the database
        filiereRepository.saveAndFlush(filiere);
        Module module = ModuleResourceIT.createEntity(em);
        em.persist(module);
        em.flush();
        filiere.addModule(module);
        filiereRepository.saveAndFlush(filiere);
        Long moduleId = module.getId();

        // Get all the filiereList where module equals to moduleId
        defaultFiliereShouldBeFound("moduleId.equals=" + moduleId);

        // Get all the filiereList where module equals to moduleId + 1
        defaultFiliereShouldNotBeFound("moduleId.equals=" + (moduleId + 1));
    }


    @Test
    @Transactional
    public void getAllFilieresByDepartementIsEqualToSomething() throws Exception {
        // Initialize the database
        filiereRepository.saveAndFlush(filiere);
        Departement departement = DepartementResourceIT.createEntity(em);
        em.persist(departement);
        em.flush();
        filiere.setDepartement(departement);
        filiereRepository.saveAndFlush(filiere);
        Long departementId = departement.getId();

        // Get all the filiereList where departement equals to departementId
        defaultFiliereShouldBeFound("departementId.equals=" + departementId);

        // Get all the filiereList where departement equals to departementId + 1
        defaultFiliereShouldNotBeFound("departementId.equals=" + (departementId + 1));
    }


    @Test
    @Transactional
    public void getAllFilieresByHistoriqueEnseignantFiliereIsEqualToSomething() throws Exception {
        // Get already existing entity
        HistoriqueEnseignantFiliere historiqueEnseignantFiliere = filiere.getHistoriqueEnseignantFiliere();
        filiereRepository.saveAndFlush(filiere);
        Long historiqueEnseignantFiliereId = historiqueEnseignantFiliere.getId();

        // Get all the filiereList where historiqueEnseignantFiliere equals to historiqueEnseignantFiliereId
        defaultFiliereShouldBeFound("historiqueEnseignantFiliereId.equals=" + historiqueEnseignantFiliereId);

        // Get all the filiereList where historiqueEnseignantFiliere equals to historiqueEnseignantFiliereId + 1
        defaultFiliereShouldNotBeFound("historiqueEnseignantFiliereId.equals=" + (historiqueEnseignantFiliereId + 1));
    }


    @Test
    @Transactional
    public void getAllFilieresByHistoriqueEtudiantFiliereIsEqualToSomething() throws Exception {
        // Initialize the database
        filiereRepository.saveAndFlush(filiere);
        HistoriqueEtudiantFiliere historiqueEtudiantFiliere = HistoriqueEtudiantFiliereResourceIT.createEntity(em);
        em.persist(historiqueEtudiantFiliere);
        em.flush();
        filiere.setHistoriqueEtudiantFiliere(historiqueEtudiantFiliere);
        filiereRepository.saveAndFlush(filiere);
        Long historiqueEtudiantFiliereId = historiqueEtudiantFiliere.getId();

        // Get all the filiereList where historiqueEtudiantFiliere equals to historiqueEtudiantFiliereId
        defaultFiliereShouldBeFound("historiqueEtudiantFiliereId.equals=" + historiqueEtudiantFiliereId);

        // Get all the filiereList where historiqueEtudiantFiliere equals to historiqueEtudiantFiliereId + 1
        defaultFiliereShouldNotBeFound("historiqueEtudiantFiliereId.equals=" + (historiqueEtudiantFiliereId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFiliereShouldBeFound(String filter) throws Exception {
        restFiliereMockMvc.perform(get("/api/filieres?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filiere.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));

        // Check, that the count call also returns 1
        restFiliereMockMvc.perform(get("/api/filieres/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFiliereShouldNotBeFound(String filter) throws Exception {
        restFiliereMockMvc.perform(get("/api/filieres?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFiliereMockMvc.perform(get("/api/filieres/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFiliere() throws Exception {
        // Get the filiere
        restFiliereMockMvc.perform(get("/api/filieres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFiliere() throws Exception {
        // Initialize the database
        filiereService.save(filiere);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockFiliereSearchRepository);

        int databaseSizeBeforeUpdate = filiereRepository.findAll().size();

        // Update the filiere
        Filiere updatedFiliere = filiereRepository.findById(filiere.getId()).get();
        // Disconnect from session so that the updates on updatedFiliere are not directly saved in db
        em.detach(updatedFiliere);
        updatedFiliere
            .nom(UPDATED_NOM);

        restFiliereMockMvc.perform(put("/api/filieres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFiliere)))
            .andExpect(status().isOk());

        // Validate the Filiere in the database
        List<Filiere> filiereList = filiereRepository.findAll();
        assertThat(filiereList).hasSize(databaseSizeBeforeUpdate);
        Filiere testFiliere = filiereList.get(filiereList.size() - 1);
        assertThat(testFiliere.getNom()).isEqualTo(UPDATED_NOM);

        // Validate the Filiere in Elasticsearch
        verify(mockFiliereSearchRepository, times(1)).save(testFiliere);
    }

    @Test
    @Transactional
    public void updateNonExistingFiliere() throws Exception {
        int databaseSizeBeforeUpdate = filiereRepository.findAll().size();

        // Create the Filiere

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiliereMockMvc.perform(put("/api/filieres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(filiere)))
            .andExpect(status().isBadRequest());

        // Validate the Filiere in the database
        List<Filiere> filiereList = filiereRepository.findAll();
        assertThat(filiereList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Filiere in Elasticsearch
        verify(mockFiliereSearchRepository, times(0)).save(filiere);
    }

    @Test
    @Transactional
    public void deleteFiliere() throws Exception {
        // Initialize the database
        filiereService.save(filiere);

        int databaseSizeBeforeDelete = filiereRepository.findAll().size();

        // Delete the filiere
        restFiliereMockMvc.perform(delete("/api/filieres/{id}", filiere.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Filiere> filiereList = filiereRepository.findAll();
        assertThat(filiereList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Filiere in Elasticsearch
        verify(mockFiliereSearchRepository, times(1)).deleteById(filiere.getId());
    }

    @Test
    @Transactional
    public void searchFiliere() throws Exception {
        // Initialize the database
        filiereService.save(filiere);
        when(mockFiliereSearchRepository.search(queryStringQuery("id:" + filiere.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(filiere), PageRequest.of(0, 1), 1));
        // Search the filiere
        restFiliereMockMvc.perform(get("/api/_search/filieres?query=id:" + filiere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filiere.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }
}
