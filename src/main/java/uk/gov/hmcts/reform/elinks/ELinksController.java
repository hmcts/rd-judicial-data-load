package uk.gov.hmcts.reform.elinks;


import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.elinks.response.IdamResponse;
import uk.gov.hmcts.reform.elinks.service.ELinksService;
import uk.gov.hmcts.reform.elinks.service.IdamElasticSearchService;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.elinks.util.RefDataConstants.BAD_REQUEST;
import static uk.gov.hmcts.reform.elinks.util.RefDataConstants.BASE_LOCATION_DATA_LOAD_SUCCESS;
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
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("all")
public class ELinksController {

    @Autowired
    IdamElasticSearchService idamElasticSearchService;

    @Autowired
    ELinksService eLinksService;

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
    @GetMapping (path = "/reference_data/location",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loadLocation(){


        return eLinksService.retrieveLocation();
    }


    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Get list of Base locations and populate base location type.",
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
    @GetMapping(  path = "/reference_data/base_location",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loadBaseLocationType() {

    eLinksService.retrieveBaseLocation();
    return ResponseEntity
                .status(HttpStatus.OK)
                .body(BASE_LOCATION_DATA_LOAD_SUCCESS);
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

}
