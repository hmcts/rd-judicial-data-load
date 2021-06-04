package uk.gov.hmcts.reform.juddata.camel.util;

import uk.gov.hmcts.reform.juddata.client.IdamClient;

import java.util.Set;

public interface JrdSidamTokenService {

    String getBearerToken() throws RuntimeException;

    Set<IdamClient.User> getSyncFeed() throws RuntimeException;
}
