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
public class InvitationResponse {
    
    @JsonProperty("invi_msg_id")
    private String inviMsgId;
    
    @JsonProperty("oob_id")
    private String oobId;
    
    @JsonProperty("invitation")
    private Map<String, Object> invitation;
    
    @JsonProperty("invitation_url")
    private String invitationUrl;
    
    @JsonProperty("state")
    private String state;
}
