package uk.gov.hmcts.reform.juddata.camel.service;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class EmailData {
    String recipient;
    String subject;
    String message;
}