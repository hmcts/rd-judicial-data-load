package uk.gov.hmcts.reform.elinks.service;

import org.springframework.http.ResponseEntity;

public interface ELinksService {

    ResponseEntity<Object> retrieveBaseLocation(String baseLocation);

}
