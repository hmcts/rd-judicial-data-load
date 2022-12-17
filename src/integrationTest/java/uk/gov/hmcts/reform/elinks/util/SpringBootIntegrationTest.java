package uk.gov.hmcts.reform.elinks.util;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.juddata.JudicialApplication;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JudicialApplication.class, webEnvironment =
        SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class SpringBootIntegrationTest {

    @LocalServerPort
    protected int port;

}
