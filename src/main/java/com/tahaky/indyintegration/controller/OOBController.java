package com.tahaky.indyintegration.controller;

import com.tahaky.indyintegration.dto.oob.CreateInvitationRequest;
import com.tahaky.indyintegration.dto.oob.InvitationResponse;
import com.tahaky.indyintegration.dto.oob.ReceiveInvitationRequest;
import com.tahaky.indyintegration.service.AcaPyClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/v1/oob")
@Tag(name = "Out-of-Band", description = "Out-of-band invitation endpoints")
public class OOBController {

    private final AcaPyClientService acaPyClient;

    public OOBController(AcaPyClientService acaPyClient) {
        this.acaPyClient = acaPyClient;
    }

    @PostMapping("/invitations")
    @Operation(summary = "Create OOB invitation", description = "Creates a new out-of-band invitation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invitation created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<InvitationResponse> createInvitation(@RequestBody CreateInvitationRequest request) {
        return acaPyClient.post("/out-of-band/create-invitation", request, InvitationResponse.class);
    }

    @PostMapping("/invitations/receive")
    @Operation(summary = "Receive OOB invitation", description = "Receives an out-of-band invitation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invitation received successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<Map<String, Object>> receiveInvitation(@RequestBody ReceiveInvitationRequest request) {
        return acaPyClient.postMap("/out-of-band/receive-invitation", request);
    }

    @DeleteMapping("/invitations/{invi_msg_id}")
    @Operation(summary = "Delete OOB invitation", description = "Deletes an out-of-band invitation by invitation message ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invitation deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Invitation not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<Void> deleteInvitation(
            @Parameter(description = "Invitation message ID", required = true)
            @PathVariable("invi_msg_id") String inviMsgId) {
        return acaPyClient.deleteVoid("/out-of-band/invitations/" + inviMsgId);
    }
}
