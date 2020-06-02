package com.pfe.sequortalibfinal.repository.search;

import com.pfe.sequortalibfinal.domain.Enseignant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Enseignant} entity.
 */
public interface EnseignantSearchRepository extends ElasticsearchRepository<Enseignant, Long> {
}
