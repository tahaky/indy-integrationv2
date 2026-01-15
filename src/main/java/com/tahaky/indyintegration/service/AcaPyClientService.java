package com.tahaky.indyintegration.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class AcaPyClientService {

    private final WebClient webClient;

    public AcaPyClientService(@Qualifier("acapyWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> Mono<T> get(String path, Class<T> responseType) {
        return webClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<T> post(String path, Object request, Class<T> responseType) {
        return webClient.post()
                .uri(path)
                .bodyValue(request != null ? request : Map.of())
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<T> delete(String path, Class<T> responseType) {
        return webClient.delete()
                .uri(path)
                .retrieve()
                .bodyToMono(responseType);
    }

    public Mono<Void> deleteVoid(String path) {
        return webClient.delete()
                .uri(path)
                .retrieve()
                .bodyToMono(Void.class);
    }
    
    // Helper method for Map returns
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> getMap(String path) {
        return webClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono((Class<Map<String, Object>>)(Class<?>)Map.class);
    }
    
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> postMap(String path, Object request) {
        return webClient.post()
                .uri(path)
                .bodyValue(request != null ? request : Map.of())
                .retrieve()
                .bodyToMono((Class<Map<String, Object>>)(Class<?>)Map.class);
    }
}
