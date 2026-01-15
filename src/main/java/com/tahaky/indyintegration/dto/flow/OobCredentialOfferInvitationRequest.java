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
public class OobCredentialOfferInvitationRequest {
    
    @JsonProperty("filter")
    private Map<String, Object> filter;
    
    @JsonProperty("credential_preview")
    private Map<String, Object> credentialPreview;
    
    @JsonProperty("comment")
    private String comment;
    
    @JsonProperty("alias")
    private String alias;
    
    @JsonProperty("auto_issue")
    private Boolean autoIssue;
}
