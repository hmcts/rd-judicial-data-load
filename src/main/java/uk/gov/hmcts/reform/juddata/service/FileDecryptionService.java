package uk.gov.hmcts.reform.juddata.service;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchProviderException;

public interface FileDecryptionService {

    File decrypt() throws IOException, NoSuchProviderException;
}
