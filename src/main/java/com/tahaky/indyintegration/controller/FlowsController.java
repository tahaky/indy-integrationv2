package com.tahaky.indyintegration.controller;

import com.tahaky.indyintegration.dto.flow.*;
import com.tahaky.indyintegration.service.FlowsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/flows")
@Tag(name = "Flows", description = "Orchestration flow endpoints")
public class FlowsController {

    private final FlowsService flowsService;

    public FlowsController(FlowsService flowsService) {
        this.flowsService = flowsService;
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
        return flowsService.createOobProofInvitation(request);
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
        return flowsService.createOobCredentialOfferInvitation(request);
    }
}
