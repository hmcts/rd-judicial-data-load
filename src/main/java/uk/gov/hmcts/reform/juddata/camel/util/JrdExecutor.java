package uk.gov.hmcts.reform.juddata.camel.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.FileStatus;
import uk.gov.hmcts.reform.data.ingestion.camel.service.AuditServiceImpl;
import uk.gov.hmcts.reform.data.ingestion.camel.service.IEmailService;
import uk.gov.hmcts.reform.data.ingestion.camel.service.dto.Email;
import uk.gov.hmcts.reform.data.ingestion.camel.util.RouteExecutor;
import uk.gov.hmcts.reform.juddata.configuration.EmailConfiguration;

import java.util.List;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang.BooleanUtils.isNotTrue;
import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.DataLoadUtil.getFileDetails;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.FAILURE;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.CONTENT_TYPE_PLAIN;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.IS_PARENT;

@Slf4j
@Component
@RefreshScope
public class JrdExecutor extends RouteExecutor {

    @Autowired
    AuditServiceImpl judicialAuditServiceImpl;

    @Value("${logging-component-name}")
    private String logComponentName;

    @Autowired
    protected IEmailService emailService;

    @Value("${archival-file-names}")
    List<String> archivalFileNames;

    @Value("${ENV_NAME:''}")
    private String environment;

    @Autowired
    private EmailConfiguration emailConfiguration;

    @Override
    public String execute(CamelContext camelContext, String schedulerName, String route) {
        try {
            return super.execute(camelContext, schedulerName, route);
        } finally {
            if (nonNull(camelContext.getGlobalOption(IS_PARENT))) {
                //runs Job Auditing
                judicialAuditServiceImpl.auditSchedulerStatus(camelContext);
            }
            List<String> fileStatuses = archivalFileNames.stream()
                .map(s -> getFileDetails(camelContext, s))
                .filter(fileStatus -> nonNull(fileStatus.getAuditStatus())
                    && fileStatus.getAuditStatus().equalsIgnoreCase(FAILURE))
                .map(FileStatus::getFileName).toList();
            if (isNotTrue(isEmpty(fileStatuses))) {
                EmailConfiguration.MailTypeConfig mailTypeConfig = emailConfiguration.getMailTypes().get("report");
                if (mailTypeConfig.isEnabled()) {
                    emailService.sendEmail(Email.builder()
                            .contentType(CONTENT_TYPE_PLAIN)
                            .from(mailTypeConfig.getFrom())
                            .to(mailTypeConfig.getTo())
                            .messageBody(String.format(mailTypeConfig.getBody(), StringUtils.join(fileStatuses, ",")))
                            .subject(String.format(mailTypeConfig.getSubject(), environment))
                            .build());
                }
            }
        }
    }
}
