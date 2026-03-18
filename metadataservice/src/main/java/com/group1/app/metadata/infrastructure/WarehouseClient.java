package com.group1.app.metadata.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class WarehouseClient {

    private final WebClient warehouseWebClient;

    public JsonNode getWarehouseById(String warehouseId) {
        try {
            return warehouseWebClient.get()
                    .uri("/api/warehouse-service/warehouses/{id}", warehouseId)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .timeout(Duration.ofSeconds(3)) // ✅ thêm timeout
                    .block();
        } catch (Exception e) {
            System.err.println("Warehouse call failed: " + e.getMessage());
            return null; // (tạm chấp nhận)
        }
    }
}
