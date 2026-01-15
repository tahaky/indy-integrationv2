package com.tahaky.indyintegration.dto.credential;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response from automated credential issuance")
public class IssueCredentialResponse {
    
    @JsonProperty("cred_ex_id")
    @Schema(description = "Credential exchange ID")
    private String credExId;
    
    @JsonProperty("connection_id")
    @Schema(description = "Connection ID")
    private String connectionId;
    
    @JsonProperty("state")
    @Schema(description = "Current state of the credential exchange")
    private String state;
    
    @JsonProperty("thread_id")
    @Schema(description = "Thread ID for tracking the exchange")
    private String threadId;
    
    @JsonProperty("created_at")
    @Schema(description = "Timestamp when the exchange was created")
    private String createdAt;
}
