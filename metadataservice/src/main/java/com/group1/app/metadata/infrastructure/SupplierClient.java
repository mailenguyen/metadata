package com.group1.app.metadata.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.group1.app.common.exception.ApiException;
import com.group1.app.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class SupplierClient {

    private final WebClient webClient;

    public JsonNode getSupplierById(String supplierId) {

        try {
            return webClient.get()
                    .uri("/api/supplier-service/suppliers/{id}", supplierId)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .timeout(Duration.ofSeconds(3)) // ✅ thêm timeout
                    .block();

        } catch (Exception e) {
            // ✅ log để debug
            System.err.println("Supplier call failed: " + e.getMessage());
            throw new ApiException(ErrorCode.SUPPLIER_SERVICE_UNAVAILABLE);
        }
    }
}