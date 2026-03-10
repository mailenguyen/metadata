package com.group1.metadataservice.model.dto.franchise.request;

import com.group1.metadataservice.common.enums.DayOfWeekValue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdateOpeningHoursRequest {

    @NotNull(message = "Day of week must not be null")
    private DayOfWeekValue dayOfWeek;

    @NotNull(message = "Open time must not be null")
    private LocalTime openTime;

    @NotNull(message = "Close time must not be null")
    private LocalTime closeTime;

}
