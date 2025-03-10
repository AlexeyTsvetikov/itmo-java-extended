package com.example.itmo.extended.model.dto.request;

import com.example.itmo.extended.model.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoRequest {

    @NotEmpty
    @Schema(description = "Email")
    private String email;

    @Schema(description = "Пароль")
    private String password;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Отчество")
    private String middleName;

    @NotNull
    @Schema(description = "Возраст")
    private Integer age;

    @Schema(description = "Пол")
    private Gender gender;

}
