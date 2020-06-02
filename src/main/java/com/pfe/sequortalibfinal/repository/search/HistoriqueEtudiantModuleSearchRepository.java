package com.pfe.sequortalibfinal.repository.search;

import com.pfe.sequortalibfinal.domain.HistoriqueEtudiantModule;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link HistoriqueEtudiantModule} entity.
 */
public interface HistoriqueEtudiantModuleSearchRepository extends ElasticsearchRepository<HistoriqueEtudiantModule, Long> {
}
