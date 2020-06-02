package com.pfe.sequortalibfinal.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link HistoriqueEtudiantFiliereSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class HistoriqueEtudiantFiliereSearchRepositoryMockConfiguration {

    @MockBean
    private HistoriqueEtudiantFiliereSearchRepository mockHistoriqueEtudiantFiliereSearchRepository;

}
