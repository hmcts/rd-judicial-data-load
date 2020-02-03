package uk.gov.hmcts.reform.juddata.service;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchProviderException;
import java.util.List;

public interface FileDecryptionService {

    List<File> decrypt(List<File> file) throws IOException, NoSuchProviderException;
}
