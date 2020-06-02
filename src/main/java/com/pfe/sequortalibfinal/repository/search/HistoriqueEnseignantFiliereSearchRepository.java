package com.pfe.sequortalibfinal.repository.search;

import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantFiliere;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link HistoriqueEnseignantFiliere} entity.
 */
public interface HistoriqueEnseignantFiliereSearchRepository extends ElasticsearchRepository<HistoriqueEnseignantFiliere, Long> {
}
