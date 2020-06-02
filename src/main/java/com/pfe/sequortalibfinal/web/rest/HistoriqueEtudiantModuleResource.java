package com.pfe.sequortalibfinal.web.rest;

import com.pfe.sequortalibfinal.domain.HistoriqueEtudiantModule;
import com.pfe.sequortalibfinal.service.HistoriqueEtudiantModuleService;
import com.pfe.sequortalibfinal.web.rest.errors.BadRequestAlertException;
import com.pfe.sequortalibfinal.service.dto.HistoriqueEtudiantModuleCriteria;
import com.pfe.sequortalibfinal.service.HistoriqueEtudiantModuleQueryService;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.pfe.sequortalibfinal.domain.HistoriqueEtudiantModule}.
 */
@RestController
@RequestMapping("/api")
public class HistoriqueEtudiantModuleResource {

    private final Logger log = LoggerFactory.getLogger(HistoriqueEtudiantModuleResource.class);

    private static final String ENTITY_NAME = "historiqueEtudiantModule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoriqueEtudiantModuleService historiqueEtudiantModuleService;

    private final HistoriqueEtudiantModuleQueryService historiqueEtudiantModuleQueryService;

    public HistoriqueEtudiantModuleResource(HistoriqueEtudiantModuleService historiqueEtudiantModuleService, HistoriqueEtudiantModuleQueryService historiqueEtudiantModuleQueryService) {
        this.historiqueEtudiantModuleService = historiqueEtudiantModuleService;
        this.historiqueEtudiantModuleQueryService = historiqueEtudiantModuleQueryService;
    }

    /**
     * {@code POST  /historique-etudiant-modules} : Create a new historiqueEtudiantModule.
     *
     * @param historiqueEtudiantModule the historiqueEtudiantModule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historiqueEtudiantModule, or with status {@code 400 (Bad Request)} if the historiqueEtudiantModule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/historique-etudiant-modules")
    public ResponseEntity<HistoriqueEtudiantModule> createHistoriqueEtudiantModule(@RequestBody HistoriqueEtudiantModule historiqueEtudiantModule) throws URISyntaxException {
        log.debug("REST request to save HistoriqueEtudiantModule : {}", historiqueEtudiantModule);
        if (historiqueEtudiantModule.getId() != null) {
            throw new BadRequestAlertException("A new historiqueEtudiantModule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistoriqueEtudiantModule result = historiqueEtudiantModuleService.save(historiqueEtudiantModule);
        return ResponseEntity.created(new URI("/api/historique-etudiant-modules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /historique-etudiant-modules} : Updates an existing historiqueEtudiantModule.
     *
     * @param historiqueEtudiantModule the historiqueEtudiantModule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiqueEtudiantModule,
     * or with status {@code 400 (Bad Request)} if the historiqueEtudiantModule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historiqueEtudiantModule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/historique-etudiant-modules")
    public ResponseEntity<HistoriqueEtudiantModule> updateHistoriqueEtudiantModule(@RequestBody HistoriqueEtudiantModule historiqueEtudiantModule) throws URISyntaxException {
        log.debug("REST request to update HistoriqueEtudiantModule : {}", historiqueEtudiantModule);
        if (historiqueEtudiantModule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HistoriqueEtudiantModule result = historiqueEtudiantModuleService.save(historiqueEtudiantModule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiqueEtudiantModule.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /historique-etudiant-modules} : get all the historiqueEtudiantModules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiqueEtudiantModules in body.
     */
    @GetMapping("/historique-etudiant-modules")
    public ResponseEntity<List<HistoriqueEtudiantModule>> getAllHistoriqueEtudiantModules(HistoriqueEtudiantModuleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get HistoriqueEtudiantModules by criteria: {}", criteria);
        Page<HistoriqueEtudiantModule> page = historiqueEtudiantModuleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /historique-etudiant-modules/count} : count all the historiqueEtudiantModules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/historique-etudiant-modules/count")
    public ResponseEntity<Long> countHistoriqueEtudiantModules(HistoriqueEtudiantModuleCriteria criteria) {
        log.debug("REST request to count HistoriqueEtudiantModules by criteria: {}", criteria);
        return ResponseEntity.ok().body(historiqueEtudiantModuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /historique-etudiant-modules/:id} : get the "id" historiqueEtudiantModule.
     *
     * @param id the id of the historiqueEtudiantModule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historiqueEtudiantModule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/historique-etudiant-modules/{id}")
    public ResponseEntity<HistoriqueEtudiantModule> getHistoriqueEtudiantModule(@PathVariable Long id) {
        log.debug("REST request to get HistoriqueEtudiantModule : {}", id);
        Optional<HistoriqueEtudiantModule> historiqueEtudiantModule = historiqueEtudiantModuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historiqueEtudiantModule);
    }

    /**
     * {@code DELETE  /historique-etudiant-modules/:id} : delete the "id" historiqueEtudiantModule.
     *
     * @param id the id of the historiqueEtudiantModule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/historique-etudiant-modules/{id}")
    public ResponseEntity<Void> deleteHistoriqueEtudiantModule(@PathVariable Long id) {
        log.debug("REST request to delete HistoriqueEtudiantModule : {}", id);
        historiqueEtudiantModuleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/historique-etudiant-modules?query=:query} : search for the historiqueEtudiantModule corresponding
     * to the query.
     *
     * @param query the query of the historiqueEtudiantModule search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/historique-etudiant-modules")
    public ResponseEntity<List<HistoriqueEtudiantModule>> searchHistoriqueEtudiantModules(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of HistoriqueEtudiantModules for query {}", query);
        Page<HistoriqueEtudiantModule> page = historiqueEtudiantModuleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
