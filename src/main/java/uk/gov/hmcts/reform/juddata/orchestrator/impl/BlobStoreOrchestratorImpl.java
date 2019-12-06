package uk.gov.hmcts.reform.juddata.orchestrator.impl;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchProviderException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.juddata.orchestrator.BlobStoreOrchestrator;
import uk.gov.hmcts.reform.juddata.service.FileDecryptionService;
import uk.gov.hmcts.reform.juddata.service.PushFileService;

@Service
@AllArgsConstructor
public class BlobStoreOrchestratorImpl implements BlobStoreOrchestrator {

    @Autowired
    FileDecryptionService fileDecryptionService;
    @Autowired
    PushFileService pushFileService;

    @Override
    public void execute() throws IOException, InvalidKeyException, NoSuchProviderException {

        /*
         Decrypt file with GPG private key
        */
        File decryptedFile = fileDecryptionService.decrypt();

        /*
          Push file to desired blob store
          By this time the csv file has been decrypted
         */
        pushFileService.push(decryptedFile);
    }
}
