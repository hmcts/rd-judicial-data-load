package uk.gov.hmcts.reform.elinks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.elinks.domain.ElinkDataSchedularAudit;


@Repository
public interface ElinkSchedularAuditRepository  extends JpaRepository<ElinkDataSchedularAudit, Long> {

}
