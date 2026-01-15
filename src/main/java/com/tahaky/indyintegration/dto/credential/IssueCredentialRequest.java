package com.tahaky.indyintegration.dto.credential;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Request to automatically issue a credential")
public class IssueCredentialRequest {
    
    @NotBlank(message = "Connection ID is required")
    @JsonProperty("connection_id")
    @Schema(description = "Connection ID to send the credential to", required = true, example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String connectionId;
    
    @NotBlank(message = "Credential definition ID is required")
    @JsonProperty("cred_def_id")
    @Schema(description = "Credential definition ID", required = true, example = "WgWxqztrNooG92RXvxSTWv:3:CL:20:tag")
    private String credDefId;
    
    @NotNull(message = "Attributes are required")
    @JsonProperty("attributes")
    @Schema(description = "Generic JSON map of credential attributes according to the credential schema", 
            required = true,
            example = "{\"name\": \"John Doe\", \"age\": \"30\", \"email\": \"john@example.com\"}")
    private Map<String, String> attributes;
    
    @JsonProperty("comment")
    @Schema(description = "Optional comment for the credential", example = "Identity verification credential")
    private String comment;
    
    @JsonProperty("auto_issue")
    @Schema(description = "Whether to automatically issue the credential after the holder accepts the offer (default: true)", 
            defaultValue = "true")
    private Boolean autoIssue;
    
    @JsonProperty("auto_remove")
    @Schema(description = "Whether to automatically remove the credential exchange record after issuance", 
            defaultValue = "false")
    private Boolean autoRemove;
    
    @JsonProperty("trace")
    @Schema(description = "Whether to enable trace messages for debugging", defaultValue = "false")
    private Boolean trace;
}
