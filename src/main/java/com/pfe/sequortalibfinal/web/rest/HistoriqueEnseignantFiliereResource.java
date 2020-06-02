package com.pfe.sequortalibfinal.web.rest;

import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantFiliere;
import com.pfe.sequortalibfinal.service.HistoriqueEnseignantFiliereService;
import com.pfe.sequortalibfinal.web.rest.errors.BadRequestAlertException;
import com.pfe.sequortalibfinal.service.dto.HistoriqueEnseignantFiliereCriteria;
import com.pfe.sequortalibfinal.service.HistoriqueEnseignantFiliereQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.pfe.sequortalibfinal.domain.HistoriqueEnseignantFiliere}.
 */
@RestController
@RequestMapping("/api")
public class HistoriqueEnseignantFiliereResource {

    private final Logger log = LoggerFactory.getLogger(HistoriqueEnseignantFiliereResource.class);

    private static final String ENTITY_NAME = "historiqueEnseignantFiliere";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoriqueEnseignantFiliereService historiqueEnseignantFiliereService;

    private final HistoriqueEnseignantFiliereQueryService historiqueEnseignantFiliereQueryService;

    public HistoriqueEnseignantFiliereResource(HistoriqueEnseignantFiliereService historiqueEnseignantFiliereService, HistoriqueEnseignantFiliereQueryService historiqueEnseignantFiliereQueryService) {
        this.historiqueEnseignantFiliereService = historiqueEnseignantFiliereService;
        this.historiqueEnseignantFiliereQueryService = historiqueEnseignantFiliereQueryService;
    }

    /**
     * {@code POST  /historique-enseignant-filieres} : Create a new historiqueEnseignantFiliere.
     *
     * @param historiqueEnseignantFiliere the historiqueEnseignantFiliere to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historiqueEnseignantFiliere, or with status {@code 400 (Bad Request)} if the historiqueEnseignantFiliere has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/historique-enseignant-filieres")
    public ResponseEntity<HistoriqueEnseignantFiliere> createHistoriqueEnseignantFiliere(@Valid @RequestBody HistoriqueEnseignantFiliere historiqueEnseignantFiliere) throws URISyntaxException {
        log.debug("REST request to save HistoriqueEnseignantFiliere : {}", historiqueEnseignantFiliere);
        if (historiqueEnseignantFiliere.getId() != null) {
            throw new BadRequestAlertException("A new historiqueEnseignantFiliere cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistoriqueEnseignantFiliere result = historiqueEnseignantFiliereService.save(historiqueEnseignantFiliere);
        return ResponseEntity.created(new URI("/api/historique-enseignant-filieres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /historique-enseignant-filieres} : Updates an existing historiqueEnseignantFiliere.
     *
     * @param historiqueEnseignantFiliere the historiqueEnseignantFiliere to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiqueEnseignantFiliere,
     * or with status {@code 400 (Bad Request)} if the historiqueEnseignantFiliere is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historiqueEnseignantFiliere couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/historique-enseignant-filieres")
    public ResponseEntity<HistoriqueEnseignantFiliere> updateHistoriqueEnseignantFiliere(@Valid @RequestBody HistoriqueEnseignantFiliere historiqueEnseignantFiliere) throws URISyntaxException {
        log.debug("REST request to update HistoriqueEnseignantFiliere : {}", historiqueEnseignantFiliere);
        if (historiqueEnseignantFiliere.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HistoriqueEnseignantFiliere result = historiqueEnseignantFiliereService.save(historiqueEnseignantFiliere);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiqueEnseignantFiliere.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /historique-enseignant-filieres} : get all the historiqueEnseignantFilieres.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiqueEnseignantFilieres in body.
     */
    @GetMapping("/historique-enseignant-filieres")
    public ResponseEntity<List<HistoriqueEnseignantFiliere>> getAllHistoriqueEnseignantFilieres(HistoriqueEnseignantFiliereCriteria criteria, Pageable pageable) {
        log.debug("REST request to get HistoriqueEnseignantFilieres by criteria: {}", criteria);
        Page<HistoriqueEnseignantFiliere> page = historiqueEnseignantFiliereQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /historique-enseignant-filieres/count} : count all the historiqueEnseignantFilieres.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/historique-enseignant-filieres/count")
    public ResponseEntity<Long> countHistoriqueEnseignantFilieres(HistoriqueEnseignantFiliereCriteria criteria) {
        log.debug("REST request to count HistoriqueEnseignantFilieres by criteria: {}", criteria);
        return ResponseEntity.ok().body(historiqueEnseignantFiliereQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /historique-enseignant-filieres/:id} : get the "id" historiqueEnseignantFiliere.
     *
     * @param id the id of the historiqueEnseignantFiliere to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historiqueEnseignantFiliere, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/historique-enseignant-filieres/{id}")
    public ResponseEntity<HistoriqueEnseignantFiliere> getHistoriqueEnseignantFiliere(@PathVariable Long id) {
        log.debug("REST request to get HistoriqueEnseignantFiliere : {}", id);
        Optional<HistoriqueEnseignantFiliere> historiqueEnseignantFiliere = historiqueEnseignantFiliereService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historiqueEnseignantFiliere);
    }

    /**
     * {@code DELETE  /historique-enseignant-filieres/:id} : delete the "id" historiqueEnseignantFiliere.
     *
     * @param id the id of the historiqueEnseignantFiliere to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/historique-enseignant-filieres/{id}")
    public ResponseEntity<Void> deleteHistoriqueEnseignantFiliere(@PathVariable Long id) {
        log.debug("REST request to delete HistoriqueEnseignantFiliere : {}", id);
        historiqueEnseignantFiliereService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/historique-enseignant-filieres?query=:query} : search for the historiqueEnseignantFiliere corresponding
     * to the query.
     *
     * @param query the query of the historiqueEnseignantFiliere search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/historique-enseignant-filieres")
    public ResponseEntity<List<HistoriqueEnseignantFiliere>> searchHistoriqueEnseignantFilieres(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of HistoriqueEnseignantFilieres for query {}", query);
        Page<HistoriqueEnseignantFiliere> page = historiqueEnseignantFiliereService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
