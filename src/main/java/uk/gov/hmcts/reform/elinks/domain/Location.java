package uk.gov.hmcts.reform.elinks.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity(name = "region_type")
@Getter
@Setter
@NoArgsConstructor
public class Location implements Serializable {

    @Id
    @Column(name = "region_Id")
    @JsonProperty(value = "id")
    private String regionId;

    @Column(name = "region_desc_en")
    @Size(max = 256)
    @JsonProperty(value = "name")
    private String regionDescEn;

    @Column(name = "region_desc_cy")
    @Size(max = 256)
    private String regionDescCy;
}
