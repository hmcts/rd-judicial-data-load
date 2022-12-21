package uk.gov.hmcts.reform.elinks.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
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
public class ElinkDataSchedularAudit implements Serializable {

    @Id
    @GeneratedValue()
    @Column(name = "id")
    private int id;

    @Column(name = "scheduler_name")
    private String schedulerName;

    @Column(name = "scheduler_start_time")
    private LocalDateTime schedulerStartTime;

    @Column(name = "scheduler_end_time")
    private LocalDateTime schedulerEndTime;

    @Column(name = "status")
    private String status;

    @Column(name = "api_name")
    private String apiName;

}
