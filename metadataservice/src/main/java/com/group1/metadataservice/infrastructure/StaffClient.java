package com.group1.metadataservice.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;

@Component
@RequiredArgsConstructor
public class StaffClient {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://microservice-dvqh.onrender.com")
            .build();

    public JsonNode getStaffById(String staffId) {
        return webClient.get()
                .uri("/api/shift-service/staffs/{id}", staffId)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
}
