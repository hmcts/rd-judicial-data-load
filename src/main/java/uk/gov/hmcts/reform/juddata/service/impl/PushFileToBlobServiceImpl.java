package uk.gov.hmcts.reform.juddata.service.impl;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageCredentials;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.juddata.configuration.AzureBlobConfig;
import uk.gov.hmcts.reform.juddata.service.FilePushService;


@Slf4j
@Service
public class PushFileToBlobServiceImpl implements FilePushService {

    @Autowired
    private AzureBlobConfig azureBlobConfig;

    @Autowired
    private StorageCredentials credsreg;

    /**
     * Initializes blob container with blob details mentioned in application.yml config file.
     *
     * @return CloudBlobContainer
     * @throws URISyntaxException URISyntaxException
     * @throws StorageException StorageException
     */
    public CloudBlobContainer initializeBlobContainer() throws URISyntaxException, StorageException {
        URI blobUri = URI.create("http://rdaat.privatelink.blob.core.windows.net");
        CloudStorageAccount cloudStorageAccount = new CloudStorageAccount(credsreg, blobUri, blobUri, blobUri);
        CloudBlobClient cloudBlobClient = cloudStorageAccount.createCloudBlobClient();
        return cloudBlobClient.getContainerReference(azureBlobConfig.getContainerName());
    }

    /**
     * Uploads files to desired blob store.
     *
     * @param files List of files to be uploaded into blob store
     * @throws IOException IOException
     * @throws StorageException StorageException
     * @throws URISyntaxException URISyntaxException
     */
    @Override
    public void push(List<File> files) throws IOException, StorageException, URISyntaxException {
        CloudBlobContainer cloudBlobContainer = initializeBlobContainer();
        for (File file : files) {
            CloudBlockBlob cloudBlockBlob = cloudBlobContainer.getBlockBlobReference(file.getName());
            cloudBlockBlob.upload(new FileInputStream(file), file.length());
            log.info("File uploaded successfully : " + file.getName() + " timestamp: " + LocalDateTime.now());
        }
    }

}
