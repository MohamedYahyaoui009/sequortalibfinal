package com.pfe.sequortalibfinal.web.rest;

import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantModule;
import com.pfe.sequortalibfinal.service.HistoriqueEnseignantModuleService;
import com.pfe.sequortalibfinal.web.rest.errors.BadRequestAlertException;
import com.pfe.sequortalibfinal.service.dto.HistoriqueEnseignantModuleCriteria;
import com.pfe.sequortalibfinal.service.HistoriqueEnseignantModuleQueryService;

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
 * REST controller for managing {@link com.pfe.sequortalibfinal.domain.HistoriqueEnseignantModule}.
 */
@RestController
@RequestMapping("/api")
public class HistoriqueEnseignantModuleResource {

    private final Logger log = LoggerFactory.getLogger(HistoriqueEnseignantModuleResource.class);

    private static final String ENTITY_NAME = "historiqueEnseignantModule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoriqueEnseignantModuleService historiqueEnseignantModuleService;

    private final HistoriqueEnseignantModuleQueryService historiqueEnseignantModuleQueryService;

    public HistoriqueEnseignantModuleResource(HistoriqueEnseignantModuleService historiqueEnseignantModuleService, HistoriqueEnseignantModuleQueryService historiqueEnseignantModuleQueryService) {
        this.historiqueEnseignantModuleService = historiqueEnseignantModuleService;
        this.historiqueEnseignantModuleQueryService = historiqueEnseignantModuleQueryService;
    }

    /**
     * {@code POST  /historique-enseignant-modules} : Create a new historiqueEnseignantModule.
     *
     * @param historiqueEnseignantModule the historiqueEnseignantModule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historiqueEnseignantModule, or with status {@code 400 (Bad Request)} if the historiqueEnseignantModule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/historique-enseignant-modules")
    public ResponseEntity<HistoriqueEnseignantModule> createHistoriqueEnseignantModule(@Valid @RequestBody HistoriqueEnseignantModule historiqueEnseignantModule) throws URISyntaxException {
        log.debug("REST request to save HistoriqueEnseignantModule : {}", historiqueEnseignantModule);
        if (historiqueEnseignantModule.getId() != null) {
            throw new BadRequestAlertException("A new historiqueEnseignantModule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistoriqueEnseignantModule result = historiqueEnseignantModuleService.save(historiqueEnseignantModule);
        return ResponseEntity.created(new URI("/api/historique-enseignant-modules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /historique-enseignant-modules} : Updates an existing historiqueEnseignantModule.
     *
     * @param historiqueEnseignantModule the historiqueEnseignantModule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiqueEnseignantModule,
     * or with status {@code 400 (Bad Request)} if the historiqueEnseignantModule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historiqueEnseignantModule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/historique-enseignant-modules")
    public ResponseEntity<HistoriqueEnseignantModule> updateHistoriqueEnseignantModule(@Valid @RequestBody HistoriqueEnseignantModule historiqueEnseignantModule) throws URISyntaxException {
        log.debug("REST request to update HistoriqueEnseignantModule : {}", historiqueEnseignantModule);
        if (historiqueEnseignantModule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HistoriqueEnseignantModule result = historiqueEnseignantModuleService.save(historiqueEnseignantModule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiqueEnseignantModule.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /historique-enseignant-modules} : get all the historiqueEnseignantModules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiqueEnseignantModules in body.
     */
    @GetMapping("/historique-enseignant-modules")
    public ResponseEntity<List<HistoriqueEnseignantModule>> getAllHistoriqueEnseignantModules(HistoriqueEnseignantModuleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get HistoriqueEnseignantModules by criteria: {}", criteria);
        Page<HistoriqueEnseignantModule> page = historiqueEnseignantModuleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /historique-enseignant-modules/count} : count all the historiqueEnseignantModules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/historique-enseignant-modules/count")
    public ResponseEntity<Long> countHistoriqueEnseignantModules(HistoriqueEnseignantModuleCriteria criteria) {
        log.debug("REST request to count HistoriqueEnseignantModules by criteria: {}", criteria);
        return ResponseEntity.ok().body(historiqueEnseignantModuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /historique-enseignant-modules/:id} : get the "id" historiqueEnseignantModule.
     *
     * @param id the id of the historiqueEnseignantModule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historiqueEnseignantModule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/historique-enseignant-modules/{id}")
    public ResponseEntity<HistoriqueEnseignantModule> getHistoriqueEnseignantModule(@PathVariable Long id) {
        log.debug("REST request to get HistoriqueEnseignantModule : {}", id);
        Optional<HistoriqueEnseignantModule> historiqueEnseignantModule = historiqueEnseignantModuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historiqueEnseignantModule);
    }

    /**
     * {@code DELETE  /historique-enseignant-modules/:id} : delete the "id" historiqueEnseignantModule.
     *
     * @param id the id of the historiqueEnseignantModule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/historique-enseignant-modules/{id}")
    public ResponseEntity<Void> deleteHistoriqueEnseignantModule(@PathVariable Long id) {
        log.debug("REST request to delete HistoriqueEnseignantModule : {}", id);
        historiqueEnseignantModuleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/historique-enseignant-modules?query=:query} : search for the historiqueEnseignantModule corresponding
     * to the query.
     *
     * @param query the query of the historiqueEnseignantModule search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/historique-enseignant-modules")
    public ResponseEntity<List<HistoriqueEnseignantModule>> searchHistoriqueEnseignantModules(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of HistoriqueEnseignantModules for query {}", query);
        Page<HistoriqueEnseignantModule> page = historiqueEnseignantModuleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
