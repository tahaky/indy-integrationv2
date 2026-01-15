package com.tahaky.indyintegration.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    private static final Logger log = LoggerFactory.getLogger(WebClientConfig.class);
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Value("${acapy.base-url}")
    private String acapyBaseUrl;

    @Value("${acapy.read-timeout}")
    private int readTimeout;

    @Bean
    public WebClient acapyWebClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(readTimeout));

        return WebClient.builder()
                .baseUrl(acapyBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(correlationIdFilter())
                .filter(loggingFilter())
                .build();
    }

    private ExchangeFilterFunction correlationIdFilter() {
        return (request, next) -> {
            String correlationId = MDC.get("correlationId");
            if (correlationId != null) {
                ClientRequest filtered = ClientRequest.from(request)
                        .header(CORRELATION_ID_HEADER, correlationId)
                        .build();
                return next.exchange(filtered);
            }
            return next.exchange(request);
        };
    }

    private ExchangeFilterFunction loggingFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            long startTime = System.currentTimeMillis();
            log.info("Outbound ACA-Py request: method={}, url={}", 
                    request.method(), request.url());
            
            return Mono.just(ClientRequest.from(request)
                    .attribute("startTime", startTime)
                    .build());
        }).andThen(ExchangeFilterFunction.ofResponseProcessor(response -> {
            // Note: Response attributes are not directly accessible in Spring Boot 3
            // We'll just log the status code without duration for now
            log.info("Outbound ACA-Py response: status={}", response.statusCode());
            return Mono.just(response);
        }));
    }
}
