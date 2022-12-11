package uk.gov.hmcts.reform.elinks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.elinks.domain.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {

}
