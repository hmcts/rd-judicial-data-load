package uk.gov.hmcts.reform.elinks.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import uk.gov.hmcts.reform.elinks.exception.JudicialDataLoadException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;


@SuppressWarnings({"unchecked","HideUtilityClassConstructor"})
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonFeignResponseUtility {
    private static final ObjectMapper json = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static ResponseEntity<Object> toResponseEntity(Response response, TypeReference<?> reference) {
        Optional<Object> payload = Optional.empty();

        try {
            payload = Optional.of(json.readValue(response.body().asReader(Charset.defaultCharset()), reference));

        } catch (IOException ex) {
            log.error("error while reading the body", ex);
            throw new JudicialDataLoadException("Response parsing failed");
        }

        return new ResponseEntity<>(
            payload.orElse(null),
            convertHeaders(response.headers()),
            HttpStatus.valueOf(response.status()));
    }

    public static MultiValueMap<String, String> convertHeaders(Map<String, Collection<String>> responseHeaders) {
        HttpHeaders responseEntityHeaders = new HttpHeaders();
        responseHeaders.entrySet().stream().forEach(e ->
            responseEntityHeaders.put(e.getKey(), new ArrayList<>(e.getValue())));
        return responseEntityHeaders;
    }
}

