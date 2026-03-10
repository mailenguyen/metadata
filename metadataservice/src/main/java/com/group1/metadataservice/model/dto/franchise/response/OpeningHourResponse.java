package com.group1.metadataservice.model.dto.franchise.response;

import com.group1.metadataservice.common.enums.DayOfWeekValue;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OpeningHourResponse {

    private UUID franchiseId;

    private DayOfWeekValue dayOfWeek;

    private LocalTime openTime;

    private LocalTime closeTime;

    private Boolean isClosed;

}
