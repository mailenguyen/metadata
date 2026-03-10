package com.group1.metadataservice.data.contract;

import com.group1.metadataservice.model.entity.contract.Contract;
import com.group1.metadataservice.model.entity.contract.ContractStatus;
import com.group1.metadataservice.repository.contract.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ContractSeeder implements ApplicationRunner {

    private final ContractRepository repository;

    @Override
    public void run(ApplicationArguments args) {
        if (repository.count() > 0) return;

        repository.saveAll(List.of(
                Contract.builder()
                        .contractNumber("CN-2025-001")
                        .franchiseId(UUID.randomUUID()) // Giả lập ID
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusYears(1))
                        .royaltyRate(new BigDecimal("5.0"))
                        .status(ContractStatus.ACTIVE)
                        .autoOrderEnabled(true)
                        .createdBy("system-seeder")
                        .build(),

                Contract.builder()
                        .contractNumber("CN-2025-002")
                        .franchiseId(UUID.randomUUID()) // Giả lập ID
                        .startDate(LocalDate.now().plusMonths(1))
                        .endDate(LocalDate.now().plusYears(2))
                        .royaltyRate(new BigDecimal("7.5"))
                        .status(ContractStatus.DRAFT)
                        .autoOrderEnabled(true)
                        .createdBy("system-seeder")
                        .build()
        ));
    }
}
