package com.tahaky.indyintegration.controller;

import com.tahaky.indyintegration.dto.proof.CreateProofRequestRequest;
import com.tahaky.indyintegration.dto.proof.PresentationExchangeRecord;
import com.tahaky.indyintegration.dto.proof.PresentationRecordListResponse;
import com.tahaky.indyintegration.dto.proof.SendProofRequestRequest;
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
@RequestMapping("/v1/proofs/v2")
@Tag(name = "Proofs V2", description = "Present Proof 2.0 protocol endpoints")
public class ProofsController {

    private final AcaPyClientService acaPyClient;

    public ProofsController(AcaPyClientService acaPyClient) {
        this.acaPyClient = acaPyClient;
    }

    @PostMapping("/requests")
    @Operation(summary = "Create proof request", description = "Creates a new proof request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PresentationExchangeRecord> createRequest(@RequestBody CreateProofRequestRequest request) {
        return acaPyClient.post("/present-proof-2.0/create-request", request, PresentationExchangeRecord.class);
    }

    @PostMapping("/send-request")
    @Operation(summary = "Send proof request", description = "Sends a proof request to a connection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request sent successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PresentationExchangeRecord> sendRequest(@RequestBody SendProofRequestRequest request) {
        return acaPyClient.post("/present-proof-2.0/send-request", request, PresentationExchangeRecord.class);
    }

    @GetMapping("/records")
    @Operation(summary = "List proof records", description = "Retrieves a list of all presentation exchange records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Records retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PresentationRecordListResponse> listRecords() {
        return acaPyClient.get("/present-proof-2.0/records", PresentationRecordListResponse.class);
    }

    @GetMapping("/records/{pres_ex_id}")
    @Operation(summary = "Get proof record", description = "Retrieves a specific presentation exchange record by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Record retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Record not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PresentationExchangeRecord> getRecord(
            @Parameter(description = "Presentation exchange ID", required = true)
            @PathVariable("pres_ex_id") String presExId) {
        return acaPyClient.get("/present-proof-2.0/records/" + presExId, PresentationExchangeRecord.class);
    }

    @PostMapping("/records/{pres_ex_id}/verify")
    @Operation(summary = "Verify presentation", description = "Verifies a received presentation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification initiated successfully"),
            @ApiResponse(responseCode = "404", description = "Record not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PresentationExchangeRecord> verifyPresentation(
            @Parameter(description = "Presentation exchange ID", required = true)
            @PathVariable("pres_ex_id") String presExId) {
        return acaPyClient.post("/present-proof-2.0/records/" + presExId + "/verify-presentation",
                null, PresentationExchangeRecord.class);
    }
}
