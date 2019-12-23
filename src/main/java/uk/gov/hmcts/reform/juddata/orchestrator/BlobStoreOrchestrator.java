package uk.gov.hmcts.reform.juddata.orchestrator;

import com.microsoft.azure.storage.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchProviderException;

public interface BlobStoreOrchestrator {

    void execute() throws IOException, InvalidKeyException, NoSuchProviderException, URISyntaxException, StorageException;

}
