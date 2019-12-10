package uk.gov.hmcts.reform.juddata.configuration;

import com.microsoft.azure.storage.StorageCredentialsAccountAndKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageCredentials {

    @Autowired
    AzureBlobConfig azureBlobConfig;

    @Bean(name = "credsreg")
    public com.microsoft.azure.storage.StorageCredentials credentials()
    {

        return new StorageCredentialsAccountAndKey(azureBlobConfig.getAccountName(), azureBlobConfig.getAccountKey());

    }
}
