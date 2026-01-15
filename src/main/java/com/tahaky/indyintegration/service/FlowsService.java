package com.tahaky.indyintegration.service;

import com.tahaky.indyintegration.dto.credential.CreateOfferRequest;
import com.tahaky.indyintegration.dto.credential.CredentialExchangeRecord;
import com.tahaky.indyintegration.dto.flow.OobCredentialOfferInvitationRequest;
import com.tahaky.indyintegration.dto.flow.OobCredentialOfferInvitationResponse;
import com.tahaky.indyintegration.dto.flow.OobProofInvitationRequest;
import com.tahaky.indyintegration.dto.flow.OobProofInvitationResponse;
import com.tahaky.indyintegration.dto.oob.CreateInvitationRequest;
import com.tahaky.indyintegration.dto.oob.InvitationResponse;
import com.tahaky.indyintegration.dto.proof.CreateProofRequestRequest;
import com.tahaky.indyintegration.dto.proof.PresentationExchangeRecord;
import com.tahaky.indyintegration.mapper.FlowsMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * Service for orchestration flow operations.
 * Handles business logic for OOB (Out-of-Band) invitation flows.
 */
@Service
public class FlowsService {

    private final AcaPyClientService acaPyClient;
    private final FlowsMapper flowsMapper;

    public FlowsService(AcaPyClientService acaPyClient, FlowsMapper flowsMapper) {
        this.acaPyClient = acaPyClient;
        this.flowsMapper = flowsMapper;
    }

    /**
     * Creates a proof request and wraps it in an out-of-band invitation.
     * This orchestrates two API calls: creating the proof request and creating the OOB invitation.
     *
     * @param request The OOB proof invitation request
     * @return Mono of the OOB proof invitation response
     */
    public Mono<OobProofInvitationResponse> createOobProofInvitation(OobProofInvitationRequest request) {
        // Step 1: Create proof request
        CreateProofRequestRequest proofRequest = CreateProofRequestRequest.builder()
                .presentationRequest(request.getPresentationRequest())
                .comment(request.getComment())
                .autoVerify(request.getAutoVerify())
                .build();

        return acaPyClient.post("/present-proof-2.0/create-request", proofRequest, PresentationExchangeRecord.class)
                .flatMap(proofRecord -> {
                    // Step 2: Build attachment and create OOB invitation
                    Map<String, Object> attachment = flowsMapper.buildProofAttachment(
                            proofRecord.getThreadId(),
                            proofRecord.getPresentationRequest()
                    );
                    
                    CreateInvitationRequest invitationRequest = flowsMapper.buildInvitationRequest(
                            request.getAlias(),
                            List.of(attachment)
                    );

                    return acaPyClient.post("/out-of-band/create-invitation", invitationRequest, InvitationResponse.class)
                            .map(invitationResponse -> flowsMapper.toOobProofInvitationResponse(
                                    proofRecord,
                                    invitationResponse
                            ));
                });
    }

    /**
     * Creates a credential offer and wraps it in an out-of-band invitation.
     * This orchestrates two API calls: creating the credential offer and creating the OOB invitation.
     *
     * @param request The OOB credential offer invitation request
     * @return Mono of the OOB credential offer invitation response
     */
    public Mono<OobCredentialOfferInvitationResponse> createOobCredentialOfferInvitation(
            OobCredentialOfferInvitationRequest request) {
        
        // Step 1: Create credential offer
        CreateOfferRequest offerRequest = CreateOfferRequest.builder()
                .filter(request.getFilter())
                .credentialPreview(request.getCredentialPreview())
                .comment(request.getComment())
                .autoIssue(request.getAutoIssue())
                .build();

        return acaPyClient.post("/issue-credential-2.0/create-offer", offerRequest, CredentialExchangeRecord.class)
                .flatMap(credRecord -> {
                    // Step 2: Build attachment and create OOB invitation
                    Map<String, Object> attachment = flowsMapper.buildCredentialAttachment(
                            credRecord.getThreadId(),
                            credRecord.getCredentialOffer()
                    );
                    
                    CreateInvitationRequest invitationRequest = flowsMapper.buildInvitationRequest(
                            request.getAlias(),
                            List.of(attachment)
                    );

                    return acaPyClient.post("/out-of-band/create-invitation", invitationRequest, InvitationResponse.class)
                            .map(invitationResponse -> flowsMapper.toOobCredentialOfferInvitationResponse(
                                    credRecord,
                                    invitationResponse
                            ));
                });
    }
}
