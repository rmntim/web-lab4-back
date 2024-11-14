package ru.rmntim.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointDTO {
    private double x;
    private double y;
    private double r;
    private boolean result;
}

