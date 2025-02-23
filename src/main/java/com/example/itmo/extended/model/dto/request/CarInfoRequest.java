package com.example.itmo.extended.model.dto.request;

import com.example.itmo.extended.model.enums.CarType;
import com.example.itmo.extended.model.enums.Color;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarInfoRequest {
    @Schema(description = "Марка")
    private String brand;

    @NotEmpty
    @Schema(description = "Модель")
    private String model;

    @Schema(description = "Цвет")
    private Color color;

    @NotNull
    @Schema(description = "Год выпуска")
    private Integer year;

    @Schema(description = "Цена")
    private Long price;

    @Schema(description = "Новая/БУ")
    private Boolean isNew;

    @Schema(description = "Кузов")
    private CarType type;
}
