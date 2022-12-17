package uk.gov.hmcts.reform.elinks.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "dataload_schedular_audit")
@Table(name = "dbjudicialdata.dataload_schedular_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataloadSchedularAudit  implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    private String schedulerName;

    private String schedulerStartTime;

    private String schedulerEndTime;

    private String status;

    private String fileName;
}
