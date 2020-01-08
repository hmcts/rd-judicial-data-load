package uk.gov.hmcts.reform.juddata.orchestrator.impl;

import com.microsoft.azure.storage.StorageException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchProviderException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.juddata.orchestrator.BlobStoreOrchestrator;
import uk.gov.hmcts.reform.juddata.service.FileDecryptionService;
import uk.gov.hmcts.reform.juddata.service.FileDeletionService;
import uk.gov.hmcts.reform.juddata.service.FilePushService;
import uk.gov.hmcts.reform.juddata.service.FileReadService;

@Service
@AllArgsConstructor
public class BlobStoreOrchestratorImpl implements BlobStoreOrchestrator {

    @Autowired
    FileReadService fileReadService;
    @Autowired
    FileDecryptionService fileDecryptionService;
    @Autowired
    FilePushService filePushService;
    @Autowired
    FileDeletionService fileDeletionService;

    @Override
    public void execute() throws IOException, InvalidKeyException, URISyntaxException, StorageException, NoSuchProviderException {

        /*
         Read file with from SFTP server
        */
        List<File> sftpFiles = fileReadService.read();


        filePushService.push(sftpFiles);

        /*
          Delete temporary created files
         */
        fileDeletionService.delete(sftpFiles);


    }
}
