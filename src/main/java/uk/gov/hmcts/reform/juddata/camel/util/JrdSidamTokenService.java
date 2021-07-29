package uk.gov.hmcts.reform.juddata.camel.util;

import uk.gov.hmcts.reform.juddata.client.IdamClient;
import uk.gov.hmcts.reform.juddata.exception.JudicialDataLoadException;

import java.util.Set;

public interface JrdSidamTokenService {

    String getBearerToken() throws JudicialDataLoadException;

    Set<IdamClient.User> getSyncFeed() throws JudicialDataLoadException;
}
