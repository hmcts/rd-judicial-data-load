package uk.gov.hmcts.reform.juddata.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataModel {

    @JsonProperty("error")
    private Boolean error;
    @JsonProperty("region_id")
    private String regionId;
    @JsonProperty("description")
    private String description;
    @JsonProperty("welsh_description")
    private String welshDescription;
}
