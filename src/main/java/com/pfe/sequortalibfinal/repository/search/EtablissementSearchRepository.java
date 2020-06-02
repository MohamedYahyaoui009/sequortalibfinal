package com.pfe.sequortalibfinal.repository.search;

import com.pfe.sequortalibfinal.domain.Etablissement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Etablissement} entity.
 */
public interface EtablissementSearchRepository extends ElasticsearchRepository<Etablissement, Long> {
}
