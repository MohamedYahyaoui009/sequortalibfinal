package com.pfe.sequortalibfinal.repository.search;

import com.pfe.sequortalibfinal.domain.HistoriqueEnseignantModule;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link HistoriqueEnseignantModule} entity.
 */
public interface HistoriqueEnseignantModuleSearchRepository extends ElasticsearchRepository<HistoriqueEnseignantModule, Long> {
}
