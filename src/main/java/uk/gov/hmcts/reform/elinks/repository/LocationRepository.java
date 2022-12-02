package uk.gov.hmcts.reform.elinks.repository;

import uk.gov.hmcts.reform.elinks.domain.BaseLocation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRepository extends JpaRepository<BaseLocation, String> {

}
