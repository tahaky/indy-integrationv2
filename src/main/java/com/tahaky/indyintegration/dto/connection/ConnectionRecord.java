package com.tahaky.indyintegration.dto.connection;

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
public class ConnectionRecord {
    
    @JsonProperty("connection_id")
    private String connectionId;
    
    @JsonProperty("state")
    private String state;
    
    @JsonProperty("their_label")
    private String theirLabel;
    
    @JsonProperty("their_role")
    private String theirRole;
    
    @JsonProperty("my_did")
    private String myDid;
    
    @JsonProperty("their_did")
    private String theirDid;
    
    @JsonProperty("invitation_key")
    private String invitationKey;
    
    @JsonProperty("alias")
    private String alias;
    
    @JsonProperty("created_at")
    private String createdAt;
    
    @JsonProperty("updated_at")
    private String updatedAt;
    
    @JsonProperty("rfc23_state")
    private String rfc23State;
}
