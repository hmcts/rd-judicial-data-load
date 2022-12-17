package uk.gov.hmcts.reform.juddata.elinks.controller;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.gov.hmcts.reform.elinks.feign.ElinksFeignClient;
import uk.gov.hmcts.reform.elinks.feign.IdamFeignClient;
import uk.gov.hmcts.reform.elinks.service.IdamElasticSearchService;
import uk.gov.hmcts.reform.elinks.service.impl.PublishSidamIdServiceImpl;
import uk.gov.hmcts.reform.elinks.servicebus.ElinkTopicPublisher;

@TestConfiguration
public class ELinksControllerTestConfig {

    @MockBean
    ElinksFeignClient elinksFeignClient;
    @MockBean
    IdamElasticSearchService idamElasticSearchService;
    @MockBean
    IdamFeignClient idamFeignClientOne;
    @MockBean
    PublishSidamIdServiceImpl service;
    @MockBean
    ElinkTopicPublisher topicPublisher;

}
