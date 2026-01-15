package com.tahaky.indyintegration.mapper;

import com.tahaky.indyintegration.dto.credential.CredentialExchangeRecord;
import com.tahaky.indyintegration.dto.credential.IssueCredentialResponse;
import com.tahaky.indyintegration.dto.credential.SendCredentialRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper for credential-related operations.
 * Handles mapping between different credential data structures.
 */
@Component
public class CredentialMapper {

    /**
     * Builds a credential preview structure from attributes map.
     * The credential preview follows ACA-Py's expected format.
     *
     * @param attributes Map of attribute names to values
     * @return Map representing the credential preview
     */
    public Map<String, Object> buildCredentialPreview(Map<String, String> attributes) {
        Map<String, Object> preview = new HashMap<>();
        preview.put("@type", "https://didcomm.org/issue-credential/2.0/credential-preview");
        
        List<Map<String, String>> attributesList = new ArrayList<>();
        attributes.forEach((name, value) ->
                attributesList.add(Map.of("name", name, "value", value)));
        
        preview.put("attributes", attributesList);
        return preview;
    }

    /**
     * Builds a filter structure with credential definition ID.
     *
     * @param credDefId The credential definition ID
     * @return Map representing the filter
     */
    public Map<String, Object> buildFilter(String credDefId) {
        Map<String, Object> filter = new HashMap<>();
        Map<String, Object> indyFilter = new HashMap<>();
        indyFilter.put("cred_def_id", credDefId);
        filter.put("indy", indyFilter);
        return filter;
    }

    /**
     * Maps a CredentialExchangeRecord to IssueCredentialResponse.
     *
     * @param record The credential exchange record from ACA-Py
     * @return The response DTO
     */
    public IssueCredentialResponse toIssueCredentialResponse(CredentialExchangeRecord record) {
        return IssueCredentialResponse.builder()
                .credExId(record.getCredExId())
                .connectionId(record.getConnectionId())
                .state(record.getState())
                .threadId(record.getThreadId())
                .createdAt(record.getCreatedAt())
                .build();
    }

    /**
     * Builds a SendCredentialRequest from the provided parameters.
     *
     * @param connectionId The connection ID
     * @param credDefId The credential definition ID
     * @param attributes The credential attributes
     * @param comment Optional comment
     * @param autoRemove Whether to auto-remove the credential
     * @param trace Whether to trace the credential
     * @return The send credential request
     */
    public SendCredentialRequest buildSendCredentialRequest(
            String connectionId,
            String credDefId,
            Map<String, String> attributes,
            String comment,
            Boolean autoRemove,
            Boolean trace) {
        
        Map<String, Object> credentialPreview = buildCredentialPreview(attributes);
        Map<String, Object> filter = buildFilter(credDefId);
        
        return SendCredentialRequest.builder()
                .connectionId(connectionId)
                .filter(filter)
                .credentialPreview(credentialPreview)
                .comment(comment)
                .autoRemove(autoRemove != null ? autoRemove : false)
                .trace(trace != null ? trace : false)
                .build();
    }
}
