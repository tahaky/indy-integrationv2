package com.tahaky.indyintegration.controller;

import com.tahaky.indyintegration.dto.flow.*;
import com.tahaky.indyintegration.dto.oob.CreateInvitationRequest;
import com.tahaky.indyintegration.dto.oob.InvitationResponse;
import com.tahaky.indyintegration.dto.proof.CreateProofRequestRequest;
import com.tahaky.indyintegration.dto.proof.PresentationExchangeRecord;
import com.tahaky.indyintegration.dto.credential.CreateOfferRequest;
import com.tahaky.indyintegration.dto.credential.CredentialExchangeRecord;
import com.tahaky.indyintegration.service.AcaPyClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/flows")
@Tag(name = "Flows", description = "Orchestration flow endpoints")
public class FlowsController {

    private final AcaPyClientService acaPyClient;

    public FlowsController(AcaPyClientService acaPyClient) {
        this.acaPyClient = acaPyClient;
    }

    @PostMapping("/oob-proof-invitation")
    @Operation(summary = "Create OOB proof invitation",
            description = "Creates a proof request and wraps it in an out-of-band invitation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OOB proof invitation created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<OobProofInvitationResponse> createOobProofInvitation(
            @RequestBody OobProofInvitationRequest request) {

        // Step 1: Create proof request
        CreateProofRequestRequest proofRequest = CreateProofRequestRequest.builder()
                .presentationRequest(request.getPresentationRequest())
                .comment(request.getComment())
                .autoVerify(request.getAutoVerify())
                .build();

        return acaPyClient.post("/present-proof-2.0/create-request", proofRequest, PresentationExchangeRecord.class)
                .flatMap(proofRecord -> {
                    // Step 2: Create OOB invitation with proof request attachment
                    Map<String, Object> attachment = new HashMap<>();
                    attachment.put("@id", proofRecord.getThreadId());
                    attachment.put("mime-type", "application/json");
                    
                    Map<String, Object> data = new HashMap<>();
                    data.put("json", proofRecord.getPresentationRequest());
                    attachment.put("data", data);

                    CreateInvitationRequest invitationRequest = CreateInvitationRequest.builder()
                            .alias(request.getAlias())
                            .handshakeProtocols(List.of("https://didcomm.org/didexchange/1.0"))
                            .attachments(List.of(attachment))
                            .build();

                    return acaPyClient.post("/out-of-band/create-invitation", invitationRequest, InvitationResponse.class)
                            .map(invitationResponse -> OobProofInvitationResponse.builder()
                                    .invitationUrl(invitationResponse.getInvitationUrl())
                                    .oobId(invitationResponse.getOobId())
                                    .inviMsgId(invitationResponse.getInviMsgId())
                                    .presExId(proofRecord.getPresExId())
                                    .build());
                });
    }

    @PostMapping("/oob-credential-offer-invitation")
    @Operation(summary = "Create OOB credential offer invitation",
            description = "Creates a credential offer and wraps it in an out-of-band invitation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OOB credential offer invitation created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<OobCredentialOfferInvitationResponse> createOobCredentialOfferInvitation(
            @RequestBody OobCredentialOfferInvitationRequest request) {

        // Step 1: Create credential offer
        CreateOfferRequest offerRequest = CreateOfferRequest.builder()
                .filter(request.getFilter())
                .credentialPreview(request.getCredentialPreview())
                .comment(request.getComment())
                .autoIssue(request.getAutoIssue())
                .build();

        return acaPyClient.post("/issue-credential-2.0/create-offer", offerRequest, CredentialExchangeRecord.class)
                .flatMap(credRecord -> {
                    // Step 2: Create OOB invitation with credential offer attachment
                    Map<String, Object> attachment = new HashMap<>();
                    attachment.put("@id", credRecord.getThreadId());
                    attachment.put("mime-type", "application/json");
                    
                    Map<String, Object> data = new HashMap<>();
                    data.put("json", credRecord.getCredentialOffer());
                    attachment.put("data", data);

                    CreateInvitationRequest invitationRequest = CreateInvitationRequest.builder()
                            .alias(request.getAlias())
                            .handshakeProtocols(List.of("https://didcomm.org/didexchange/1.0"))
                            .attachments(List.of(attachment))
                            .build();

                    return acaPyClient.post("/out-of-band/create-invitation", invitationRequest, InvitationResponse.class)
                            .map(invitationResponse -> OobCredentialOfferInvitationResponse.builder()
                                    .invitationUrl(invitationResponse.getInvitationUrl())
                                    .oobId(invitationResponse.getOobId())
                                    .inviMsgId(invitationResponse.getInviMsgId())
                                    .credExId(credRecord.getCredExId())
                                    .build());
                });
    }
}
