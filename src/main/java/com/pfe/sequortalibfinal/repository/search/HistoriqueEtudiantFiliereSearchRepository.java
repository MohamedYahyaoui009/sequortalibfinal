package com.pfe.sequortalibfinal.repository.search;

import com.pfe.sequortalibfinal.domain.HistoriqueEtudiantFiliere;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link HistoriqueEtudiantFiliere} entity.
 */
public interface HistoriqueEtudiantFiliereSearchRepository extends ElasticsearchRepository<HistoriqueEtudiantFiliere, Long> {
}
