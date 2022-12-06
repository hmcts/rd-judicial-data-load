package uk.gov.hmcts.reform.juddata.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
public class WelcomeController {

    @Value("${loggingComponentName}")
    private String loggingComponentName;
    private static final String INSTANCE_ID = UUID.randomUUID().toString();
    private static final String MESSAGE = "Welcome to the Elink Ref Data API";

    @ApiOperation("Welcome message for the Elink Ref Data API")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Welcome message",
                    response = String.class
            )
    })
    @GetMapping(
            path = "/say_hello",
            produces = APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> welcome() {

        log.info("{}:: Welcome message '{}' from running instance: {}", loggingComponentName, MESSAGE, INSTANCE_ID);
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .body("{\"message\": \"" + MESSAGE + "\"}");
    }
}
