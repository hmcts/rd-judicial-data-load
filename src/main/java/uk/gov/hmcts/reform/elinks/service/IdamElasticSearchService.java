package uk.gov.hmcts.reform.elinks.service;

import uk.gov.hmcts.reform.elinks.response.IdamResponse;
import uk.gov.hmcts.reform.juddata.exception.JudicialDataLoadException;

import java.util.Set;

public interface IdamElasticSearchService {

    String getIdamBearerToken() throws JudicialDataLoadException;

    Set<IdamResponse> getIdamElasticSearchSyncFeed() throws JudicialDataLoadException;
}
