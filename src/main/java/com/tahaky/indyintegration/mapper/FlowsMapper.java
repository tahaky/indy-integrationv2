package com.tahaky.indyintegration.mapper;

import com.tahaky.indyintegration.dto.credential.CredentialExchangeRecord;
import com.tahaky.indyintegration.dto.flow.OobCredentialOfferInvitationResponse;
import com.tahaky.indyintegration.dto.flow.OobProofInvitationResponse;
import com.tahaky.indyintegration.dto.oob.CreateInvitationRequest;
import com.tahaky.indyintegration.dto.oob.InvitationResponse;
import com.tahaky.indyintegration.dto.proof.PresentationExchangeRecord;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper for flow-related operations.
 * Handles mapping for OOB (Out-of-Band) invitation flows.
 */
@Component
public class FlowsMapper {

    /**
     * Builds an attachment for OOB invitation from proof request.
     *
     * @param threadId The thread ID
     * @param presentationRequest The presentation request object
     * @return Map representing the attachment
     */
    public Map<String, Object> buildProofAttachment(String threadId, Object presentationRequest) {
        Map<String, Object> attachment = new HashMap<>();
        attachment.put("@id", threadId);
        attachment.put("mime-type", "application/json");
        
        Map<String, Object> data = new HashMap<>();
        data.put("json", presentationRequest);
        attachment.put("data", data);
        
        return attachment;
    }

    /**
     * Builds an attachment for OOB invitation from credential offer.
     *
     * @param threadId The thread ID
     * @param credentialOffer The credential offer object
     * @return Map representing the attachment
     */
    public Map<String, Object> buildCredentialAttachment(String threadId, Object credentialOffer) {
        Map<String, Object> attachment = new HashMap<>();
        attachment.put("@id", threadId);
        attachment.put("mime-type", "application/json");
        
        Map<String, Object> data = new HashMap<>();
        data.put("json", credentialOffer);
        attachment.put("data", data);
        
        return attachment;
    }

    /**
     * Builds a CreateInvitationRequest for OOB with attachments.
     *
     * @param alias Optional alias for the invitation
     * @param attachments List of attachments to include
     * @return The create invitation request
     */
    public CreateInvitationRequest buildInvitationRequest(String alias, List<Map<String, Object>> attachments) {
        return CreateInvitationRequest.builder()
                .alias(alias)
                .handshakeProtocols(List.of("https://didcomm.org/didexchange/1.0"))
                .attachments(attachments)
                .build();
    }

    /**
     * Maps proof record and invitation response to OOB proof invitation response.
     *
     * @param proofRecord The presentation exchange record
     * @param invitationResponse The invitation response
     * @return The OOB proof invitation response
     */
    public OobProofInvitationResponse toOobProofInvitationResponse(
            PresentationExchangeRecord proofRecord,
            InvitationResponse invitationResponse) {
        
        return OobProofInvitationResponse.builder()
                .invitationUrl(invitationResponse.getInvitationUrl())
                .oobId(invitationResponse.getOobId())
                .inviMsgId(invitationResponse.getInviMsgId())
                .presExId(proofRecord.getPresExId())
                .build();
    }

    /**
     * Maps credential record and invitation response to OOB credential offer invitation response.
     *
     * @param credRecord The credential exchange record
     * @param invitationResponse The invitation response
     * @return The OOB credential offer invitation response
     */
    public OobCredentialOfferInvitationResponse toOobCredentialOfferInvitationResponse(
            CredentialExchangeRecord credRecord,
            InvitationResponse invitationResponse) {
        
        return OobCredentialOfferInvitationResponse.builder()
                .invitationUrl(invitationResponse.getInvitationUrl())
                .oobId(invitationResponse.getOobId())
                .inviMsgId(invitationResponse.getInviMsgId())
                .credExId(credRecord.getCredExId())
                .build();
    }
}
