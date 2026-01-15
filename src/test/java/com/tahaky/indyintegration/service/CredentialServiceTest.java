package com.tahaky.indyintegration.service;

import com.tahaky.indyintegration.dto.credential.CredentialExchangeRecord;
import com.tahaky.indyintegration.dto.credential.IssueCredentialRequest;
import com.tahaky.indyintegration.dto.credential.IssueCredentialResponse;
import com.tahaky.indyintegration.dto.credential.SendCredentialRequest;
import com.tahaky.indyintegration.mapper.CredentialMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CredentialServiceTest {

    @Mock
    private AcaPyClientService acaPyClient;

    @Mock
    private CredentialMapper credentialMapper;

    private CredentialService credentialService;

    @BeforeEach
    void setUp() {
        credentialService = new CredentialService(acaPyClient, credentialMapper);
    }

    @Test
    void testIssueCredential() {
        // Arrange
        Map<String, String> attributes = new HashMap<>();
        attributes.put("name", "John Doe");
        attributes.put("email", "john@example.com");

        IssueCredentialRequest request = IssueCredentialRequest.builder()
                .connectionId("conn-123")
                .credDefId("WgWxqztrNooG92RXvxSTWv:3:CL:20:tag")
                .attributes(attributes)
                .comment("Test credential")
                .autoRemove(false)
                .trace(false)
                .build();

        SendCredentialRequest sendRequest = SendCredentialRequest.builder()
                .connectionId("conn-123")
                .build();

        CredentialExchangeRecord mockRecord = CredentialExchangeRecord.builder()
                .credExId("cred-ex-123")
                .connectionId("conn-123")
                .state("offer-sent")
                .threadId("thread-123")
                .createdAt("2026-01-15T22:00:00.000Z")
                .build();

        IssueCredentialResponse expectedResponse = IssueCredentialResponse.builder()
                .credExId("cred-ex-123")
                .connectionId("conn-123")
                .state("offer-sent")
                .threadId("thread-123")
                .createdAt("2026-01-15T22:00:00.000Z")
                .build();

        when(credentialMapper.buildSendCredentialRequest(
                eq("conn-123"),
                eq("WgWxqztrNooG92RXvxSTWv:3:CL:20:tag"),
                eq(attributes),
                eq("Test credential"),
                eq(false),
                eq(false)
        )).thenReturn(sendRequest);

        when(acaPyClient.post(
                eq("/issue-credential-2.0/send"),
                eq(sendRequest),
                eq(CredentialExchangeRecord.class)
        )).thenReturn(Mono.just(mockRecord));

        when(credentialMapper.toIssueCredentialResponse(mockRecord))
                .thenReturn(expectedResponse);

        // Act
        IssueCredentialResponse result = credentialService.issueCredential(request).block();

        // Assert
        assertNotNull(result);
        assertEquals("cred-ex-123", result.getCredExId());
        assertEquals("conn-123", result.getConnectionId());
        assertEquals("offer-sent", result.getState());

        // Verify interactions
        verify(credentialMapper).buildSendCredentialRequest(
                eq("conn-123"),
                eq("WgWxqztrNooG92RXvxSTWv:3:CL:20:tag"),
                eq(attributes),
                eq("Test credential"),
                eq(false),
                eq(false)
        );
        verify(acaPyClient).post(
                eq("/issue-credential-2.0/send"),
                eq(sendRequest),
                eq(CredentialExchangeRecord.class)
        );
        verify(credentialMapper).toIssueCredentialResponse(mockRecord);
    }
}
