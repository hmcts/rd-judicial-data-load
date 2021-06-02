package uk.gov.hmcts.reform.juddata.camel.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PublishJudicialData {
    @JsonProperty
    private List<String> userIds;
}
