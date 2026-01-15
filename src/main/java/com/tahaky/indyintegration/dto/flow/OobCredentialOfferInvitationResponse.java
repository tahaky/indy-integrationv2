package com.tahaky.indyintegration.dto.flow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OobCredentialOfferInvitationResponse {
    
    @JsonProperty("invitation_url")
    private String invitationUrl;
    
    @JsonProperty("oob_id")
    private String oobId;
    
    @JsonProperty("invi_msg_id")
    private String inviMsgId;
    
    @JsonProperty("cred_ex_id")
    private String credExId;
}
