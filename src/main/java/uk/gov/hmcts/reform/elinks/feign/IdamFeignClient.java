package uk.gov.hmcts.reform.elinks.feign;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.hmcts.reform.elinks.response.IdamOpenIdTokenResponse;

import java.util.Map;


@FeignClient(name = "IdamFeignClient", url = "${idam.api.url}")
public interface IdamFeignClient {

    @GetMapping(value = "/api/v1/users")
    Response getUserFeed(@RequestHeader("Authorization") String authorization,
                                @RequestParam  Map<String, String> params);

    @PostMapping(value = "/o/token", consumes = {"application/x-www-form-urlencoded"})
    IdamOpenIdTokenResponse getOpenIdToken(@RequestParam Map<String, String> params);

}
