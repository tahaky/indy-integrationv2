package com.tahaky.indyintegration.controller;

import com.tahaky.indyintegration.service.AcaPyClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/v1/status")
@Tag(name = "Status", description = "ACA-Py status endpoints")
public class StatusController {

    private final AcaPyClientService acaPyClient;

    public StatusController(AcaPyClientService acaPyClient) {
        this.acaPyClient = acaPyClient;
    }

    @GetMapping
    @Operation(summary = "Get ACA-Py status", description = "Retrieves the status of the ACA-Py agent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<Map<String, Object>> getStatus() {
        return acaPyClient.getMap("/status");
    }
}
