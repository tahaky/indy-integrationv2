package com.tahaky.indyintegration.dto.proof;

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
public class SendProofRequestRequest {
    
    @JsonProperty("connection_id")
    private String connectionId;
    
    @JsonProperty("presentation_request")
    private Map<String, Object> presentationRequest;
    
    @JsonProperty("comment")
    private String comment;
    
    @JsonProperty("auto_verify")
    private Boolean autoVerify;
    
    @JsonProperty("trace")
    private Boolean trace;
}
