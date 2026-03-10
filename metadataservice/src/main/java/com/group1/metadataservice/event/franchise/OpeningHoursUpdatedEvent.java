package com.group1.metadataservice.event.franchise;

import com.group1.metadataservice.common.enums.DayOfWeekValue;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OpeningHoursUpdatedEvent {

    private UUID franchiseId;

    private String franchiseCode;

    private DayOfWeekValue dayOfWeek;

    private LocalTime openTime;

    private LocalTime closeTime;

    private LocalDateTime updatedAt;

}
