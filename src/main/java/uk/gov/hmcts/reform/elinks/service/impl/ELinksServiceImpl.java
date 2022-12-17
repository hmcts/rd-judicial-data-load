package uk.gov.hmcts.reform.elinks.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.elinks.service.ELinksService;

@Service
public class ELinksServiceImpl implements ELinksService {

    @Override
    public ResponseEntity<Object> retrieveBaseLocation(String baseLocation) {
        return null;
    }

}
