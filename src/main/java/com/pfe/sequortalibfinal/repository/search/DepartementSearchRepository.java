package com.pfe.sequortalibfinal.repository.search;

import com.pfe.sequortalibfinal.domain.Departement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Departement} entity.
 */
public interface DepartementSearchRepository extends ElasticsearchRepository<Departement, Long> {
}
