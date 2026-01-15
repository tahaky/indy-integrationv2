package com.tahaky.indyintegration.dto.oob;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateInvitationRequest {
    
    @JsonProperty("alias")
    private String alias;
    
    @JsonProperty("handshake_protocols")
    private List<String> handshakeProtocols;
    
    @JsonProperty("use_public_did")
    private Boolean usePublicDid;
    
    @JsonProperty("attachments")
    private List<Map<String, Object>> attachments;
    
    @JsonProperty("metadata")
    private Map<String, Object> metadata;
}
