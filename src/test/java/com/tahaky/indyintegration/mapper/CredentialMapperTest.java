package com.tahaky.indyintegration.mapper;

import com.tahaky.indyintegration.dto.credential.CredentialExchangeRecord;
import com.tahaky.indyintegration.dto.credential.IssueCredentialResponse;
import com.tahaky.indyintegration.dto.credential.SendCredentialRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CredentialMapperTest {

    private CredentialMapper credentialMapper;

    @BeforeEach
    void setUp() {
        credentialMapper = new CredentialMapper();
    }

    @Test
    void testBuildCredentialPreview() {
        // Arrange
        Map<String, String> attributes = new HashMap<>();
        attributes.put("name", "John Doe");
        attributes.put("age", "30");
        attributes.put("email", "john@example.com");

        // Act
        Map<String, Object> preview = credentialMapper.buildCredentialPreview(attributes);

        // Assert
        assertNotNull(preview);
        assertEquals("https://didcomm.org/issue-credential/2.0/credential-preview", preview.get("@type"));
        assertTrue(preview.containsKey("attributes"));
        assertInstanceOf(java.util.List.class, preview.get("attributes"));
    }

    @Test
    void testBuildFilter() {
        // Arrange
        String credDefId = "WgWxqztrNooG92RXvxSTWv:3:CL:20:tag";

        // Act
        Map<String, Object> filter = credentialMapper.buildFilter(credDefId);

        // Assert
        assertNotNull(filter);
        assertTrue(filter.containsKey("indy"));
        assertInstanceOf(Map.class, filter.get("indy"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> indyFilter = (Map<String, Object>) filter.get("indy");
        assertEquals(credDefId, indyFilter.get("cred_def_id"));
    }

    @Test
    void testToIssueCredentialResponse() {
        // Arrange
        CredentialExchangeRecord record = CredentialExchangeRecord.builder()
                .credExId("cred-ex-123")
                .connectionId("conn-123")
                .state("offer-sent")
                .threadId("thread-123")
                .createdAt("2026-01-15T22:00:00.000Z")
                .build();

        // Act
        IssueCredentialResponse response = credentialMapper.toIssueCredentialResponse(record);

        // Assert
        assertNotNull(response);
        assertEquals("cred-ex-123", response.getCredExId());
        assertEquals("conn-123", response.getConnectionId());
        assertEquals("offer-sent", response.getState());
        assertEquals("thread-123", response.getThreadId());
        assertEquals("2026-01-15T22:00:00.000Z", response.getCreatedAt());
    }

    @Test
    void testBuildSendCredentialRequest() {
        // Arrange
        String connectionId = "conn-123";
        String credDefId = "WgWxqztrNooG92RXvxSTWv:3:CL:20:tag";
        Map<String, String> attributes = new HashMap<>();
        attributes.put("name", "John Doe");
        attributes.put("age", "30");
        String comment = "Test credential";
        Boolean autoRemove = true;
        Boolean trace = false;

        // Act
        SendCredentialRequest request = credentialMapper.buildSendCredentialRequest(
                connectionId, credDefId, attributes, comment, autoRemove, trace
        );

        // Assert
        assertNotNull(request);
        assertEquals(connectionId, request.getConnectionId());
        assertEquals(comment, request.getComment());
        assertEquals(autoRemove, request.getAutoRemove());
        assertEquals(trace, request.getTrace());
        assertNotNull(request.getFilter());
        assertNotNull(request.getCredentialPreview());
    }

    @Test
    void testBuildSendCredentialRequestWithNullOptionalFields() {
        // Arrange
        String connectionId = "conn-456";
        String credDefId = "WgWxqztrNooG92RXvxSTWv:3:CL:20:tag";
        Map<String, String> attributes = Map.of("name", "Jane Doe");

        // Act
        SendCredentialRequest request = credentialMapper.buildSendCredentialRequest(
                connectionId, credDefId, attributes, null, null, null
        );

        // Assert
        assertNotNull(request);
        assertEquals(connectionId, request.getConnectionId());
        assertNull(request.getComment());
        assertFalse(request.getAutoRemove()); // Should default to false
        assertFalse(request.getTrace()); // Should default to false
        assertNotNull(request.getFilter());
        assertNotNull(request.getCredentialPreview());
    }
}
