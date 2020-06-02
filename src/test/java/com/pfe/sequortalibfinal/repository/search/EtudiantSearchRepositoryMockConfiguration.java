package com.pfe.sequortalibfinal.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link EtudiantSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class EtudiantSearchRepositoryMockConfiguration {

    @MockBean
    private EtudiantSearchRepository mockEtudiantSearchRepository;

}
