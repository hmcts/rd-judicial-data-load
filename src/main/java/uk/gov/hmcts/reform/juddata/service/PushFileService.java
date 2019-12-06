package uk.gov.hmcts.reform.juddata.service;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;

public interface PushFileService {
    void push(File decryptedFile) throws IOException, InvalidKeyException;
}
