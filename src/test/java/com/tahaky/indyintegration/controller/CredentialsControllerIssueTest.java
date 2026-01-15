package com.tahaky.indyintegration.controller;

import com.tahaky.indyintegration.dto.credential.IssueCredentialRequest;
import com.tahaky.indyintegration.dto.credential.IssueCredentialResponse;
import com.tahaky.indyintegration.service.AcaPyClientService;
import com.tahaky.indyintegration.service.CredentialService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(CredentialsController.class)
class CredentialsControllerIssueTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AcaPyClientService acaPyClientService;

    @MockBean
    private CredentialService credentialService;

    @Test
    void testIssueCredential() {
        // Arrange
        Map<String, String> attributes = new HashMap<>();
        attributes.put("name", "John Doe");
        attributes.put("age", "30");
        attributes.put("email", "john@example.com");

        IssueCredentialRequest request = IssueCredentialRequest.builder()
                .connectionId("conn-123")
                .credDefId("WgWxqztrNooG92RXvxSTWv:3:CL:20:tag")
                .attributes(attributes)
                .comment("Test credential")
                .autoIssue(true)
                .build();

        IssueCredentialResponse mockResponse = IssueCredentialResponse.builder()
                .credExId("cred-ex-123")
                .connectionId("conn-123")
                .state("offer-sent")
                .threadId("thread-123")
                .createdAt("2026-01-15T22:00:00.000Z")
                .build();

        when(credentialService.issueCredential(any(IssueCredentialRequest.class)))
                .thenReturn(Mono.just(mockResponse));

        // Act & Assert
        webTestClient.post()
                .uri("/v1/credentials/v2/issue")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.cred_ex_id").isEqualTo("cred-ex-123")
                .jsonPath("$.connection_id").isEqualTo("conn-123")
                .jsonPath("$.state").isEqualTo("offer-sent")
                .jsonPath("$.thread_id").isEqualTo("thread-123")
                .jsonPath("$.created_at").isEqualTo("2026-01-15T22:00:00.000Z");

        // Verify the service was called
        verify(credentialService).issueCredential(any(IssueCredentialRequest.class));
    }

    @Test
    void testIssueCredentialWithMissingCredDefId() {
        // Arrange - missing required field (credDefId)
        Map<String, Object> invalidRequest = new HashMap<>();
        invalidRequest.put("connection_id", "conn-123");
        invalidRequest.put("attributes", Map.of("name", "John Doe"));

        // Act & Assert - Since validation occurs on binding, this should still work
        webTestClient.post()
                .uri("/v1/credentials/v2/issue")
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().is5xxServerError(); // Will be 500 due to validation
    }

    @Test
    void testIssueCredentialWithEmptyAttributes() {
        // Arrange
        IssueCredentialRequest request = IssueCredentialRequest.builder()
                .connectionId("conn-123")
                .credDefId("WgWxqztrNooG92RXvxSTWv:3:CL:20:tag")
                .attributes(Map.of())
                .build();

        IssueCredentialResponse mockResponse = IssueCredentialResponse.builder()
                .credExId("cred-ex-456")
                .connectionId("conn-123")
                .state("offer-sent")
                .build();

        when(credentialService.issueCredential(any(IssueCredentialRequest.class)))
                .thenReturn(Mono.just(mockResponse));

        // Act & Assert - empty attributes should still work
        webTestClient.post()
                .uri("/v1/credentials/v2/issue")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.cred_ex_id").isEqualTo("cred-ex-456");
    }
}
