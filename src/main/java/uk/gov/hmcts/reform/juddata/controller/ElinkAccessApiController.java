package uk.gov.hmcts.reform.juddata.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.juddata.service.ElinkAccessApiService;

import java.util.List;

@Slf4j
@RestController
public class ElinkAccessApiController {

    private static final Logger logger = LogManager.getLogger(ElinkAccessApiController.class);
    @Autowired
    ElinkAccessApiService elinkAccessApiService;
    @Value("${loggingComponentName}")
    private String loggingComponentName;

    @Scheduled(cron = "${elinks.schedule}")
    public List<ResponseEntity> getApiAsPerScheduler() {
        logger.info("{} : Inside retrieveDetails", loggingComponentName);

        List<ResponseEntity>  response = elinkAccessApiService.retrieveDetails();
        logger.info("{} : Return getApiAsPerScheduler", response);
        return response;
    }


}
