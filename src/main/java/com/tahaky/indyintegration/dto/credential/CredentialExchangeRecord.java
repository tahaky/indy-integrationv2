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
public class CredentialExchangeRecord {
    
    @JsonProperty("cred_ex_id")
    private String credExId;
    
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
    
    @JsonProperty("credential_offer")
    private Map<String, Object> credentialOffer;
    
    @JsonProperty("credential")
    private Map<String, Object> credential;
}
