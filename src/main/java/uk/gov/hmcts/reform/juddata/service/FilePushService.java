package uk.gov.hmcts.reform.juddata.service;

import com.microsoft.azure.storage.StorageException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;

public interface FilePushService {
    void push(List<File> decryptedFile) throws IOException, InvalidKeyException, StorageException, URISyntaxException;
}
