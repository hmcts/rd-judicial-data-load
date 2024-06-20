package uk.gov.hmcts.reform.elinks;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(
        path = "/refdata/internal/v1/elinks" //need to decide the path
)
@RestController
@Slf4j
@NoArgsConstructor
//@AllArgsConstructor
public class ELinksController {


}
