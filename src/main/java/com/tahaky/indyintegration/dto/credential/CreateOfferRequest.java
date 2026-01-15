package com.tahaky.indyintegration.dto.credential;

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
public class CreateOfferRequest {
    
    @JsonProperty("connection_id")
    private String connectionId;
    
    @JsonProperty("filter")
    private Map<String, Object> filter;
    
    @JsonProperty("credential_preview")
    private Map<String, Object> credentialPreview;
    
    @JsonProperty("comment")
    private String comment;
    
    @JsonProperty("auto_issue")
    private Boolean autoIssue;
    
    @JsonProperty("auto_remove")
    private Boolean autoRemove;
    
    @JsonProperty("trace")
    private Boolean trace;
}
