package uk.gov.hmcts.reform.elinks;


import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.elinks.response.IdamResponse;
import uk.gov.hmcts.reform.elinks.response.SchedulerJobStatusResponse;
import uk.gov.hmcts.reform.elinks.service.IdamElasticSearchService;
import uk.gov.hmcts.reform.elinks.service.PublishSidamIdService;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.elinks.util.RefDataConstants.BAD_REQUEST;
import static uk.gov.hmcts.reform.elinks.util.RefDataConstants.FORBIDDEN_ERROR;
import static uk.gov.hmcts.reform.elinks.util.RefDataConstants.INTERNAL_SERVER_ERROR;
import static uk.gov.hmcts.reform.elinks.util.RefDataConstants.NO_DATA_FOUND;
import static uk.gov.hmcts.reform.elinks.util.RefDataConstants.TOO_MANY_REQUESTS;
import static uk.gov.hmcts.reform.elinks.util.RefDataConstants.UNAUTHORIZED_ERROR;

@RestController
@RequestMapping(
        path = "/refdata/jinternal/elink"
)
@Slf4j
//@NoArgsConstructor
@AllArgsConstructor
public class ELinksController {

    @Autowired
    PublishSidamIdService publishSidamIdService;

    @Autowired
    IdamElasticSearchService idamElasticSearchService;

    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Get list of location and populate region type.",
                    response = String.class
            ),
            @ApiResponse(
                    code = 400,
                    message = BAD_REQUEST
            ),
            @ApiResponse(
                    code = 401,
                    message = UNAUTHORIZED_ERROR
            ),
            @ApiResponse(
                    code = 403,
                    message = FORBIDDEN_ERROR
            ),
            @ApiResponse(
                    code = 404,
                    message = NO_DATA_FOUND
            ),
            @ApiResponse(
                    code = 429,
                    message = TOO_MANY_REQUESTS
            ),
            @ApiResponse(
                    code = 500,
                    message = INTERNAL_SERVER_ERROR
            )
    })
    @GetMapping (path = "/location",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loadRegionType() {


        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Test API");
    }

    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Get list of idam users.",
                    response = IdamResponse.class
            ),
            @ApiResponse(
                    code = 400,
                    message = BAD_REQUEST
            ),
            @ApiResponse(
                    code = 401,
                    message = UNAUTHORIZED_ERROR
            ),
            @ApiResponse(
                    code = 403,
                    message = FORBIDDEN_ERROR
            ),
            @ApiResponse(
                    code = 500,
                    message = INTERNAL_SERVER_ERROR
            )
    })
    @GetMapping (path = "/idam/elastic/search",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> idamElasticSearch() {

        Set<IdamResponse> response =  idamElasticSearchService.getIdamElasticSearchSyncFeed();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Publish all SIDAM id's to ASB",
                    response = String.class
            ),
            @ApiResponse(
                    code = 400,
                    message = BAD_REQUEST
            ),
            @ApiResponse(
                    code = 401,
                    message = UNAUTHORIZED_ERROR
            ),
            @ApiResponse(
                    code = 403,
                    message = FORBIDDEN_ERROR
            ),
            @ApiResponse(
                    code = 404,
                    message = NO_DATA_FOUND
            ),
            @ApiResponse(
                    code = 429,
                    message = TOO_MANY_REQUESTS
            ),
            @ApiResponse(
                    code = 500,
                    message = INTERNAL_SERVER_ERROR
            )
    })
    @GetMapping(path = "/sidam/asb/publish",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> publishSidamIdToAsb() {
        SchedulerJobStatusResponse response = publishSidamIdService.publishSidamIdToAsb();

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
