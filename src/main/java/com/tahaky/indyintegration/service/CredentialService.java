package com.tahaky.indyintegration.service;

import com.tahaky.indyintegration.dto.credential.CredentialExchangeRecord;
import com.tahaky.indyintegration.dto.credential.IssueCredentialRequest;
import com.tahaky.indyintegration.dto.credential.IssueCredentialResponse;
import com.tahaky.indyintegration.dto.credential.SendCredentialRequest;
import com.tahaky.indyintegration.mapper.CredentialMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service for credential issuance operations.
 * Handles business logic for issuing credentials.
 */
@Service
public class CredentialService {

    private final AcaPyClientService acaPyClient;
    private final CredentialMapper credentialMapper;

    public CredentialService(AcaPyClientService acaPyClient, CredentialMapper credentialMapper) {
        this.acaPyClient = acaPyClient;
        this.credentialMapper = credentialMapper;
    }

    /**
     * Issues a credential to a connection with the provided attributes.
     * This method handles the complete issuance process including building the request
     * and mapping the response.
     *
     * @param request The issue credential request
     * @return Mono of the issue credential response
     */
    public Mono<IssueCredentialResponse> issueCredential(IssueCredentialRequest request) {
        // Build the send credential request using mapper
        SendCredentialRequest sendRequest = credentialMapper.buildSendCredentialRequest(
                request.getConnectionId(),
                request.getCredDefId(),
                request.getAttributes(),
                request.getComment(),
                request.getAutoRemove(),
                request.getTrace()
        );
        
        // Call ACA-Py send credential endpoint and map response
        return acaPyClient.post("/issue-credential-2.0/send", sendRequest, CredentialExchangeRecord.class)
                .map(credentialMapper::toIssueCredentialResponse);
    }
}
