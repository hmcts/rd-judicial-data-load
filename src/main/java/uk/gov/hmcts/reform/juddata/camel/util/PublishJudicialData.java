package uk.gov.hmcts.reform.juddata.camel.util;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PublishJudicialData {
    private List<String> userIds;
}
