package uk.gov.hmcts.reform.elinks.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(
    ignoreUnknown = true
)
public class IdamResponse {
    
    @JsonProperty("active")
    private boolean active;

    @JsonProperty("email")
    private String email;
    
    @JsonProperty("forename")
    private String forename;

    @JsonProperty("id")
    private String id;

    @JsonProperty("lastModified")
    private String lastModified;

    @JsonProperty("pending")
    private boolean pending;

    @JsonProperty("roles")
    private List<String> roles;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("ssoId")
    private String ssoId;
}
