package com.tahaky.indyintegration.controller;

import com.tahaky.indyintegration.service.AcaPyClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@WebFluxTest(StatusController.class)
class StatusControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AcaPyClientService acaPyClientService;

    @Test
    void testGetStatus() {
        // Arrange
        Map<String, Object> mockStatus = new HashMap<>();
        mockStatus.put("version", "0.8.0");
        mockStatus.put("label", "Test Agent");

        when(acaPyClientService.getMap("/status"))
                .thenReturn(Mono.just(mockStatus));

        // Act & Assert
        webTestClient.get()
                .uri("/v1/status")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.version").isEqualTo("0.8.0")
                .jsonPath("$.label").isEqualTo("Test Agent");
    }
}
