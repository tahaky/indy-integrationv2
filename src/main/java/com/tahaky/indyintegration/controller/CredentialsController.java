package com.tahaky.indyintegration.controller;

import com.tahaky.indyintegration.dto.credential.CreateOfferRequest;
import com.tahaky.indyintegration.dto.credential.CredentialExchangeRecord;
import com.tahaky.indyintegration.dto.credential.CredentialRecordListResponse;
import com.tahaky.indyintegration.dto.credential.SendCredentialRequest;
import com.tahaky.indyintegration.dto.credential.IssueCredentialRequest;
import com.tahaky.indyintegration.dto.credential.IssueCredentialResponse;
import com.tahaky.indyintegration.service.AcaPyClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/issue")
    @Operation(summary = "Automated credential issuance",
            description = "Automatically issues a credential to a connection with the provided attributes. " +
                    "This endpoint accepts a credential definition ID and generic JSON attributes, " +
                    "and handles the complete issuance process including control flows and approval mechanisms.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credential issued successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Connection or credential definition not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<IssueCredentialResponse> issueCredential(@Valid @RequestBody IssueCredentialRequest request) {
        // Build credential preview with attributes
        Map<String, Object> credentialPreview = buildCredentialPreview(request.getAttributes());
        
        // Build filter with cred_def_id
        Map<String, Object> filter = new HashMap<>();
        Map<String, Object> indyFilter = new HashMap<>();
        indyFilter.put("cred_def_id", request.getCredDefId());
        filter.put("indy", indyFilter);
        
        // Build the send credential request
        SendCredentialRequest sendRequest = SendCredentialRequest.builder()
                .connectionId(request.getConnectionId())
                .filter(filter)
                .credentialPreview(credentialPreview)
                .comment(request.getComment())
                .autoRemove(request.getAutoRemove() != null ? request.getAutoRemove() : false)
                .trace(request.getTrace() != null ? request.getTrace() : false)
                .build();
        
        // Call ACA-Py send credential endpoint
        return acaPyClient.post("/issue-credential-2.0/send", sendRequest, CredentialExchangeRecord.class)
                .map(record -> IssueCredentialResponse.builder()
                        .credExId(record.getCredExId())
                        .connectionId(record.getConnectionId())
                        .state(record.getState())
                        .threadId(record.getThreadId())
                        .createdAt(record.getCreatedAt())
                        .build());
    }
    
    /**
     * Builds the credential preview structure from attributes map.
     * The credential preview follows ACA-Py's expected format.
     */
    private Map<String, Object> buildCredentialPreview(Map<String, String> attributes) {
        Map<String, Object> preview = new HashMap<>();
        preview.put("@type", "https://didcomm.org/issue-credential/2.0/credential-preview");
        
        List<Map<String, String>> attributesList = new ArrayList<>();
        attributes.forEach((name, value) -> {
            Map<String, String> attribute = new HashMap<>();
            attribute.put("name", name);
            attribute.put("value", value);
            attributesList.add(attribute);
        });
        
        preview.put("attributes", attributesList);
        return preview;
    }
}
