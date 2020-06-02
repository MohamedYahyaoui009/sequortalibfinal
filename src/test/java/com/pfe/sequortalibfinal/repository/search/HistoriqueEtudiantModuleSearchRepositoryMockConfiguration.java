package com.pfe.sequortalibfinal.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link HistoriqueEtudiantModuleSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class HistoriqueEtudiantModuleSearchRepositoryMockConfiguration {

    @MockBean
    private HistoriqueEtudiantModuleSearchRepository mockHistoriqueEtudiantModuleSearchRepository;

}
