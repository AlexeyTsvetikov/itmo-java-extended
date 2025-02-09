package com.example.itmo.extended.model.dto.request;

import com.example.itmo.extended.model.enums.CarType;
import com.example.itmo.extended.model.enums.Color;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CarInfoRequest {
    private String brand;
    private String model;
    private Color color;
    private Integer year;
    private Long price;
    private Boolean isNew;
    private CarType type;
}
