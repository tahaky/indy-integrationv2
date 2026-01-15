package com.tahaky.indyintegration.dto.flow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OobProofInvitationRequest {
    
    @JsonProperty("presentation_request")
    private Map<String, Object> presentationRequest;
    
    @JsonProperty("comment")
    private String comment;
    
    @JsonProperty("alias")
    private String alias;
    
    @JsonProperty("auto_verify")
    private Boolean autoVerify;
}
