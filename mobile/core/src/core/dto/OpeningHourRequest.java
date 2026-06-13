package com.example.core.dto;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class OpeningHourRequest {
    private DayOfWeek dayOfWeek;

    private LocalTime openTime;

    private LocalTime closeTime;

    private boolean isValidTimeRange() {
        return openTime != null && closeTime != null && closeTime.isAfter(openTime);
    }

}
