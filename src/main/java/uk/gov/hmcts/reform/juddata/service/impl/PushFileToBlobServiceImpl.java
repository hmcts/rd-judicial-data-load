package uk.gov.hmcts.reform.juddata.service.impl;

import com.microsoft.azure.storage.blob.BlockBlobURL;
import com.microsoft.azure.storage.blob.ContainerURL;
import com.microsoft.azure.storage.blob.PipelineOptions;
import com.microsoft.azure.storage.blob.ServiceURL;
import com.microsoft.azure.storage.blob.SharedKeyCredentials;
import com.microsoft.azure.storage.blob.StorageURL;
import com.microsoft.azure.storage.blob.TransferManager;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.security.InvalidKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.juddata.configuration.AzureBlobConfig;
import uk.gov.hmcts.reform.juddata.service.PushFileService;


@Slf4j
@Service
public class PushFileToBlobServiceImpl implements PushFileService {

    private static final String SOURCE_FILE = "jrd1.csv";

    @Autowired
    AzureBlobConfig azureBlobConfig;

    @Override
    public void push(File decryptedFile) throws IOException, InvalidKeyException {

        final File sourceFile = new File(this.getClass().getClassLoader().getResource(SOURCE_FILE).getFile());
        ServiceURL serviceUrl = createServiceUrl();
        ContainerURL containerUrl = serviceUrl.createContainerURL(azureBlobConfig.getContainerName());
        final BlockBlobURL blockBlobUrl = containerUrl.createBlockBlobURL("/");
        uploadFile(blockBlobUrl, sourceFile);

    }

    public void uploadFile(BlockBlobURL blob, File sourceFile) throws IOException {
        log.info("Start uploading file ... " + sourceFile.getName());
      // final AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(sourceFile.toPath());

        TransferManager.uploadFileToBlockBlob(FileChannel.open(sourceFile.toPath()), blob, 8 * 1024 * 1024, null)
                .ignoreElement()
                .doOnComplete(() -> log.info("File " + sourceFile.getName() + " is uploaded"))
                .doOnError(error -> log.error("Failed to upload file " + sourceFile.getName() + " with error ", sourceFile, error.getMessage()))
                .blockingAwait();
    }


    public ServiceURL createServiceUrl() throws InvalidKeyException,
            MalformedURLException {
        log.debug("Creating ServiceURL bean...");
        final SharedKeyCredentials credentials = new SharedKeyCredentials(azureBlobConfig.getAccountName(),
                azureBlobConfig.getAccountKey());
        final URL blobUrl = getUrl();
        final ServiceURL serviceUrl = new ServiceURL(blobUrl, StorageURL.createPipeline(credentials, new PipelineOptions()));
        return serviceUrl;
    }

    public URL getUrl() throws MalformedURLException {
        if (azureBlobConfig.isHttpsEnabledUrl()) {
            return new URL(String.format(azureBlobConfig.getBlobHttpsUrl(), azureBlobConfig.getContainerName()));
        }
        return new URL(String.format(azureBlobConfig.getBlobUrl(), azureBlobConfig.getContainerName()));
    }
}
