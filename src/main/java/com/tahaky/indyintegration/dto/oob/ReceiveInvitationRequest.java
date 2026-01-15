package com.tahaky.indyintegration.dto.oob;

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
public class ReceiveInvitationRequest {
    
    @JsonProperty("invitation")
    private Map<String, Object> invitation;
    
    @JsonProperty("alias")
    private String alias;
    
    @JsonProperty("auto_accept")
    private Boolean autoAccept;
    
    @JsonProperty("use_existing_connection")
    private Boolean useExistingConnection;
}
