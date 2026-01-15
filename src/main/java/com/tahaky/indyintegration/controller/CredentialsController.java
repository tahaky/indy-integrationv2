package com.tahaky.indyintegration.controller;

import com.tahaky.indyintegration.dto.credential.CreateOfferRequest;
import com.tahaky.indyintegration.dto.credential.CredentialExchangeRecord;
import com.tahaky.indyintegration.dto.credential.CredentialRecordListResponse;
import com.tahaky.indyintegration.dto.credential.SendCredentialRequest;
import com.tahaky.indyintegration.service.AcaPyClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/credentials/v2")
@Tag(name = "Credentials V2", description = "Issue Credential 2.0 protocol endpoints")
public class CredentialsController {

    private final AcaPyClientService acaPyClient;

    public CredentialsController(AcaPyClientService acaPyClient) {
        this.acaPyClient = acaPyClient;
    }

    @PostMapping("/offers")
    @Operation(summary = "Create credential offer", description = "Creates a new credential offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<CredentialExchangeRecord> createOffer(@RequestBody CreateOfferRequest request) {
        return acaPyClient.post("/issue-credential-2.0/create-offer", request, CredentialExchangeRecord.class);
    }

    @PostMapping("/send")
    @Operation(summary = "Send credential", description = "Sends a credential to a connection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credential sent successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<CredentialExchangeRecord> sendCredential(@RequestBody SendCredentialRequest request) {
        return acaPyClient.post("/issue-credential-2.0/send", request, CredentialExchangeRecord.class);
    }

    @GetMapping("/records")
    @Operation(summary = "List credential records", description = "Retrieves a list of all credential exchange records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Records retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<CredentialRecordListResponse> listRecords() {
        return acaPyClient.get("/issue-credential-2.0/records", CredentialRecordListResponse.class);
    }

    @GetMapping("/records/{cred_ex_id}")
    @Operation(summary = "Get credential record", description = "Retrieves a specific credential exchange record by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Record retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Record not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<CredentialExchangeRecord> getRecord(
            @Parameter(description = "Credential exchange ID", required = true)
            @PathVariable("cred_ex_id") String credExId) {
        return acaPyClient.get("/issue-credential-2.0/records/" + credExId, CredentialExchangeRecord.class);
    }
}
