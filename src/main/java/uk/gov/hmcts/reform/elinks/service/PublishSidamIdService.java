package uk.gov.hmcts.reform.elinks.service;

import uk.gov.hmcts.reform.elinks.response.SchedulerJobStatusResponse;

public interface PublishSidamIdService {

    SchedulerJobStatusResponse publishSidamIdToAsb()throws Exception;

}
