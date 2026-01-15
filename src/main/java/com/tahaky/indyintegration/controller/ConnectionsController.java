package com.tahaky.indyintegration.controller;

import com.tahaky.indyintegration.dto.connection.ConnectionListResponse;
import com.tahaky.indyintegration.dto.connection.ConnectionRecord;
import com.tahaky.indyintegration.dto.connection.SendMessageRequest;
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
@RequestMapping("/v1/connections")
@Tag(name = "Connections", description = "Connection management endpoints")
public class ConnectionsController {

    private final AcaPyClientService acaPyClient;

    public ConnectionsController(AcaPyClientService acaPyClient) {
        this.acaPyClient = acaPyClient;
    }

    @GetMapping
    @Operation(summary = "List connections", description = "Retrieves a list of all connections")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connections retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ConnectionListResponse> listConnections() {
        return acaPyClient.get("/connections", ConnectionListResponse.class);
    }

    @GetMapping("/{conn_id}")
    @Operation(summary = "Get connection", description = "Retrieves a specific connection by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connection retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Connection not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ConnectionRecord> getConnection(
            @Parameter(description = "Connection ID", required = true)
            @PathVariable("conn_id") String connId) {
        return acaPyClient.get("/connections/" + connId, ConnectionRecord.class);
    }

    @PostMapping("/{conn_id}/message")
    @Operation(summary = "Send basic message", description = "Sends a basic message to a connection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully"),
            @ApiResponse(responseCode = "404", description = "Connection not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<Map<String, Object>> sendMessage(
            @Parameter(description = "Connection ID", required = true)
            @PathVariable("conn_id") String connId,
            @RequestBody SendMessageRequest request) {
        return acaPyClient.postMap("/connections/" + connId + "/send-message", request);
    }
}
