package uk.gov.hmcts.reform.elinks.feign;


import feign.Headers;
import feign.RequestLine;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import uk.gov.hmcts.reform.elinks.configuration.ElinksFeignInterceptorConfiguration;

@FeignClient(name = "ElinksFeignClient", url = "${elinksUrl}",
        configuration = ElinksFeignInterceptorConfiguration.class)
public interface ElinksFeignClient {

    //URL not tested , code is for developers reference
    @GetMapping(value = "/api/v4/reference_data/location")
    @RequestLine("GET /api/v4/reference_data/location")
    @Headers({"Authorization: {authorization}",
            "Content-Type: application/json"})
    Response getLocationDetails();


}
