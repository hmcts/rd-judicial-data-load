package uk.gov.hmcts.reform.juddata.service;

import java.io.File;
import java.util.List;

public interface FileDeletionService {
    void delete(List<File> file);
}
