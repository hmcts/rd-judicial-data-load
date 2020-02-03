package uk.gov.hmcts.reform.juddata.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface FileReadService {
    List<File> read() throws IOException, URISyntaxException;
}
