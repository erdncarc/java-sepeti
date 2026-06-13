package com.example.javasepeti.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class OpeningHourRequest {
    @NotNull(message = "Day of week must not be null.")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Opening time must not be null.")
    private LocalTime openTime;

    @NotNull(message = "Closing time must not be null.")
    private LocalTime closeTime;

    @AssertTrue(message = "Closing time must be after opening time.")
    private boolean isValidTimeRange() {
        return openTime != null && closeTime != null && closeTime.isAfter(openTime);
    }

}
