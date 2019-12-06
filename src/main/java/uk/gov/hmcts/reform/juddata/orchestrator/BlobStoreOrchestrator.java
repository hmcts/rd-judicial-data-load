package uk.gov.hmcts.reform.juddata.orchestrator;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchProviderException;

public interface BlobStoreOrchestrator {

    void execute() throws IOException, InvalidKeyException, NoSuchProviderException;

}
