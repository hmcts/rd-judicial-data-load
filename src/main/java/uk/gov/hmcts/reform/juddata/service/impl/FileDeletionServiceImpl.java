package uk.gov.hmcts.reform.juddata.service.impl;

import java.io.File;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.juddata.service.FileDeletionService;

@Service
@Slf4j
public class FileDeletionServiceImpl implements FileDeletionService {

    @Override
    public void delete(List<File> files) {
        files.forEach(file -> {
            file.delete();
        });
    }
}
