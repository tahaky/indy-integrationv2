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
public class PresentationExchangeRecord {
    
    @JsonProperty("pres_ex_id")
    private String presExId;
    
    @JsonProperty("connection_id")
    private String connectionId;
    
    @JsonProperty("state")
    private String state;
    
    @JsonProperty("role")
    private String role;
    
    @JsonProperty("thread_id")
    private String threadId;
    
    @JsonProperty("created_at")
    private String createdAt;
    
    @JsonProperty("updated_at")
    private String updatedAt;
    
    @JsonProperty("presentation_request")
    private Map<String, Object> presentationRequest;
    
    @JsonProperty("presentation")
    private Map<String, Object> presentation;
    
    @JsonProperty("verified")
    private String verified;
}
