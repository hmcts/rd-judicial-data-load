package uk.gov.hmcts.reform.juddata.cameltest.testsupport;

import static com.microsoft.azure.storage.blob.DeleteSnapshotsOption.INCLUDE_SNAPSHOTS;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageCredentialsAccountAndKey;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import uk.gov.hmcts.reform.data.ingestion.configuration.AzureBlobConfig;
import uk.gov.hmcts.reform.data.ingestion.configuration.StorageCredentials;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@ContextConfiguration(classes = {
        AzureBlobConfig.class, StorageCredentials.class}, initializers = ConfigFileApplicationContextInitializer.class)
public class JrdBlobSupport {

    CloudStorageAccount acc;

    CloudBlobClient cloudBlobClient;

    CloudBlobContainer cloudBlobContainer;

    CloudBlobContainer cloudBlobArchContainer;

    @Resource(name = "credsreg")
    StorageCredentialsAccountAndKey storageCredentialsAccountAndKey;

    @Value("${archival-date-format}")
    private String archivalDateFormat;

    @PostConstruct
    public void init() throws Exception {
        acc = new CloudStorageAccount(storageCredentialsAccountAndKey, true);
        cloudBlobClient = acc.createCloudBlobClient();
        cloudBlobContainer = cloudBlobClient.getContainerReference("jud-ref-data");
        cloudBlobArchContainer = cloudBlobClient.getContainerReference("jud-ref-data-archive");
    }

    public void uploadFile(String blob, InputStream sourceFile) throws Exception {
        CloudBlockBlob cloudBlockBlob = cloudBlobContainer.getBlockBlobReference(blob);
        cloudBlockBlob.upload(sourceFile, 8 * 1024 * 1024);
    }

    public void deleteBlob(String blob) throws Exception {
        CloudBlockBlob cloudBlockBlob = cloudBlobContainer.getBlockBlobReference(blob);
        cloudBlockBlob.delete(INCLUDE_SNAPSHOTS, null, null, null);

        String date = new SimpleDateFormat(archivalDateFormat).format(new Date());
        cloudBlockBlob = cloudBlobArchContainer.getBlockBlobReference(blob.concat(date));
        cloudBlockBlob.delete(INCLUDE_SNAPSHOTS, null, null, null);
    }
}

